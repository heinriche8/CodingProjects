#ifndef GENERATOR_IS_ALIVE
#define GENERATOR_IS_ALIVE 1

#include <stdint.h>

uint16_t* sineWave(uint32_t samples);
uint16_t* triWave(uint32_t samples);
uint16_t* sawtoothWave(uint32_t samples);
void wave_Init(void);
void wave_Start(uint16_t* samples, uint32_t frequency, uint32_t numSamples);
void wave_Stop(void);
void TIM6_DAC_IRQHandler(void);

#endif
