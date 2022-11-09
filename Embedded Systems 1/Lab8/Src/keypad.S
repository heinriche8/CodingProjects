//	Evan Heinrich
//	CE2801 sect. 011
//	10/12/2021
//
//	File:
//		keypad.S
//	Description of File:
//		Lab 5 Keypad API
//		Modified 10/26/2021 for Lab 7
//		->	Converted from polling to interrupts
//		->	Scan keypad on button press instead of manually polling
//	(opt) Dependencies:
//		delay.S
//		LCD_Control.S
//		keypad.S

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

// Global Functions
.global key_Init
.global key_ToChar
.global key_ToHexChar
.global key_ToNum

// Constants
.equ RCC_BASE, 		0x40023800	// Base address for RCC
.equ RCC_AHB1ENR, 	0x30		// Offset from RCC to AHB1ENR
.equ RCC_APB2ENR,	0x44		// Offset from RCC to APB2ENR
.equ RCC_GPIOCEN, 	1 << 2		// Location of the GPIOC Enabler
.equ RCC_SYSCFGEN,	1 << 14		// Location of the SYSCFG enabler

.equ GPIOC_BASE, 	0x40020800	// Base address for GPIOC
.equ GPIO_MODER, 	0x0			// Offset to the mode register for all GPIO ports
.equ GPIO_ODR, 		0x14		// Offset to the ODR for all GPIO ports
.equ GPIO_IDR,		0x10		// Offset to the IDR for all GPIO ports
.equ GPIO_PUPDR,	0x0C		// Offset to the PUPDR for all GPIO ports

.equ ROW_INPUT,		0x55		// Mask to set rows as inputs and columns as outputs
.equ COL_INPUT,		0x55 << 8	// Mask to set columns as inputs and rows as outputs
.equ PIN_INPUT,		0b00		// Mask to set a pin as an input
.equ PIN_OUTPUT,	0b01		// Mask to set a pin as an output

.equ SYSCFG_BASE,	0x40013800	// Base address of SYSCFG to configure EXTIx
.equ EXTI_CR2,		0x0C		// Offset to EXTICR2 for configuring EXTI4-EXTI7
.equ EXTI_PC,		0b10		// Mask to set EXTIx to Port C

.equ EXTI_BASE,		0x40013C00	// Base address for EXTI registers
.equ EXTI_RTSR,		0x08		// Offset to rising trigger select
.equ EXTI_PR,		0x14		// Offset to the external interrupt pending register
.equ EXTI_IMR,		0x0			// Offset to interrupt mask register
.equ RISING_MASK,	0b1111		// Mask to set rows as rising edge trigger
.equ EXTI_UNMASK,	0b1111		// Mask to enable EXTI

.equ NVIC_BASE,		0xE000E100	// Base address of the NVIC
.equ NVIC_ISER,		0x00		// Offset to interrupt set enable register
.equ NVIC_ENABLE,	1<<10|1<<23	// Mask to enable interrupts in the NVIC

.equ DEBOUNCE, 		30
.equ ROW_MASK,		0x0F		// Masks off all but rows in a keycode
.equ COL_MASK,		0xF0		// Masks off all but columns in a keycode



// 	Function: key_Init
//	Register-safe!
//	Description:
//		Initializes the GPIO port for use with the keypad
//	Args:
//		N/A
//	Returns:
//		N/A
//	Register Use:
//		R1	-	Instructions/Commands
//		R2	-	Masks
//		R3	-	Masks
// Keypad lives on PC0-PC7
// Row[0] = PC4; Row[3] = PC7
// Col[0] = PC0; Col[3] = PC3
key_Init:
	PUSH {R1-R3, LR}			// Backup

	// Enable GPIOC
	LDR R1, =RCC_BASE			// Load RCC base address
	LDR R2, [R1, #RCC_AHB1ENR]	// Read from the RCC AHB1 enable register
	ORR R2, #RCC_GPIOCEN		// Apply mask to enable GPIOC
	STR R2, [R1, #RCC_AHB1ENR]	// Write back to the RCC

	// Enable SYSCFG
	LDR R2, [R1, #RCC_APB2ENR]
	ORR R2, #RCC_SYSCFGEN
	STR R2, [R1, #RCC_APB2ENR]

	// Configure rows as input, columns as outputs
	LDR R1, =GPIOC_BASE			// Load GPIOC base address
	LDR R2, [R1, #GPIO_MODER]	// Read from the current mode register
	MOV R3, #ROW_INPUT			// Load mask to set rows as input
	BFI R2, R3, #0, #16			// Insert mask where PC0-PC7 live
	STR R2, [R1, #GPIO_MODER]	// Write back to the mode register

	// Configure Pull-down
	LDR R2, [R1, #GPIO_PUPDR]	// Read the current pull-up/down register
	LDR R3, =0xAAAA				// Load the mask to set our pins to pull-down
	ORR R2, R3					// Apply mask
	STR R2, [R1, #GPIO_PUPDR]	// Write back to pull-up/down register

	// Configure all EXTI's to use Port C
	LDR R1, =SYSCFG_BASE		// Load system config base addr
	MOV R3, #EXTI_PC			// Load mask to configure EXTI's as Port C
	LDR R2, [R1, #EXTI_CR2]		// Read from external interrupt CR2
	BFI R2, R3, #0, #4			// Insert Port C mask into EXTI4
	BFI R2, R3, #4, #4			// "                     " EXTI5
	BFI R2, R3, #8, #4			// "                     " EXTI6
	BFI R2, R3, #12, #4			// "                     " EXTI7
	STR R2, [R1, #EXTI_CR2]		// Write

	// Configure rising edge trigger
	LDR R1, =EXTI_BASE			// Load EXTI base addr
	MOV R3, #RISING_MASK		// Load mask for our pins
	LDR R2, [R1, #EXTI_RTSR]	// Read
	BFI R2, R3, #4, #4			// Insert rising edge mask
	STR R2, [R1, #EXTI_RTSR]	// Write

	// Configure interrupt mask
	// EXTI base addr still in R1
	MOV R3, #EXTI_UNMASK		// Load EXTI enable mask
	LDR R2, [R1, #EXTI_IMR]		// Read
	BFI R2, R3, #4, #4			// Insert mask
	STR R2, [R1, #EXTI_IMR]		// Write

	// At this point, the interrupt is configured all the way through
	// the EXTI controls, we just need to enable it in the NVIC

	// Enable interrupts in the NVIC
	LDR R1, =NVIC_BASE			// Load NVIC base addr
	LDR R3, =NVIC_ENABLE		// Load mask to enable interrupts
	LDR R2, [R1, #NVIC_ISER]	// Read
	ORR R2, R3
	STR R2, [R1, #NVIC_ISER]	// Write

	// At this point, the interrupts should be enabled

	// Broadcast onto keypad to "arm" interrupts
	// Otherwise a keypress connects ground to ground
	LDR R1, =GPIOC_BASE
	MOV R2, #0xF
	STR R2, [R1, #GPIO_ODR]

	POP {R1-R3, PC}				// Restore & Return


// 	Function: Key_ToChar
//	Register-safe!
//	Description:
//		Converts a provided one-hot keycode into
//		an ASCII character
//
//		Keycode format: xxxx_yyyy
//		where the x's represent one-hot encoded row index
//		where the y's represent one-hot encoded col index
//	Args:
//		R1	-	One-hot encoded keycode
//	Returns:
//		R0	-	ASCII code
//	Register Use:
//		R0	-	Return
//		R1	-	Argument
//		R2	-	Row index
//		R3	-	Column index
//		R4	-	Masks
key_ToChar:
	PUSH {R1-R4, LR}

	// Extract row index
	MOV R4, #COL_MASK	// Load mask to extract rows
	AND R2, R1, R4		// Extract rows
	LSR R2, R2, #4		// Shift

	// Extract column index
	MOV R4, #ROW_MASK	// Load mask to extract columns
	AND R3, R1, R4		// Extract columns


	CMP R2, #0b0001		// Compare to row 1 case
	BEQ firstRow

	CMP R2, #0b0010		// Compare to row 2 case
	BEQ secondRow

	CMP R2, #0b0100		// Compare to row 3 case
	BEQ thirdRow

	CMP R2, #0b1000		// Compare to row 4 case
	BEQ fourthRow

	// Default case, return a null char
	B 2f

	firstRow:
		// Row 1, Col 1
		// AKA "1"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'1'
			BEQ 1f

		// Row 1, Col 2
		// AKA "2"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'2'
			BEQ 1f

		// Row 1, Col 3
		// AKA "3"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'3'
			BEQ 1f

		// Row 1, Col 4
		// AKA "A"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'A'
			BEQ 1f

		B 2f

	secondRow:
		// Row 2, Col 1
		// AKA "4"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'4'
			BEQ 1f

		// Row 2, Col 2
		// AKA "5"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'5'
			BEQ 1f

		// Row 2, Col 3
		// AKA "6"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'6'
			BEQ 1f

		// Row 2, Col 4
		// AKA "B"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'B'
			BEQ 1f

		B 2f

	thirdRow:
		// Row 3, Col 1
		// AKA "7"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'7'
			BEQ 1f

		// Row 3, Col 2
		// AKA "8"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'8'
			BEQ 1f

		// Row 3, Col 3
		// AKA "9"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'9'
			BEQ 1f

		// Row 3, Col 4
		// AKA "C"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'C'
			BEQ 1f

		B 2f

	fourthRow:
		// Row 4, Col 1
		// AKA "*"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'*'
			BEQ 1f

		// Row 4, Col 2
		// AKA "0"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'0'
			BEQ 1f

		// Row 4, Col 3
		// AKA "#"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'#'
			BEQ 1f

		// Row 4, Col 4
		// AKA "D"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'D'
			BEQ 1f

	2:
	MOV R0, #0

	1:
	POP {R1-R4, PC}


// 	Function: Key_ToHexChar
//	Register-safe!
//	Description:
//		Converts a provided one-hot keycode into
//		an ASCII character
//
//		Keycode format: xxxx_yyyy
//		where the x's represent one-hot encoded row index
//		where the y's represent one-hot encoded col index
//	Args:
//		R1	-	One-hot encoded keycode
//	Returns:
//		R0	-	ASCII code
//	Register Use:
//		R0	-	Return
//		R1	-	Argument
//		R2	-	Row index
//		R3	-	Column index
//		R4	-	Masks
key_ToHexChar:
	PUSH {R1-R4, LR}

	// Extract row index
	MOV R4, #COL_MASK	// Load mask to extract rows
	AND R2, R1, R4		// Extract rows
	LSR R2, R2, #4		// Shift

	// Extract column index
	MOV R4, #ROW_MASK	// Load mask to extract columns
	AND R3, R1, R4		// Extract columns


	CMP R2, #0b0001		// Compare to row 1 case
	BEQ firstRowHex

	CMP R2, #0b0010		// Compare to row 2 case
	BEQ secondRowHex

	CMP R2, #0b0100		// Compare to row 3 case
	BEQ thirdRowHex

	CMP R2, #0b1000		// Compare to row 4 case
	BEQ fourthRowHex

	// Default case, return a null char
	B 2f

	firstRowHex:
		// Row 1, Col 1
		// AKA "1"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'1'
			BEQ 1f

		// Row 1, Col 2
		// AKA "2"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'2'
			BEQ 1f

		// Row 1, Col 3
		// AKA "3"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'3'
			BEQ 1f

		// Row 1, Col 4
		// AKA "A"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'A'
			BEQ 1f

		B 2f

	secondRowHex:
		// Row 2, Col 1
		// AKA "4"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'4'
			BEQ 1f

		// Row 2, Col 2
		// AKA "5"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'5'
			BEQ 1f

		// Row 2, Col 3
		// AKA "6"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'6'
			BEQ 1f

		// Row 2, Col 4
		// AKA "B"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'B'
			BEQ 1f

		B 2f

	thirdRowHex:
		// Row 3, Col 1
		// AKA "7"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'7'
			BEQ 1f

		// Row 3, Col 2
		// AKA "8"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'8'
			BEQ 1f

		// Row 3, Col 3
		// AKA "9"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'9'
			BEQ 1f

		// Row 3, Col 4
		// AKA "C"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'C'
			BEQ 1f

		B 2f

	fourthRowHex:
		// Row 4, Col 1
		// AKA "*"
		CMP R3, #0b0001
		ITT EQ
			MOVEQ R0, #'F'
			BEQ 1f

		// Row 4, Col 2
		// AKA "0"
		CMP R3, #0b0010
		ITT EQ
			MOVEQ R0, #'0'
			BEQ 1f

		// Row 4, Col 3
		// AKA "#"
		CMP R3, #0b0100
		ITT EQ
			MOVEQ R0, #'E'
			BEQ 1f

		// Row 4, Col 4
		// AKA "D"
		CMP R3, #0b1000
		ITT EQ
			MOVEQ R0, #'D'
			BEQ 1f

	2:
	MOV R0, #0

	1:
	POP {R1-R4, PC}


key_ToNum:
	PUSH {LR}

	CMP R1, #'0'
	ITT EQ
		MOVEQ R0, #0
		BEQ 1f

	CMP R1, #'1'
	ITT EQ
		MOVEQ R0, #1
		BEQ 1f

	CMP R1, #'2'
	ITT EQ
		MOVEQ R0, #2
		BEQ 1f

	CMP R1, #'3'
	ITT EQ
		MOVEQ R0, #3
		BEQ 1f

	CMP R1, #'4'
	ITT EQ
		MOVEQ R0, #4
		BEQ 1f

	CMP R1, #'5'
	ITT EQ
		MOVEQ R0, #5
		BEQ 1f

	CMP R1, #'6'
	ITT EQ
		MOVEQ R0, #6
		BEQ 1f

	CMP R1, #'7'
	ITT EQ
		MOVEQ R0, #7
		BEQ 1f

	CMP R1, #'8'
	ITT EQ
		MOVEQ R0, #8
		BEQ 1f

	CMP R1, #'9'
	ITT EQ
		MOVEQ R0, #9
		BEQ 1f

	CMP R1, #'A'
	ITT EQ
		MOVEQ R0, #10
		BEQ 1f

	CMP R1, #'B'
	ITT EQ
		MOVEQ R0, #11
		BEQ 1f

	CMP R1, #'C'
	ITT EQ
		MOVEQ R0, #12
		BEQ 1f

	CMP R1, #'D'
	ITT EQ
		MOVEQ R0, #13
		BEQ 1f

	CMP R1, #'E'
	ITT EQ
		MOVEQ R0, #14
		BEQ 1f

	CMP R1, #'F'
	ITT EQ
		MOVEQ R0, #15
		BEQ 1f

	// Default case
	MOV R0, #16

	1:
	POP {PC}



.global EXTI4_IRQHandler
.thumb_func
EXTI4_IRQHandler:
	// Disable external interrupts
	LDR R1, =EXTI_BASE
	MOV R3, #0
	LDR R2, [R1, #EXTI_IMR]
	BFI R2, R3, #4, #4
	STR R2, [R1, #EXTI_IMR]

	// Clear pending
	LDR R1, =EXTI_BASE
	MOV R2, #1<<4
	STR R2, [R1, #EXTI_PR]

	PUSH {LR}

	MOV R1, #DEBOUNCE
	BL delay_ms

	// Actual code goes here
	// At this point, we know this is in row 0
	// so we can just scan row 0 to see which column
	// is active

	// Switch PC4 from input to output
	LDR R1, =GPIOC_BASE			// GPIO base addr
	MOV R3, #PIN_OUTPUT			// Output mask
	LDR R2, [R1, #GPIO_MODER]	// Read
	BFI R2, R3, #(4*2), #2		// Set PC4 as output
	STR R2, [R1, #GPIO_MODER]	// Write

	// Switch PC0-PC3 (columns) to inputs
	MOV R3, #PIN_INPUT
	LDR R2, [R1, #GPIO_MODER]	// Read
	BFI R2, R3, #0, #2			// Apply mask to PC0
	BFI R2, R3, #(1*2), #2		// Apply mask to PC1
	BFI R2, R3, #(2*2), #2		// Apply mask to PC2
	BFI R2, R3, #(3*2), #2		// Apply mask to PC3
	STR R2, [R1, #GPIO_MODER]	// Write

	// Broadcast a '1' onto the first row
	MOV R3, #1					// '1'
	LDR R2, [R1, #GPIO_ODR]		// Read
	BFI R2, R3, #4, #1			// Insert onto PC4
	STR R2, [R1, #GPIO_ODR]		// Write

	MOV R1, #5
	BL delay_us

	// Scan inputs to determine which key is active
	LDR R1, =GPIOC_BASE
	MOV R3, #0b00010000			// Prepare a mask
	LDR R2, [R1, #GPIO_IDR]		// Read the IDR

	// Mask off all but column values
	AND R2, #0xF

	// Insert into row 4 key mask
	ORR R3, R2

	// Write the key code into memory
	LDR R1, =button
	STRB R3, [R1]		// R3 should only contain a byte

	// Write a non-zero value to the flag
	// in memory indicating the interrupt occured
	LDR R1, =press
	MOV R2, #1
	STRB R2, [R1]

	// Reset GPIOC to default config
	LDR R1, =GPIOC_BASE			// Load GPIOC base address
	LDR R2, [R1, #GPIO_MODER]	// Read from the current mode register
	MOV R3, #ROW_INPUT			// Load mask to set rows as input
	BFI R2, R3, #0, #16			// Insert mask where PC0-PC7 live
	STR R2, [R1, #GPIO_MODER]	// Write back to the mode register

	// Re-enable external interrupts
	LDR R1, =EXTI_BASE
	MOV R3, #0xF
	LDR R2, [R1, #EXTI_IMR]
	BFI R2, R3, #4, #4
	STR R2, [R1, #EXTI_IMR]

	POP {LR}
	BX LR

.global EXTI9_5_IRQHandler
.thumb_func
EXTI9_5_IRQHandler:
	// Disable external interrupts
	LDR R1, =EXTI_BASE
	MOV R3, #0
	LDR R2, [R1, #EXTI_IMR]
	BFI R2, R3, #4, #4
	STR R2, [R1, #EXTI_IMR]

	// Load pending to decode
	LDR R4, [R1, #EXTI_PR]

	// Clear pending
	LDR R1, =EXTI_BASE
	MOV R2, #0xE0
	STR R2, [R1, #EXTI_PR]

	PUSH {LR}

	MOV R1, #DEBOUNCE
	BL delay_ms

	// Prepare row index
	LSR R4, #4				// Move row index to the first 4 bits

	// Switch rows to be outputs and columns to inputs
	LDR R1, =GPIOC_BASE			// Load GPIOC base address
	LDR R2, [R1, #GPIO_MODER]	// Read from the current mode register
	MOV R3, #COL_INPUT			// Load mask to set rows as input
	BFI R2, R3, #0, #16			// Insert mask where PC0-PC7 live
	STR R2, [R1, #GPIO_MODER]	// Write back to the mode register

	// Broadcast row pattern
	LDR R2, [R1, #GPIO_ODR]		// Read
	BFI R2, R4, #4, #4			// Insert pattern from PR onto row outputs
	STR R2, [R1, #GPIO_ODR]		// Write

	// Allow charge to propagate
	MOV R1, #5
	BL delay_us

	// Scan column inputs
	LDR R3, =0xFF				// Mask to allow our keycode
	LDR R1, =GPIOC_BASE			// GPIO base addr
	LDR R2, [R1, #GPIO_IDR]		// Read IDR
	AND R2, R3					// Apply mask

	// Now, our keycode should hopefully be in R2

	// Write the key code into memory
	LDR R1, =button
	STRB R2, [R1]		// R2 should only contain a byte

	// Write a non-zero value to the flag
	// in memory indicating the interrupt occured
	LDR R1, =press
	MOV R2, #1
	STRB R2, [R1]

	// Reset GPIOC to default config
	LDR R1, =GPIOC_BASE			// Load GPIOC base address
	LDR R2, [R1, #GPIO_MODER]	// Read from the current mode register
	MOV R3, #ROW_INPUT			// Load mask to set rows as input
	BFI R2, R3, #0, #16			// Insert mask where PC0-PC7 live
	STR R2, [R1, #GPIO_MODER]	// Write back to the mode register

	// Re-enable external interrupts
	LDR R1, =EXTI_BASE
	MOV R3, #0xF
	LDR R2, [R1, #EXTI_IMR]
	BFI R2, R3, #4, #4
	STR R2, [R1, #EXTI_IMR]

	POP {LR}
	BX LR


.section .data
// Reserved for the button that was pressed
.global button
button:	.byte	0

// Indicates when a key was pressed
.global press
press: 	.byte	0


