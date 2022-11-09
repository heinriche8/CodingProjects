/*
 * Evan Heinrich
 * 4/13/2022
 * CE2820 MS6
 * LT24 API Header
 */

#ifndef LT24_API_ALIVE
#define LT24_API_ALIVE 1

#include <stdint.h>

typedef struct {
	uint32_t fBuffer;
	uint32_t bBuffer;
	uint32_t resolution;
	uint32_t status;
} LCD;

void LCD_Init();
void LCD_swapFrames();
void LCD_Fill(uint32_t color);
void LCD_drawPixel(uint32_t x, uint32_t y, uint32_t color);
void LCD_drawRect(uint32_t anchorX, uint32_t anchorY, uint32_t xLen, uint32_t yLen, uint32_t Color);
uint16_t getTouchX();
uint16_t getTouchY();
uint32_t getPixelX();
uint32_t getPixelY();

#endif
