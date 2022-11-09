//	Evan Heinrich
//	CE2801 sect. 011
//	10/2/2021
//
//	File:
//		test.S
//	Description of File:
//		Lab 3, Driver to test all methods
//	(opt) Dependancies:
//		delay.S
//		LED.S
//		ASCII.S

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

.global main

.equ MAX_LOOP, 0x400	// Loop if less than 1024
.equ DELAY_500, 0x1F4	// 500ms delay
.equ DELAY_LONG, 0x1388	// long delay
.equ ASCII, 0x2694		// Number to be converted to ASCII
main:
	BL LED_init			// Initialize the LEDs

ascii:
	LDR R1, =ASCII		// Load the number being converted
	BL num_to_ASCII		// Convert the number to ASCII
1:
	LDR R1, =DELAY_500	// Prepare the 500ms delay between digits
	BL delay_ms			// Execute the delay between digits
	CMP R0, #0			// Determine if the digits register is empty
	MOV R1, #0			// Empty the register holding the current digit
	ITTT NE				// If the digits register is not empty
		BFINE R1, R0, #0, #8	// Insert the next ASCII value into the current digit
		LSRNE R0, R0, #8		// Shift the register holding all digits to the next digit
		BLNE num_to_LED			// Display the current digit
	IT NE				// Another conditional block
		BNE 1b			// Branch back to the beginning if the digits arent empty
	LDR R1, =DELAY_LONG	// Load the long delay
	BL delay_ms			// Execute the long delay
	B ascii				// Start process over again
