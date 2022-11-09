//	Evan Heinrich
//	CE2801 sect. 011
//	9/28/2021
//
//	File:
//		tone.S
//	Description of File:
//		Initially will just hold an example success and failure tone
//		and the code to make the MSOE development board piezo buzzer
//		emit those tones
//	(opt) Dependencies:
//		timer_delay.S

.syntax unified
.cpu cortex-m4
.thumb
.section .text

.global tone_Init
.global tone_Success
.global tone_Failure
.global tone_Notify

// Base addresses
.equ RCC_BASE, 		0x40023800
.equ GPIOB_BASE,	0x40020400
.equ TIM3_BASE,		0x40000400

// Offsets
.equ AHB1ENR, 		0x30		// AHB1ENR used to enable GPIO ports
.equ APB1ENR,		0x40		// APB1ENR used to enable timers
.equ GPIO_MODER,	0			// Offset from GPIOx base addr to mode register
.equ GPIO_AFRL,		0x20		// Offset from GPIOx base to alt. funct. register (low)
.equ TIM_CR1,		0x00		// Offset from TIMx base to control reg. 1
.equ TIM_ARR,		0x2C		// Offset from TIMx base to auto reload register
.equ TIM_PSC,		0x28		// Offset from TIMx base to prescale register
.equ TIM_CCMR1,		0x18		// Offset from TIMx base to capture/compare mode reg
.equ TIM_CCR,		0x34		// Offset from TIMx base to capture/compare register
.equ TIM_CCER,		0x20		// Offset from TIMx base to capture compare enable reg

// Masks
.equ GPIOBEN,		1 << 1		// Location of the GPIOB enable bit
.equ TIM3EN,		1 << 1		// Location of the TIM3 enable bit
.equ GPIO_ALTFUN,	0b10		// Mask to set a GPIO pin as alternate function
.equ PB4_ALTFUN,	0b0010		// Mask for AFRL to set PB4 as TIM3_CH1
.equ PIN_TOGGLE,	0b011		// Mask to set pin output mode to toggle

// Constants
.equ PRESCALE,		16			// Used to prescale clock from 16MHz to 1MHz
.equ NOTE_C5,		1911		// Pulses of a 1MHz clock to make a C5 note
.equ NOTE_G5,		1276		// Pulses of a 1MHz clock to make a G6 note
.equ NOTE_LEN,		200			// Duration of each note, MS


// 	Function: Tone_Setup
//	Register-safe! Pushes used registers to stack
//	Description:
//		Configures GPIO and timers for use with the MSOE devboard piezo buzzer
//		->	Piezo buzzer lives on PB4, and one of PB4's alternate functions is
//			TIM3_CH1
//	Args:
//		N/A
//	Returns:
//		N/A
//	Register Use:
//		R0	-	Scratch
//		R1	-	Addresses
//		R2	-	Scratch
//		R3	-	Scratch
tone_Init:
	PUSH {R0-R3, LR}

	// Enable GPIOB
	LDR R1, =RCC_BASE		// Load RCC base addr
	LDR R2, [R1, #AHB1ENR]	// Read from the AHB1ENR
	ORR R2, #GPIOBEN		// Apply GPIOB enable mask
	STR R2, [R1, #AHB1ENR]	// Write back to AHB1ENR

	// Enable TIM3 (enabler also lives in RCC)
	LDR R2, [R1, #APB1ENR]	// Read from the APB1 enable register
	ORR R2, #TIM3EN			// Apply timer 3 enable mask
	STR R2, [R1, #APB1ENR]	// Write back to APB1ENR

	// Set PB4 as alternate funct
	LDR R1, =GPIOB_BASE				// Load GPIOB base address
	LDR R2, [R1, #GPIO_MODER]		// Read the mode register
	ORR R2, #GPIO_ALTFUN << (4 * 2)	// Apply the 2 bit mask to PB4
	STR R2, [R1, #GPIO_MODER]		// Write

	// Set alternate function register for PB4
	// PB4 AFR is AFRL [19..16]
	// TIM3_CH1 is alternate function 2
	LDR R2, [R1, #GPIO_AFRL]	// Read current AFRL
	MOV R0, #PB4_ALTFUN			// Load mask for BFI
	BFI R2, R0, #16, #4			// Insert the alt. funct. code into AFRL4
	STR R2, [R1, #GPIO_AFRL]	// Write

	// Update timer prescaler
	LDR R1, =TIM3_BASE
	MOV R2, #PRESCALE
	STR R2, [R1, #TIM_PSC]

	// Configure capture/compare mode register (CCMR)
	// Set output mode to toggle
	// Disable preload
	LDR R1, =TIM3_BASE			// Load Timer 3 base addr
	LDR R2, [R1, #TIM_CCMR1]	// Read from the CCMR
	MOV R3, #PIN_TOGGLE			// Load toggle output mode
	BFI R2, R3, #4, #3			// Insert toggle command
	BFC	R2, #3, #1				// Clear (disable) preload
	STR R2, [R1, #TIM_CCMR1]

	// Set CC1E (capture compare ch1 enable)
	// Set CC1P (capture compare ch1 polarity)
	LDR R2, [R1, #TIM_CCER]	// Read
	ORR R2, #0b11 << 0		// CC1E & CC1P live at CCER[1..0]
	STR R2, [R1, #TIM_CCER]	// Write

	POP {R0-R3, PC}


// 	Function: Tone_Success
//	Register-safe! Pushes used registers to stack
//	Description:
//		Uses TIM3_CH1 to play a success tone on the piezo buzzer
//		C5 -> G5
//	Args:
//		N/A
//	Returns:
//		N/A
//	Register Use:
//		R1	-	Delay arguments
//		R2	-	Scratch
//		R3	-	Address
tone_Success:
	PUSH {R1-R3, LR}

	// Load base address
	LDR R3, =TIM3_BASE


	// Write first frequency
	MOV R2, #NOTE_C5
	STR R2, [R3, #TIM_ARR]
	STR R2, [R3, #TIM_CCR]

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	// Write second frequency
	MOV R2, #NOTE_G5
	STR R2, [R3, #TIM_ARR]
	STR R2, [R3, #TIM_CCR]

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	POP {R1-R3, PC}


// 	Function: Tone_Failure
//	Register-safe! Pushes used registers to stack
//	Description:
//		Uses TIM3_CH1 to play a failure tone on the piezo buzzer
//		G5 -> C5
//	Args:
//		N/A
//	Returns:
//		N/A
//	Register Use:
//		R1	-	Delay arguments
//		R2	-	Scratch
//		R3	-	Address
tone_Failure:
	PUSH {R1-R3, LR}

	// Load base address
	LDR R3, =TIM3_BASE

	// Write first frequency
	MOV R2, #NOTE_G5
	STR R2, [R3, #TIM_ARR]
	STR R2, [R3, #TIM_CCR]

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	// Write second frequency
	MOV R2, #NOTE_C5
	STR R2, [R3, #TIM_ARR]
	STR R2, [R3, #TIM_CCR]

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	POP {R1-R3, PC}

tone_Notify:
	PUSH {R1-R3, LR}

	// Load base address
	LDR R3, =TIM3_BASE

	// Write first frequency
	MOV R2, #NOTE_G5
	STR R2, [R3, #TIM_ARR]
	STR R2, [R3, #TIM_CCR]

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN/2
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	// Rest for the desired length
	MOV R1, #NOTE_LEN/4
	BL delay_ms

	// Turn on clock
	LDR R2, [R3, #TIM_CR1]
	ORR R2, #1
	STR R2, [R3, #TIM_CR1]

	// Play note for the desired length
	MOV R1, #NOTE_LEN/2
	BL delay_ms

	// Turn off clock
	LDR R2, [R3, #TIM_CR1]
	BFC R2, #0, #1
	STR R2, [R3, #TIM_CR1]

	POP {R1-R3, PC}


// Frequencies used (assuming A4 = 440Hz)
//	1. C5 (523.25Hz)
//	2. G5 (783.99Hz)
//
//	C5 -> G5 for success
//	G5 -> C5 for failure
