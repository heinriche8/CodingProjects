/*
	CE2812
	Delay code
	Evan Heinrich
	Created Lab2
	Note: Uses TIM2 prescaled to 1kHz
*/

#include "registers.h"
#include <inttypes.h>
void delay_Init() {
	// Initialize Variables
	volatile uint32_t* addr = 0;
	volatile uint32_t contents = 0;
	volatile uint32_t mask = 0;

	// Enable TIM2 in APB1ENR
	addr = (uint32_t*)RCC_APB1ENR;			// Set target address
	mask = 1 << 0;				// Set mask
	contents = *addr | mask;	// Read & modify
	*addr = contents;			// Write

	// Set TIM2 CR configurations
	addr = (uint32_t*)TIM2_CR1;	// Set target address
	mask = (1<<3)|(1<<4);		// Set mask for one-pulse and count down
	contents = *addr | mask;	// Read & modify
	*addr = contents;			// Write

	// Set TIM2 prescale (no RMW needed)
	addr = (uint32_t*)TIM2_PSC;	// Set target address
	*addr = 16;					// Set prescale to 16, changing clock to 1MHz

	// Prescale fix
	// Forces an event to be generated and then
	// clears it right away which tricks the timer
	// into applying the prescale somehow
	addr = (uint32_t*)TIM2_EGR;
	*addr = 1;
	addr = (uint32_t*)TIM2_SR;
	*addr &= ~(1);

	return;
}

void delay_ms(uint32_t delay) {
	// Initialize Variables
	volatile uint32_t* addr = 0;
	volatile uint32_t contents = 0;
	volatile uint32_t mask = 0;

	// Assert counting is not enabled
	addr = (uint32_t*)TIM2_CR1;	// Set target address
	mask = ~(1<<0);				// Set mask to clear CEN
	contents = *addr & mask;	// Read & modify
	*addr = contents;			// Write

	// Write the desired count (no RMW necessary)
	addr = (uint32_t*)TIM2_CNT;	// Set target address
	*addr = delay * 1000;		// Write desired delay, converted to ms

	// Enable count
	addr = (uint32_t*)TIM2_CR1;	// Set target address
	mask = (1<<0);				// Set mask
	contents = *addr | mask;	// Read & modify
	*addr = contents;			// Write

	// Busy loop
	addr = (uint32_t*)TIM2_CR1;
	mask = (1<<0);
	contents = *addr & mask;

	while(contents != 0) {
		contents = *addr & mask;
	}

	return;
}

void delay_us(uint32_t delay) {
	// Initialize Variables
	volatile uint32_t* addr = 0;
	volatile uint32_t contents = 0;
	volatile uint32_t mask = 0;

	// Assert counting is not enabled
	addr = (uint32_t*)TIM2_CR1;	// Set target address
	mask = ~(1<<0);				// Set mask to clear CEN
	contents = *addr & mask;	// Read & modify
	*addr = contents;			// Write

	// Write the desired count (no RMW necessary)
	addr = (uint32_t*)TIM2_CNT;	// Set target address
	*addr = delay;				// Write desired delay

	// Enable count
	addr = (uint32_t*)TIM2_CR1;	// Set target address
	mask = (1<<0);				// Set mask
	contents = *addr | mask;	// Read & modify
	*addr = contents;			// Write

	// Busy loop
	addr = (uint32_t*)TIM2_CR1;
	mask = (1<<0);
	contents = *addr & mask;

	while(contents != 0) {
		contents = *addr & mask;
	}

	return;
}
