/**
 * Evan Heinrich
 * 4/6/2022
 * CE2820 Milestone 5
 * Joystick API Header
 */

#include <stdint.h>

#ifndef THUMBSTICK_API_ALIVE
#define THUMBSTICK_API_ALIVE 1

void cstick_Init(void);
void cstick_Start(void);
void cstick_Stop(void);
uint32_t cstick_getx(void);
uint32_t cstick_gety(void);
void click_init(void* ISR);

#endif
