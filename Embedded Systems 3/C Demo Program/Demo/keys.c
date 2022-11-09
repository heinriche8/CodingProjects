/**
 * Evan Heinrich
 * 3/8/2022
 * CE2820 011 MS1
 * Key input API
 * Dependencies:
 * 		stdint	- uint types
 * 		system	- provided functionality
 */

#include <stdint.h>
#include "system.h"

/**
 * Reads the status of the two input pushbuttons
 * 	Params:
 * 		void
 * 	Returns:
 * 		uint8_t: the read status of the pushbuttons
 */
uint8_t button_Read() {
	const uint8_t* KEYS = (uint8_t*) PUSHBUTTONS_BASE;
	return *KEYS;
}

/**
 * Reads the status of the ten input slide switches
 * 	Params:
 * 		void
 * 	Returns:
 * 		uint16_t: the read status of the switches
 */
uint16_t switch_Read() {
	const uint16_t* SWITCHES = (uint16_t*) SLIDER_SWITCHES_BASE;
	return *SWITCHES;
}
