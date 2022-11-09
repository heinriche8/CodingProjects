#include <stdint.h>

#ifndef VGA_H_
#define VGA_H_ 1

void VGA_Pixelbuf_Swapframes();
void VGA_Charbuf_Init();
void VGA_Charbuf_Clear();
void VGA_Charbuf_Char(const char print, uint32_t x, uint32_t y);
void VGA_Charbuf_String(const char* str, uint32_t x, uint32_t y);
void VGA_Pixelbuf_Init();
void VGA_Pixelbuf_Fill(uint32_t color);
void VGA_Pixelbuf_Pixel(uint32_t x, uint32_t y, uint32_t color);
void VGA_Pixelbuf_Rect(uint32_t anchorX, uint32_t anchorY, uint32_t xLen, uint32_t yLen, uint32_t Color);

#endif
