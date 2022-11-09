// Kinda recursive-y since that's the header for this but
// I need the anonymous struct for notes
#include "music.h"
#include "registers.h"
#include "delay.h"
#include <stdio.h>

#define MEM_DELAY 10

// Actual method to do notes
void note_Play(uint32_t period, uint32_t duration) {
	// If period = 0, just do a delay

	if(period == 0) {
		delay_us(duration);
	} else {
		// Load period / 2 into CCR
		volatile uint32_t* target = TIM3_CCR;
		*target = (period >> 1);

		// Load period - 1 into ARR
		target = TIM3_ARR;
		*target = period - 1;

		// Start playing the note
		target = TIM3_CR1;
		*target |= 1;

		// Delay for the proper time
		delay_us(duration);

		// Stop playing note
		*target &= ~(1);
	}

	return;
}

// Initialize note delay and piezo config
void music_Init() {
	// Enable GPIOB in RCC
	volatile uint32_t* target = RCC_AHB1ENR;
	*target |= RCC_GPIOBEN;

	// Enable TIM3 in RCC
	target = RCC_APB1ENR;
	*target |= RCC_TIM3EN;

	// Set PB4 to alternate function
	target = GPIOB_MODER;
	*target |= (GPIO_ALTFUN << 8);

	// Set AFRL such that PB4 is connected to TIM3
	target = GPIOB_AFRL;
	*target |= PB4_PIEZO;

	// Set TIM3 prescale to 16, AKA 1 count = 1us
	target = TIM3_PSC;
	*target = 16;

	// Prescale fix
	// Forces an event to be generated and then
	// clears it right away which tricks the timer
	// into applying the prescale somehow
	target = TIM3_EGR;
	*target = 1;
	target = TIM3_SR;
	*target &= ~(1);

	// Configure CCMR for PWM mode
	target = TIM3_CCMR;
	*target |= (OC1M_PWM | OC1M_PE);

	// Enable in CCER
	target = TIM3_CCER;
	*target |= CCER_CC1E;

	// Assert not counting in CR1
	target = TIM3_CR1;
	*target &= ~(1);

	return;
}

// Loops through the array of notes
void music_Play(const note song[], int tempoScale) {
	// Index counter
	int i = 0;

	// Loop through array until we find a note with 0 period and 0 length
	while(!(song[i].period == 0 && song[i].length == 0)) {
		register uint32_t length = song[i].length;
		length *= tempoScale;
		register uint32_t period = song[i].period;
		uint32_t octave = song[i].octave;
		period = period >> octave;
		note_Play(period, length);
		i++;
	}

	return;
}
