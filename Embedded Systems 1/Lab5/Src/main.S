//	Evan Heinrich
//	CE2801 sect. 011
//	10/12/2021
//
//	File:
//		main.S
//	Description of File:
//		Lab 5 driver program
//	(opt) Dependencies:
//		delay.S
//		LCD_Control.S

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

.global main

main:
	BL LCD_Init				// Initialize display

	BL Key_Init				// Initialize keypad

	MOV R6, #0				// Initialize line counter
	MOV R7, #0				// Initialize char counter
1:
	BL Key_GetChar			// Get the key being pressed
	MOV R1, R0				// Move it into an argument register
	BL LCD_PrintChar		// Print the character
	ADD R7, R7, #1			// Increment the char counter
	CMP R7, #16				// If there are 16 characters
	BEQ newLine				// Move to a new line
	B 1b					// Otherwise loop

newLine:
	CMP R6, #1				// Determine if we are on line 0 or 1
	ITTT NE					// If we are on the first row
		MOVNE R0, #1		// Second row index
		MOVNE R1, #0		// First column inxed
		BLNE LCD_MoveCursor	// Move the cursor

	CMP R6, #1				// Redo comparison just to be safe
	ITT NE					// Again if we are on the first row
		MOVNE R6, #1		// Update row counter
		MOVNE R7, #0		// Reset char counter
		BNE 1b				// Jump back to loop

	CMP R6, #1				// Again, redo the comparison
	ITTT EQ					// If we are on the second row
		MOVEQ R6, #0		// Update row counter
		MOVEQ R7, #0		// Update char counter
		BLEQ LCD_Home		// Home the cursor
	B 1b					// Return to loop
