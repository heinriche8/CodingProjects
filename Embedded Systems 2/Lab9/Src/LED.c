/*
	CE2812
	LED Driver
	Evan Heinrich
	Created Lab2
	Displays a 10-bit number on the LEDs on the MSOE Devboard
*/

#include "registers.h"
#include <inttypes.h>
void LED_Init() {
	// Initialize Variables
	volatile uint32_t* addr = 0;
	volatile uint32_t mask = 0;

	// Enable GPIOB in AHB1
	addr = (uint32_t*) RCC_AHB1ENR;	// Set target address
	mask = 1<<1;		// Set mask to enable GPIOB
	*addr |= mask;		// Slick read modify write

	// Set pins PB5-PB15 (skip PB11) as outputs
	addr = (uint32_t*) GPIOB_MODER;	// Set target address
	mask = 0x55155400;				// Mask to set appropriate pins
	*addr |= mask;					// RMW

	return;
}

void LED_PrintNum(uint32_t num) {
	// Initialize Variables
	volatile uint32_t* addr = 0;

	// Split the argument because PB11 is skipped
	uint32_t temp = num & (1023);

	// Upper 4 bits
	uint32_t upper = temp & 960;

	// Lower 6 bits
	uint32_t lower = temp & 63;

	// Value to be displayed with PB11 skipped
	uint32_t output = ((upper << 1) | lower) << 5;

	// Clear LEDs via BSRR
	addr = (uint32_t*)GPIOB_BSRR;
	*addr = 0xFFFF0000;

	*addr = output;
	return;
}
