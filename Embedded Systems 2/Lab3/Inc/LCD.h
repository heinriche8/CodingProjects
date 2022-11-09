/*
	CE2812
	Header for LCD API
	Evan Heinrich
	12/16/2021
*/

#ifndef LCD_IS_ALIVE
#define LCD_IS_ALIVE 1

#include <stdint.h>

void LCD_Init();
void LCD_Clear();
void LCD_Home();
void LCD_MoveCursor(uint32_t row, uint32_t col);
uint32_t LCD_PrintString(char* string);
uint32_t LCD_PrintNum(signed int num);

#endif
