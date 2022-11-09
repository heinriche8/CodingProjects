/**
 * Evan Heinrich
 * 3/15/2022
 * CE2820 011 Milestone 2
 * 7 segment display API
 * Dependencies:
 * 		string	-	string length
 * 		system	-	provided functionality
 * 		unistd	-	quartus provided functionality
 * 		stdint	-	uint types
 */

#include <string.h>
#include "system.h"
#include "unistd.h"
#include <stdint.h>

static useconds_t delay = ((2.7E5) /2 );

/**
 * Prints a 16-bit value to the system LEDs
 * Note: There are only 10 LEDs
 * Params:
 * 		uint16_t value - number to be printed
 * Returns:
 * 		Void
 */
void LED_Print(uint16_t value) {
	uint16_t* LEDs = (uint16_t*) LEDS_BASE;

	*LEDs = value;

	return;
}

/**
 * Clears the 10 LEDs
 * Params:
 * 		Void
 * Returns:
 * 		Void
 */
void LED_Clear() {
	uint16_t* LEDs = (uint16_t*) LEDS_BASE;

	*LEDs = 0;

	return;
}


/**
 * Prints a string onto the 7 segment displays
 * ASSUMES THE INCOMING STRING IS 6 CHARS LONG
 * PRINTS LEFT-BIASED
 * Params:
 * 		const char* string - the string to be printed
 * Returns:
 * 		Void
 */
void Hex_printString(const char* string) {
	uint32_t* seg7_low = (uint32_t*) HEX3_HEX0_BASE;
	uint16_t* seg7_hi = (uint16_t*) HEX5_HEX4_BASE;

	uint32_t lower = 0;
	uint16_t upper = 0;

	// Only 6x 7seg displays
	// For-loops were tricky, so since there are only
	// 6 displays, I'll do this manually

	// Working from HEX5 down to HEX0

	// HEX5
	upper |= (string[0] << 8);

	// HEX4
	upper |= string[1];

	// HEX3
	lower |= (string[2] << (8*3));

	// HEX2
	lower |= (string[3] << (8*2));

	// HEX1
	lower |= (string[4] << (8*1));

	// HEX0
	lower |= (string[5] << (8*0));

	*seg7_low = lower;
	*seg7_hi = upper;

	return;
}

/**
 * Scrolls a provided message on the 7 segment
 * displays, scrolling from right to left
 * Params:
 * 		const char* string - the string to be scrolled
 * Returns:
 * 		Void
 */
void Hex_scrollPrint(const char* scrollMe) {
	// Create a blank char buffer of 6 chars
	char buffer[6] = "\0\0\0\0\0\0";

	// Determine the length of the provided message
	int messageLength = strlen(scrollMe);

	// Variable to store how many times to shift the buffer
	int iterations = 0;

	// If the message will fit on the display, we don't need more than
	// one iteration. Otherwise it's the length of the message minus 5 iterations.
	if(messageLength < 7) {
		iterations = 1;
	} else {
		iterations = messageLength - 5;
	}


	// Actual scrolling loop
	for(int i = 0; i < iterations; i++) {
		// Copy 6 chars starting at specified index into buffer
		for(int j = 0; j < 6; j++) {
			buffer[j] = scrollMe[j+i];
		}
		// Print the actual message
		Hex_printString(buffer);

		// Print the starting index on the LEDs
		LED_Print(i);

		usleep(delay);
	}

	return;
}

/**
 * Clears the seven segment displays
 * Params:
 * 		Void
 * Returns:
 * 		Void
 */
void Hex_Clear() {
	uint32_t* seg7_low = (uint32_t*) HEX3_HEX0_BASE;
	uint16_t* seg7_hi = (uint16_t*) HEX5_HEX4_BASE;

	*seg7_low = 0;
	*seg7_hi = 0;

	return;
}

/*
 * Sets the new delay time between scrolls
 * for Hex_scrollPrint
 * Params:
 * 		useconds_t newDelay - new delay time between scrolls
 * Returns:
 * 		Void
 */
void delay_Set(useconds_t newDelay) {
	delay = newDelay;
	return;
}

useconds_t delay_Get() {
	return delay;
}
