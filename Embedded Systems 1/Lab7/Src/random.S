//	Evan Heinrich
//	CE2801 sect. 011
//	10/29/2021
//
//	File:
//		random.S
//	Description of File:
//		Configures a timer to use as a random
//		number generator
//		->	Uses a 16-bit timer, TIM3
//	(opt) Dependencies:
//		N/A

// Assembler directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

.equ TIM3_BASE,		0x40000400	// TIM2 base address
.equ RCC_BASE, 		0x40023800	// RCC base address

.equ APB1ENR,		0x40		// Offset from RCC base to APB1ENR
.equ TIM3EN,		1 << 1		// Location of TIM3 enabler is bit 1

.equ TIM_CR1,		0x00		// Offset from TIM base to control reg.
.equ TIM_CNT,		0x24		// Offset from TIM base to count reg.
.equ TIM_CEN,		1 << 0		// Location for count enable



.global rnd_Init
.global rnd_Sample4

// 	Function: rnd_Init
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Configures TIM3 as a pseudo-random number generator
//		Literally just starts TIM3 upcounting constantly
//	Args:
//		Void
//	Returns:
//		Void
//	Register Usage:
//		R1 	- 	Addresses
//		R2	-	Scratch
rnd_Init:
	PUSH {R1, R2, LR}

	// Enable TIM3 in RCC
	LDR R1, =RCC_BASE
	LDR R2, [R1, #APB1ENR]
	ORR R2, #TIM3EN
	STR R2, [R1, #APB1ENR]

	// Start TIM3 counting
	LDR R1, =TIM3_BASE
	LDR R2, [R1, #TIM_CR1]
	ORR R2, #TIM_CEN
	STR R2, [R1, #TIM_CR1]

	POP {R1, R2, PC}

// 	Function: rnd_Sample4
//	Register-safe! Pushes all used registers to the stack
//	Description:
//		Samples the random number generator and
//		returns the lower 4 bits of the sample
//	Args:
//		Void
//	Returns:
//		R0	-	Pseudo-random number
//	Register Usage:
//		R0	-	Return
//		R1 	- 	Addresses
//		R2	-	Scratch
rnd_Sample4:
	PUSH {R1, R2, LR}

	// Sample counter
	LDR R1, =TIM3_BASE
	LDR R2, [R1, #TIM_CNT]

	// Mask off all but lower 4 bits
	MOV R1, #0xF
	AND R0, R1, R2

	POP {R1, R2, PC}
