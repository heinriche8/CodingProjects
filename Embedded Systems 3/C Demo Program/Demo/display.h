#include <stdint.h>
#include <unistd.h>

#ifndef HEX_DRIVER_ALIVE
#define HEX_DRIVER_ALIVE 1

void Hex_printString(const char* string);
void Hex_scrollPrint(const char* scrollMe);
void Hex_Clear(void);
void LED_Print(uint16_t value);
void LED_Clear(void);
void delay_Set(useconds_t);
useconds_t delay_Get(void);

#endif
