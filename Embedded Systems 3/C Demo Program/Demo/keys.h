#include <stdint.h>
#ifndef KEYS_API_ALIVE
#define KEYS_API_ALIVE 1

#define KEY0 1<<0
#define KEY1 1<<1

uint8_t button_Read();
uint16_t switch_Read();

#endif
