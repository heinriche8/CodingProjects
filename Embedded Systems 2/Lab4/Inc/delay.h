/*
	CE2812
	Delay header
	Evan Heinrich
	Created Lab2
*/

#ifndef DELAY_DRIVER_ALIVE
#define DELAY_DRIVER_ALIVE 1
#include <stdint.h>

void delay_Init();

void delay_ms(uint32_t);

void delay_us(uint32_t);

#endif
