//	Evan Heinrich
//	CE2801 sect. 011
//	10/26/2021
//
//	File:
//		main.S
//	Description of File:
//		Main program of Lab 7
//		Hexadecimal conversion game
//	(opt) Dependencies:
//		timer_delay.S
//		LCD_Control.S
//		keypad.S
//		ASCII.S
//		LED.S
//		tone.S

// TODO
//	Verify functionality

// Assembler Directives
.syntax unified
.cpu cortex-m4
.thumb
.section .text

.equ MSG_DELAY,		3			// Time in seconds to display each message
.equ RESET_TIME,	2			// Delay between showing time and restarting
.equ MAX_LOOPS,		4			// Total number of conversion loops

.equ TIM5_BASE,		0x40000C00	// Timer 5 base address
.equ RCC_BASE, 		0x40023800	// RCC base address

.equ APB1ENR,		0x40		// Offset from RCC base to APB1ENR
.equ TIM5EN,		1 << 3		// Location in APB1ENR of TIM5EN

.equ TIM_CR1,		0x00		// Offset from TIMx base to control reg. 1
.equ TIM_PSC,		0x28		// Offset from TIMx base to prescale register
.equ TIM_CNT,		0x24		// Offset from TIMx base to count register
.equ TIM_EGR, 		0x14
.equ TIM_SR, 		0x10

.equ TIM_CEN,		1 << 0		// Location of CEN in CR1
.equ TIM_UG,		1 << 0
.equ TIM_UIF, 		1 << 0


.global main

main:
	BL delay_Init
	BL LCD_Init
	BL key_Init
	BL rnd_Init
	BL LED_Init
	BL tone_Init

	// Configure conversion timer
	LDR R1, =RCC_BASE		// RCC base address
	LDR R2, [R1, #APB1ENR]	// Read
	ORR R2, #TIM5EN			// Enable TIM5
	STR R2, [R1, #APB1ENR]	// Write

	LDR R1, =TIM5_BASE		// Timer 5 base address
	LDR R2, =16000-1		// 16MHz / 16kHz = 1kHz aka 1ms
	STR R2, [R1, #TIM_PSC]	// Apply prescale to 1ms per count

	// This is the prescaler fix Dr. Livingston provided
	// From what I understand, it forces an update event
	// on the timer, which somehow forces the prescale into effect.
	MOV R2, #TIM_UG
	LDR R1, =TIM5_BASE
	STR R2, [r1, #TIM_EGR]

	LDR R1, =TIM5_BASE
	LDR R2, [R1, #TIM_SR]
	BIC R2, #TIM_UIF
	STR R2, [R1, #TIM_SR]

	// DO NOT start timer counting yet

	BL FirstText

	// Check the memory address that reports if
	// the keypad interrupt was triggered
	loop:
		LDR R1, =press
		LDRB R2, [R1]
		CMP R2, #0
		IT NE
		BLNE onPress
		B loop

// Code to be executed when a button is pressed on the keypad
// R0 = returns/args
// R1 = Addr/Arg
// R2 = Scratch
// R3 = Conversion time
// R4 = Random number
onPress:
	PUSH {R0, R1, R2, LR}

	// Stop conversion timer
	BL StopTimer

	// Get keycode
	LDR R1, =button
	LDRB R2, [R1]

	// Convert keycode to char
	MOV R1, R2
	BL key_ToHexChar

	// Print char to screen
	// Even with debouncing, occasionally a
	// null char makes it through to here
	// so this just filters out nulls
	MOV R1, R0
	CMP R1, #0
	IT NE
	BLNE LCD_PrintChar

	// Convert to a number with nulls filtered out
	CMP R1, #0
	IT NE
	BLNE key_ToNum

	// Compare entered number to RNG number
	LDR R1, =random		// random number address
	LDR R4, [R1]		// load random number
	CMP R0, R4			// compare key to random number

	// Display "correct" or "incorrect"
	IT EQ
		BLEQ DisplayCorrect

	// Redo Comparison
	LDR R1, =random		// random number address
	LDR R4, [R1]		// load random number
	CMP R0, R4			// compare key to random number

	IT NE
		BLNE DisplayIncorrect

	MOV R1, #MSG_DELAY
	BL delay_sec

	BL LCD_Clear

	// Display time it took to convert
	BL DisplayTime

	BL ResetTimer

	// Reset interrupt status
	LDR R1, =press
	MOV R2, #0
	STRB R2, [R1]

	MOV R1, #RESET_TIME
	BL delay_sec

	// Load loop iterator
	LDR R1, =loops
	LDRB R2, [R1]

	// Compare to maximum loops
	MOV R3, #MAX_LOOPS
	CMP R2, R3

	// Increment iterator if less than max loops
	// and store new value.
	// Loop program if less than, show smallest time otherwise
	ITTE LT
		ADDLT R2, #1
		STRBLT R2, [R1]
		BGE DisplayMinTime	// Go to end of progam

	// Soft Restart
	BL SoftRestart

	POP {R0, R1, R2, PC}

FirstText:
	PUSH {R1, LR}

	BL tone_Success

	LDR R1, =msg01
	BL LCD_PrintString

	MOV R1, #MSG_DELAY
	BL delay_sec

	BL LCD_Clear
	LDR R1, =msg02
	BL LCD_PrintString

	MOV R1, #MSG_DELAY
	BL delay_sec

	BL LCD_Clear
	LDR R1, =msg03
	BL LCD_PrintString

	MOV R1, #MSG_DELAY
	BL delay_sec

	BL LCD_Clear
	LDR R1, =msg04
	BL LCD_PrintString

	MOV R1, #MSG_DELAY
	BL delay_sec

	BL LCD_Clear
	LDR R1, =ReadySet
	BL LCD_PrintString

	MOV R1, #MSG_DELAY-1
	BL delay_sec

	LDR R1, =go
	BL LCD_PrintString

	MOV R0, #1
	MOV R1, #0
	BL LCD_MoveCursor

	LDR R1, =hex
	BL LCD_PrintString

	// Generate random number
	BL rnd_Sample4

	// Store random number
	LDR R1, =random
	STR R0, [R1]

	// Display random number
	MOV R1, R0
	BL num_to_LED

	BL StartTimer

	POP {R1, PC}

StartTimer:
	PUSH {R1, R2, LR}

	LDR R1, =TIM5_BASE
	LDR R2, [R1, #TIM_CR1]
	ORR R2, #TIM_CEN
	STR R2, [R1, #TIM_CR1]

	POP {R1, R2, PC}


// Stores last time in memory
// Also compares to the smallest time recorded
StopTimer:
	PUSH {R0-R2, LR}

	// Stop the timer
	LDR R1, =TIM5_BASE
	LDR R2, [R1, #TIM_CR1]
	BIC R2, #TIM_CEN
	STR R2, [R1, #TIM_CR1]

	// Load R0 with the count
	LDR R0, [R1, #TIM_CNT]

	// Store the last count to memory
	LDR R1, =time
	STR R0, [R1]

	POP {R0-R2, PC}

ResetTimer:
	PUSH {R1, R2, LR}

	LDR R1, =TIM5_BASE
	MOV R2, #0
	STR R2, [R1, #TIM_CNT]

	POP {R1, R2, PC}

SoftRestart:
	PUSH {R0, R1, LR}

	BL LCD_Clear
	LDR R1, =ReadySet
	BL LCD_PrintString

	MOV R1, #MSG_DELAY-1
	BL delay_sec

	LDR R1, =go
	BL LCD_PrintString

	MOV R0, #1
	MOV R1, #0
	BL LCD_MoveCursor

	LDR R1, =hex
	BL LCD_PrintString

	// Generate random number
	BL rnd_Sample4

	// Store random number
	LDR R1, =random
	STR R0, [R1]

	// Display random number
	MOV R1, R0
	BL num_to_LED

	BL StartTimer

	POP {R0, R1, PC}

DisplayCorrect:
	PUSH {R0-R2, LR}

	// Load and display correct text
	LDR R1, =correct
	BL LCD_PrintString

	// Play success tone
	BL tone_Success

	// Store the last count to memory
	LDR R1, =time
	LDR R0, [R1]

	// Load previous minimum time
	LDR R1, =minTime
	LDR R2, [R1]

	// Compare to this time
	CMP R0, R2

	// Store new time if it is less
	// than the previous minimum time
	IT LT
		STRLT R0, [R1]

	// Increment correct counter
	LDR R1, =correctCNT
	LDRB R2, [R1]
	ADD R2, #1
	STRB R2, [R1]

	POP {R0-R2, PC}

DisplayIncorrect:
	PUSH {R1, LR}

	// Load and display incorrect text
	LDR R1, =incorrect
	BL LCD_PrintString

	// Play failure tone
	BL tone_Failure

	POP {R1, PC}

DisplayTime:
	PUSH {R0, R1, LR}

	// Display time message
	LDR R1, =timeMSG
	BL LCD_PrintString

	// Print time in milliseconds
	LDR R2, =time
	LDR R1, [R2]
	BL LCD_PrintNum

	// Append "ms"
	LDR R1, =milliseconds
	BL LCD_PrintString

	POP {R0, R1, PC}

// This is the last funct called, so I don't
// need to back anything up
DisplayMinTime:
	// Delay between text
	MOV R1, #MSG_DELAY
	//BL delay_sec

	// Congrats you made it
	BL LCD_Clear
	LDR R1, =msg05
	BL LCD_PrintString

	// Delay between text
	MOV R1, #MSG_DELAY
	BL delay_sec

	// You got
	BL LCD_Clear
	LDR R1, =msg06
	BL LCD_PrintString

	// Newline
	MOV R0, #1
	MOV R1, #0
	BL LCD_MoveCursor

	// (some number)
	LDR R1, =correctCNT
	LDR R2, [R1]
	MOV R1, R2
	BL LCD_PrintNum

	// Correct answers!
	LDR R1, =msg07
	BL LCD_PrintString

	// Delay between text
	MOV R1, #MSG_DELAY
	BL delay_sec

	// Fastest time was
	BL LCD_Clear
	LDR R1, =msg08
	BL LCD_PrintString

	// newline
	MOV R0, #1
	MOV R1, #0
	BL LCD_MoveCursor

	// (some time)
	LDR R1, =minTime
	LDR R2, [R1]
	MOV R1, R2
	BL LCD_PrintNum

	// millisecond suffix
	LDR R1, =milliseconds
	BL LCD_PrintString

	// Infinite loop until reset
	B end

end: B end

.section .data
time:
	.word	0

minTime:
	.word	9999

random:
	.word 	0

loops:
	.byte 	0

correctCNT:
	.byte	0


// Text prompts
.section .rodata

// msg01-04 are displayed on first startup
msg01:
	.asciz	"Evan's Hex      conversion game!"

msg02:
	.asciz	"Number will     display on LEDs"

msg03:
	.asciz	"Convert that    number to hex!"

msg04:
	.asciz	"# = 0xE         * = 0xF"

// Ready set go & 0x is used every soft restart
ReadySet:
	.asciz	"Ready? Set! "

go:
	.asciz	"GO!"

hex:
	.asciz	"0x"

// Used when displaying if the answer was correct or not
correct:
	.asciz	" Correct!"

incorrect:
	.asciz	" Incorrect!"

// Time for last conversion
timeMSG:
	.asciz "That took       "

// Milliseconds suffix
milliseconds:
	.asciz "ms"

// End of game text
msg05:
	.asciz	"Congrats! You   made it!"

msg06:
	.asciz	"You got "

msg07:
	.asciz	" Correct!"

msg08:
	.asciz	"Fastest time was"


