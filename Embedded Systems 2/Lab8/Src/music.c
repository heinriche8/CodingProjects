// Kinda recursive-y since that's the header for this but
// I need the anonymous struct for notes
#include "music.h"
#include "registers_new.h"
#include "delay.h"
#include <stdio.h>

/**
 * Global variable that holds the next note index for the
 * current background song
 */
static uint32_t musicIndex;


/**
 * Global variable that points to the current song playing
 */
static note* backgroundSong;

/**
 * Flag variable to show if a note is playing in the background
 */
static uint8_t backgroundPlaying = 0;

/**
 * Plays individual notes using a busy wait
 */
void note_Play(uint32_t period, uint32_t duration) {
	volatile TIMER* TIM3 = (TIMER*) TIM3_BASE;
	if(period == 0) {
		// If period = 0, just do a delay
		delay_us(duration);
	} else {
		// Load period / 2 into CCR
		TIM3->CCR1 = period >> 1;

		// Load period - 1 into ARR
		TIM3->ARR = period - 1;

		// Start playing the note
		TIM3->CR1 |= 1;

		// Delay for the proper time
		delay_us(duration);

		// Stop playing note
		TIM3->CR1 &= ~(1);
	}

	return;
}

/**
 * Initializes the appropriate timers and GPIO port
 */
void music_Init() {
	// Pointers for all used peripherals
	// TIM5 will be a simple down-counter which will trigger interrupts
	// 	each time a note completes playing in the background.
	volatile RCC* RCC_Target = (RCC*) RCC_BASE;
	volatile GPIO* GPIOB = (GPIO*) GPIOB_BASE;
	volatile TIMER* TIM3 = (TIMER*) TIM3_BASE;
	volatile TIMER* TIM5 = (TIMER*) TIM5_BASE;
	volatile NVIC* NVIC_Target = (NVIC*) NVIC_BASE;

	// Enable GPIOB in RCC
	RCC_Target->AHB1ENR |= RCC_GPIOBEN;

	// Enable TIM3 and TIM5 in RCC
	RCC_Target->APB1ENR |= (RCC_TIM3EN | RCC_TIM5EN);

	// Set PB4 to alternate function
	GPIOB->MODER = GPIO_ALTFUN << 8;

	// Set AFRL such that PB4 is connected to TIM3
	GPIOB->AFRL = PB4_PIEZO;

	// Set TIM3 & TIM5 prescale to 16, AKA 1 count = 1us
	TIM3->PSC = 16;
	TIM5->PSC = 16;

	// Prescale fix
	// Forces an event to be generated and then
	// clears it right away which tricks the timer
	// into applying the prescale somehow
	TIM3->EGR = 1;
	TIM3->SR &= ~(1);
	TIM5->EGR = 1;
	TIM5->SR &= ~(1);

	// Configure CCMR for PWM mode
	TIM3->CCMR1 |= (OC1M_PWM | OC1M_PE);

	// Enable in CCER
	TIM3->CCER |= CCER_CC1E;

	// Assert not counting in CR1
	TIM3->CR1 &= ~(TIM_CEN);
	TIM5->CR1 &= ~(TIM_CEN);

	// Enable interrupts for TIM5
	TIM5->DIER |= TIM_UIE;

	// Set TIM5 to count-down and one-pulse
	TIM5->CR1 |= (TIM_OPM | TIM_DIR);

	// Enable the TIM5 interrupt in ISER, slot 50
	//	aka 1<<18 in ISER1
	NVIC_Target->ISER[1] = 1<<18;

	return;
}

/**
 * Iterates through the provided array of notes until a null
 * note is found
 */
void music_Play(const note song[]) {
	// Index counter
	int i = 0;

	if(backgroundPlaying == 0) {
		// Loop through array until we find a note with 0 period and 0 length
		while(!(song[i].period == 0 && song[i].length == 0)) {
			// Attempt to put these in a register for passing to the funct
			register uint32_t length = song[i].length;
			register uint32_t period = song[i].period;

			uint32_t octave = song[i].octave;
			period = period >> octave;
			note_Play(period, length);
			i++;
		}
	} else {
		printf("Cannot play a song while one is playing in the background\n");
	}

	return;
}

/**
 * Stops a background song that is playing
 */
void music_StopBackground() {
	volatile TIMER* TIM3 = (TIMER*) TIM3_BASE;
	volatile TIMER* TIM5 = (TIMER*) TIM5_BASE;

	// Stop the timers
	TIM5->CR1 &= ~(TIM_CEN);
	TIM3->CR1 &= ~(TIM_CEN);

	// Reset controls
	backgroundPlaying = 0;
	musicIndex = 0;

	return;
}

/**
 * Starts the provided song playing in the background
 */
void music_Background(note song[]) {
	volatile TIMER* TIM3 = (TIMER*) TIM3_BASE;
	volatile TIMER* TIM5 = (TIMER*) TIM5_BASE;

	// Stop background song
	music_StopBackground();

	// Set flag to show music is playing
	backgroundPlaying = 1;

	// Set new address and reset note index
	// note index gets set to one because this method
	// plays the 0th note on its own, then the interrupts
	// take over
	backgroundSong = song;
	musicIndex = 1;

	// Load the period and length
	uint32_t period = song[0].period;
	period = period >> song[0].octave;
	uint32_t length = song[0].length;

	// Write values
	TIM5->CNT = length;
	TIM3->ARR = period - 1;
	TIM3->CCR1= period >> 1;

	// Start timers
	TIM5->DIER|= TIM_UIE;
	TIM3->CR1 |= TIM_CEN;
	TIM5->CR1 |= TIM_CEN;

	return;
}

/**
 * ISR
 * Triggers when TIM5 hits zero;
 * loads next note from the global song array into appropriate
 * timers and increments the global note index
 */
void TIM5_IRQHandler() {
	volatile TIMER* TIM3 = (TIMER*) TIM3_BASE;
	volatile TIMER* TIM5 = (TIMER*) TIM5_BASE;


	// Clear the register that triggers interrupts
	TIM5->SR = 0;

	// Stop the frequency timer
	TIM3->CR1 &= ~(TIM_CEN);

	// Load current note info
	note current = backgroundSong[musicIndex];
	uint32_t period = current.period;
	period = period >> current.octave;
	uint32_t length = current.length;

	// If the note isn't a terminator
	if(!(period == 0 && length == 0)) {
		// Write the period values accordingly
		TIM3->ARR = period - 1;
		TIM3->CCR1 = period >> 1;
		// Write the note duration
		TIM5->CNT = length;

		// Increment the note counter
		musicIndex++;

		// Enable both timers
		TIM3->CR1 |= TIM_CEN;
		TIM5->CR1 |= TIM_CEN;

	// If the note is a terminator
	} else if(period == 0 && length == 0) {
		// Clear the note counter
		musicIndex = 0;

		// Clear the flag for background songs
		backgroundPlaying = 0;

		// Disable timer 5's interrupts
		TIM5->DIER &= ~(TIM_UIE);

		// Clear the timers
		TIM3->ARR = 0;
		TIM3->CCR1 = 0;
		TIM5->CNT = 0;
	}

	return;
}


