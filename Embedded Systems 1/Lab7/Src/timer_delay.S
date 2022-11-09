//	Evan Heinrich
//	CE2801 sect. 011
//	9/28/2021
//
//	File:
//		timer_delay.S
//	Description of File:
//		Originally created 9/28/2021 for Lab 3
//		Modified 10/19/2021 for Lab 6, conversion to using
//		our board's dedicated timers
//	(opt) Dependancies:
//		N/A

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

// Literal Pool
.equ TIM2_BASE,		0x40000000	// Timer 2 base address
.equ RCC_BASE, 		0x40023800	// RCC base address

.equ APB1ENR,		0x40		// Offset from RCC base to APB1ENR
.equ TIM_CR1,		0x00		// Offset from TIMx base to control reg. 1
.equ TIM_ARR,		0x2C		// Offset from TIMx base to auto reload register
.equ TIM_PSC,		0x28		// Offset from TIMx base to prescale register
.equ TIM_CNT,		0x24		// Offset from TIMx base to count register

.equ TIM2EN,		1 << 0		// Location of TIM2 enabler is bit 0
.equ OPM_SET,		1 << 3		// Mask to set one pulse mode (do not repeat)
.equ CLK_DIV,		16			// Mask to set clock division to 1MHz
.equ CNT_DN,		1 << 4		// Mask to set count down mode
.equ CNTEN_MASK,	1 << 0		// Mask for the location of counter enable
.equ CNT_MS, 		1000		// 1k counts per millisecond w/ 1MHz count rate
.equ CNT_US,		1			// 1 count per microsecond w/ 1MHz count rate
.equ CNT_S,			1000000		// 1M counts per second w/ 1MHz count rate


// Globally exposed functions
.global delay_Init
.global delay_ms
.global delay_us
.global delay_sec


// 	Function: delay_Init
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Configures TIM2 as a simple countdown timer.
//		->	Uses a 16x clock division, making the count rate 1MHz
//		-> 	TIM2 is a 32-bit counter, allowing for a large range of time
//	Args:
//		Void
//	Returns:
//		Void
//	Register Usage:
//		R1 	- 	Addresses
//		R2	-	Scratch
delay_Init:
	PUSH {R1-R2, LR}

	// Enable TIM2
	LDR R1, =RCC_BASE		// Load RCC base address
	LDR R2, [R1, #APB1ENR]	// Read
	ORR R2, #TIM2EN			// Apply Timer 2 enable mask
	STR R2, [R1, #APB1ENR]	// Write

	// Set timer configurations
	LDR R1, =TIM2_BASE		// Load Timer 2 base address
	LDR R2, [R1, #TIM_CR1]	// Read
	ORR R2, #OPM_SET		// Apply one pulse mode config
	ORR R2, #CNT_DN			// Apply countdown config
	STR R2, [R1, #TIM_CR1]	// Write

	// Set prescaler
	MOV R2, #CLK_DIV		// Load desired clock division
	STR R2, [R1, #TIM_PSC]	// Apply desired clock division

    POP {R1-R2, PC}


// 	Function: delay_ms
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Starts a timer for a duration provided in the argument
//		->	Conversion factor is 1,000 so the max value here is
//			4,294,967 and some change.
//	Args:
//		R1	-	Desired timer duration in milliseconds
//	Returns:
//		Void
//	Register Usage:
//		R0	-	Total counts for provided delay
//		R1 	- 	Argument and Addresses
//		R2	-	Scratch
delay_ms:
	PUSH {R0-R2, LR}

	// Convert the argument in milliseconds to counts
	LDR R2, =CNT_MS		// Load the conversion factor
	MUL R0, R1, R2		// Convert milliseconds to counts

	// Store desired count
	LDR R1, =TIM2_BASE		// Load timer base address
	STR R0, [R1, #TIM_CNT]	// Overwrite counter

	// Start count
	LDR R2, [R1, #TIM_CR1]	// Load the current control register
	ORR R2, #CNTEN_MASK		// Apply mask to enable counter
	STR R2, [R1, #TIM_CR1]	// Write and start count

	// Poll counter until count expires (counter enable = 0)
	1:
	LDR R2, [R1, #TIM_CR1]	// Read control register
	BFC R2, #1, #31			// Clear everything except CEN bit
	CMP R2, #0				// Compare to 0, aka counter expired
	BNE 1b					// Loop if not zero

	POP {R0-R2, PC}


// 	Function: delay_us
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Starts a timer for a duration provided in the argument
//		->	Conversion factor is 1 so the max value here is
//			4,294,967,295
//	Args:
//		R1	-	Desired timer duration in microseconds
//	Returns:
//		Void
//	Register Usage:
//		R0	-	Total counts for provided delay
//		R1 	- 	Argument and Addresses
//		R2	-	Scratch
delay_us:
	PUSH {R0-R2, LR}

	// Convert the argument in microseconds to counts
	LDR R2, =CNT_US		// Load the conversion factor (technically 1x but still)
	MUL R0, R1, R2		// Convert microseconds to counts

	// Store desired count
	LDR R1, =TIM2_BASE		// Load timer base address
	STR R0, [R1, #TIM_CNT]	// Overwrite counter

	// Start count
	LDR R2, [R1, #TIM_CR1]	// Load the current control register
	ORR R2, #CNTEN_MASK		// Apply mask to enable counter
	STR R2, [R1, #TIM_CR1]	// Write and start count

	// Poll counter until count expires (counter enable = 0)
	1:
	LDR R2, [R1, #TIM_CR1]	// Read control register
	BFC R2, #1, #31			// Clear everything except CEN bit
	CMP R2, #0				// Compare to 0, aka counter expired
	BNE 1b					// Loop if not zero

	POP {R0-R2, PC}


// 	Function: delay_sec
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Starts a timer for a duration provided in the argument
//		->	Conversion factor is 1M so the max value here is
//			4,294 and some change.
//	Args:
//		R1	-	Desired timer duration in seconds
//	Returns:
//		Void
//	Register Usage:
//		R0	-	Total counts for provided delay
//		R1 	- 	Argument and Addresses
//		R2	-	Scratch
delay_sec:
	PUSH {R0-R2, LR}

	// Convert the argument in seconds to counts
	LDR R2, =CNT_S		// Load the conversion factor
	MUL R0, R1, R2		// Convert seconds to counts

	// Store desired count
	LDR R1, =TIM2_BASE		// Load timer base address
	STR R0, [R1, #TIM_CNT]	// Overwrite counter

	// Start count
	LDR R2, [R1, #TIM_CR1]	// Load the current control register
	ORR R2, #CNTEN_MASK		// Apply mask to enable counter
	STR R2, [R1, #TIM_CR1]	// Write and start count

	// Poll counter until count expires (counter enable = 0)
	1:
	LDR R2, [R1, #TIM_CR1]	// Read control register
	BFC R2, #1, #31			// Clear everything except CEN bit
	CMP R2, #0				// Compare to 0, aka counter expired
	BNE 1b					// Loop if not zero

	POP {R0-R2, PC}
