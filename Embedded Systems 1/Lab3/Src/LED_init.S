//	Evan Heinrich
//	CE2801 sect. 011
//	9/28/2021
//
//	File:
//		LED_init.S
//	Description of File:
//		Lab 3, LED initialization used in future labs
//	(opt) Dependancies:
//		N/A

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

// Constants
.equ RCC_BASE, 0x40023800
.equ RCC_AHB1ENR, 0x30 		// Offset from RCC_BASE
.equ GPIOBEN, 1<<1 			// GPIOBEN lives on bit 1 of AHB1ENR so shift a 1 left by 1

.equ GPIOB_BASE, 0x40020400
.equ GPIOB_MODER, 0x00 		// Offset from GPIOB_BASE
.equ GPIOB_ODR, 0x14		// Offset from GPIOB_BASE

.global num_to_LED_init

// 	Function: num_to_LED_init
//	Register-safe! Pushes all general purpose registers (R0-R12) & LR to the stack
//	Description:
//		Enables RCC for GPIOB
//		Sets GPIOB_MODER for PB5-10 & PB12-15 as outputs
//	Args:
//		N/A
//	Returns:
//		Void
num_to_LED_init:
	PUSH {R0-R12, LR}			// Backup registers

	// Enable RCC For GPIOB
	LDR R1, =RCC_BASE			// Load the RCC base address
	LDR R2, [R1, #RCC_AHB1ENR]	// Load what is currently stored in the AHB1 Enabler
	ORR R2, R2, #GPIOBEN		// Apply the mask to enable GPIOB
	STR R2, [R1, #RCC_AHB1ENR]	// Write back the new AHB1 Enabler value

	// Set GPIOB Pins as Output
	LDR R1, =GPIOB_BASE 		// Load base address
	LDR R2, [R1, #GPIOB_MODER]	// Load the GPIOB mode status
	LDR R3, =0xFF3FFC00			// Load the output clearing mask
	BIC R2, R2, R3				// Clear the modes
	LDR R3, =0x55155400			// Load the output setting mask
	ORR R2, R2, R3				// Overwrite with output set mask
	STR R2, [R1, #GPIOB_MODER]	// Write back to memory

	POP {R0-R12, LR}			// Restore registers
	BX LR						// Return from subroutine
