#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <math.h>
#include "registers_new.h"

#define PI 3.1415926
#define DAC_MAX 4095

static uint8_t waveStatus;
static uint16_t* waveform;

/**
 * Generates a dynamically allocated float array containing
 * samples of a sine/cosine wave with samples on the interval
 * [0,4095]
 */
uint16_t* sineWave(uint32_t samples) {
	// Create a chunk of memory to dump the waveform with
	// one extra space for a terminator
	uint16_t* wave = (uint16_t*) malloc((samples+1) * sizeof(uint16_t));

	for(int i = 0; i < samples; i++) {
		// For speed purposes try to fit a float into a register or registers
		register float sample = (0.5) * cosf((2*PI*i)/(samples-1)) + (0.5);

		// Convert to a 16-bit int even though its artificially limited to 12-bit
		uint16_t conversion = sample * DAC_MAX;

		// Write
		wave[i] = conversion;
	}

	// Terminator
	wave[samples] = -1;

	return wave;
}

/**
 * Generates a dynamically allocated float array containing
 * samples of a triangle wave, with samples being on
 * the interval of [0,4095]
 */
uint16_t* triWave(uint32_t samples) {
	// Create a chunk of memory to dump the waveform with
	// one extra space for a terminator
	uint16_t* wave = (uint16_t*) malloc((samples+1) * sizeof(uint16_t));

	for(int i = 0; i < samples; i++) {
		register float sample = (2/(2*PI)) * asinf(sinf((2*PI*i)/(samples-1))) + (0.5);

		uint16_t conversion = sample * DAC_MAX;

		wave[i] = conversion;
	}

	wave[samples] = -1;
	return wave;

}

/**
 * Generates a dynamically allocated float array containing
 * samples of a sawtooth wave, with samples being on the
 * interval of [0,4095]
 */
uint16_t* sawtoothWave(uint32_t samples) {
	// Create a chunk of memory to dump the waveform with
	// one extra space for a terminator
	uint16_t* wave = (uint16_t*) malloc((samples+1) * sizeof(uint16_t));

	for(int i = 0; i < samples; i++) {
		register float sample = ((-1 / PI) * atanf((1/tanf((PI*i/(samples-1)))))) + 0.5;

		uint16_t conversion = sample * DAC_MAX;

		wave[i] = conversion;
	}

	// Terminator
	wave[samples] = -1;

	return wave;
}

void wave_Init() {
	// Use TIM6 (basic 16-bit timer) to sequence the waveform
	// NOTE: TIM6 is only an upcounter! Write period to ARR!
	// PA4 as analog output connected to DAC ch.1

	volatile RCC* RCC_Target = (RCC*) RCC_BASE;
	volatile GPIO* GPIOA = (GPIO*) GPIOA_BASE;
	volatile TIMER_BASIC* TIM6 = (TIMER_BASIC*) TIM6_BASE;
	volatile NVIC* NVIC_Target = (NVIC*) NVIC_BASE;
	volatile DAC* DAC_Target = (DAC*) DAC_BASE;

	// Debugging purposes, set TIM6 to freeze in debugging mode
	uint32_t* DBG_APB1 = (uint32_t*)0xE0042008;
	*DBG_APB1 |= 1<<4;

	// Enable GPIOA
	RCC_Target->AHB1ENR |= RCC_GPIOAEN;

	// Set PA4 as analog
	GPIOA->MODER |= (GPIO_ANALOG << 8);

	// Enable TIM6
	RCC_Target->APB1ENR |= RCC_TIM6EN;

	// Enable DAC
	RCC_Target->APB1ENR |= RCC_DACEN;

	// Prescale TIM6 to 1us
	TIM6->PSC = 15;

	// Prescale fix
	TIM6->EGR = 1;
	TIM6->SR &= ~(1);

	// Assert not one-pulse mode
	TIM6->CR1 &= ~(TIM_OPM);

	// Enable DAC Ch1 & its Trigger
	DAC_Target->CR |= DAC_CH1EN;
	DAC_Target->CR |= (DAC_SWTGR << 3);
	DAC_Target->CR |= DAC_TEN1;

	// Enable TIM6 interrupts in NVIC
	// NVIC_ISER1 bit 22
	NVIC_Target->ISER[1] |= 1<<22;

}

void wave_Start(uint16_t* samples, uint32_t frequency, uint32_t numSamples) {
	// If the generator is already running
	if(waveStatus != 0) {
		printf("Waveform generator is already running. Stop the current generator to continue\n");
	} else {
		volatile TIMER_BASIC* TIM6 = (TIMER_BASIC*) TIM6_BASE;

		// Set the waveform
		waveform = samples;

		// Set status flag to busy
		waveStatus = 1;

		// Determine period from frequency
		double timePerSample = ((double)1) / frequency;

		// Convert period to microseconds
		timePerSample *= 10E5;

		// Divide period by number of samples for time per sample
		timePerSample = timePerSample / numSamples;

		// Push to ARR
		TIM6->ARR = (uint16_t)timePerSample;

		// Enable TIM6 interrupts
		TIM6->DIER |= TIM_UIE;

		// Set TIM6_CEN
		TIM6->CR1 |= TIM_CEN;
	}

	return;
}

void wave_Stop() {
	// If the generator is not running
	if(waveStatus != 1) {
		printf("Waveform generator is not currently running. No changes made.\n");
	} else {
		volatile TIMER_BASIC* TIM6 = (TIMER_BASIC*) TIM6_BASE;

		// Clear TIM6_CEN
		TIM6->CR1 &= ~(TIM_CEN);

		// Stop TIM6 interrupts
		TIM6->DIER &= ~(TIM_UIE);

		waveStatus = 0;
	}
	return;
}

void TIM6_DAC_IRQHandler(void) {
	// Clear status register
	volatile TIMER_BASIC* TIM6 = (TIMER_BASIC*) TIM6_BASE;
	TIM6->SR = 0;

	volatile DAC* DAC_Target = (DAC*) DAC_BASE;

	// Iterator
	static uint32_t i = 0;

	// Read sample
	register uint16_t sample = waveform[i];

	// Check for terminator
	if(sample != 65535) {
		// Push sample to DAC
		// Samples are already limited to 12-bit, so just write
		DAC_Target->CH1_R12 = sample;

		// Increment iterator
		i++;

		// Trigger DAC Ch1
		DAC_Target->SW_TRIGR |= 1<<0;
	} else {
		// Reset iterator
		i = 0;

		// Read 0th sample
		sample = waveform[i];

		// Push sample to DAC
		DAC_Target->CH1_R12 = sample;

		// Increment iterator
		i++;

		// Trigger DAC Ch1
		DAC_Target->SW_TRIGR |= 1<<0;
	}

	return;
}
