/*
	CE2812
	Delay code
	Evan Heinrich
	Created Lab2
	Note: Uses TIM2 prescaled to 1MHz
*/

#include "registers_new.h"
#include <inttypes.h>
void delay_Init() {
	volatile RCC* RCC_Target = (RCC*) RCC_BASE;
	volatile TIMER* TIM2 = (TIMER*) TIM2_BASE;

	// Enable TIM2 in APB1ENR
	RCC_Target->APB1ENR |= RCC_TIM2EN;

	// Set TIM2 CR configurations, one-pulse & countdown
	TIM2->CR1 |= (TIM_OPM | TIM_DIR);

	// Set TIM2 prescale to 16, aka 1 count = 1us
	TIM2->PSC = 16;

	// Prescale fix
	// Forces an event to be generated and then
	// clears it right away which tricks the timer
	// into applying the prescale somehow
	TIM2->EGR = 1;
	TIM2->SR &= ~(1);

	return;
}

void delay_ms(uint32_t delay) {
	// Initialize Variables
	volatile TIMER* TIM2 = (TIMER*) TIM2_BASE;

	// Assert counting is not enabled
	TIM2->CR1 &= ~(TIM_CEN);

	// Write the desired count, x1000 for ms to us conversion
	TIM2->CNT = (delay * 1000);

	// Enable count
	TIM2->CR1 |= TIM_CEN;

	// Busy loop
	uint32_t mask = (TIM_CEN);
	volatile uint32_t contents = TIM2->CR1 & mask;

	while(contents != 0) {
		contents = TIM2->CR1 & mask;
	}

	return;
}

void delay_us(uint32_t delay) {
	// Initialize Variables
	volatile TIMER* TIM2 = (TIMER*) TIM2_BASE;

	// Assert counting is not enabled
	TIM2->CR1 &= ~(TIM_CEN);

	// Write the desired count
	TIM2->CNT = delay;

	// Enable count
	TIM2->CR1 |= TIM_CEN;

	// Busy loop
	uint32_t mask = (TIM_CEN);
	volatile uint32_t contents = TIM2->CR1 & mask;

	while(contents != 0) {
		contents = TIM2->CR1 & mask;
	}

	return;
}
