/*
 * Evan Heinrich
 * 4/13/2022
 * CE2820 MS6
 * LT24 API
 */

#include "system.h"
#include "altera_avalon_spi.h"
#include "LT24.h"
#include <stdint.h>
#include <stdlib.h>

#include "altera_avalon_pio_regs.h"
#include "sys/alt_irq.h"

#define X_MIN 170
#define X_MAX 3000
#define X_RANGE 2830
#define X_SCL 0.107
#define X_OFFSET 10

#define Y_MIN 210
#define Y_MAX 3170
#define Y_RANGE 2960
#define Y_SCL 0.079
#define Y_OFFSET 5

static volatile LCD* LT24;
static uint32_t hRes;
static uint32_t vRes;
static uint32_t numPixels;

static const uint8_t cmdX = 0x92;
static const uint8_t cmdY = 0xD2;
static uint8_t touch_rx[2];



/*
 * Converts an input RGB888 or ARGB8888 value
 * into RGB565 color space. Ignores alpha
 * in conversion.
 */
static uint16_t to565(uint32_t color) {
	// Mask off color values
	uint8_t red = (color & 0x00FF0000)>>16;
	uint8_t green = (color & 0x0000FF00)>>8;
	uint8_t blue = (color & 0x000000FF);

	return ((red & 0xF8) << 8) | ((green & 0xFC) << 3) | (blue >> 3);
}

/*
 * Swaps the front and back buffers of the display
 */
void LCD_swapFrames() {
	// Front buffer is read-only, but writing any value
	// to the front buffer register triggers a swap
	// after the current frame finishes
	LT24 -> fBuffer = 1;

	// Wait until a frame is done drawing for timing reasons
	uint8_t framePending = (LT24 -> status)&(1);
	while(framePending) {
		framePending = (LT24 -> status)&(1);
	}
}

/*
 * Initializes the display by
 * creating a structure and assigning
 * memory locations where the
 * framebuffer will live
 */
void LCD_Init() {
	// Initialize an LCD struct
	LT24 = (LCD*)VIDEO_PIXEL_BUFFER_DMA_0_BASE;

	// Get the resolution and pixel count from struct
	hRes = (LT24->resolution)&(0xFFFF);
	vRes = ((LT24->resolution)&(0xFFFF0000))>>16;
	numPixels = hRes*vRes;

	// Allocate front and back buffers
	uint16_t* front = malloc(numPixels*sizeof(uint16_t));
	uint16_t* back = malloc(numPixels*sizeof(uint16_t));

	// Update the back buffer and swap it into the front
	LT24->bBuffer = (uint32_t)front;
	LCD_swapFrames();

	// Wait until a frame is done drawing for timing reasons
	uint8_t framePending = (LT24 -> status)&(1);
	while(framePending) {
		framePending = (LT24 -> status)&(1);
	}

	// Update the new back buffer
	LT24->bBuffer = (uint32_t)back;
}

/*
 * Fills the back buffer with a user specified
 * color. The input color is in RGB888 or ARGB888
 * format, but gets converted to RGB565.
 * Alpha is ignored.
 */
void LCD_Fill(uint32_t color) {
	// Convert the RGB888 color to RGB565
	uint16_t newColor = to565(color);

	// Get access to the back buffer
	uint16_t* frameBuffer = (uint16_t*)LT24->bBuffer;

	// Y-loop
	for(int y = 0; y < vRes; y++) {
		// X-loop
		for(int x = 0; x < hRes; x++) {
			// Set the current pixel to the new color
			frameBuffer[((320*y)+x)] = newColor;
		}

	}

	LCD_swapFrames();
}

/*
 * Sets an individual pixel to the specified color.
 * Input color can be in ARGB8888, RGB888, or RGB565.
 * Alpha is ignored if input is ARGB8888
 * DOES NOT SWITCH BUFFERS AUTOMATICALLY!
 */
void LCD_drawPixel(uint32_t x, uint32_t y, uint32_t color) {
	// Dimension Check
	if(x < hRes && y < vRes) {
		uint16_t newColor = to565(color);
		uint16_t* frameBuffer = (uint16_t*)LT24->bBuffer;
		frameBuffer[((320*y)+x)] = newColor;
	}
}

/*
 * Draws a specified rectangle on the screen with the specified color.
 * This draws a filled rectangle with no special border.
 * Input color can be in ARGB8888, RGB888, or RGB565.
 * Alpha is ignored if input is ARGB8888
 * DOES NOT SWITCH BUFFERS AUTOMATICALLY!
 */
void LCD_drawRect(uint32_t anchorX, uint32_t anchorY, uint32_t xLen, uint32_t yLen, uint32_t Color) {

	// Dimension Check
	if(((anchorX+xLen) < hRes) && ((anchorY+yLen) < vRes)) {

		for(int y = anchorY; y < (anchorY+yLen); y++) {
			for(int x = anchorX; x < (anchorX+xLen); x++) {

				uint16_t newColor = to565(Color);
				uint16_t* frameBuffer = (uint16_t*)LT24->bBuffer;
				frameBuffer[((320*y)+x)] = newColor;

			}
		}
	}

}

uint16_t getTouchX() {
	// Get X
	alt_avalon_spi_command(TOUCH_SPI_BASE,0,
								1, &cmdX,
								2, touch_rx,
								0);
	return (touch_rx[0]<<5) | (touch_rx[1]>>3);
}

uint16_t getTouchY() {
	// Get Y
	alt_avalon_spi_command(TOUCH_SPI_BASE,0,
									1, &cmdY,
									2, touch_rx,
									0);
	return (touch_rx[0]<<5) | (touch_rx[1]>>3);
}

uint32_t getPixelX() {

	uint32_t location = 0xFFFFFFFF;

	if(getTouchY() > Y_MIN) {
		location = getTouchX() * X_SCL;
	}

	return (hRes-location)+X_OFFSET;
}

uint32_t getPixelY() {
	uint32_t location = 0xFFFFFFFF;

	if(getTouchY() > Y_MIN) {
		location = getTouchY() * Y_SCL;
	}

	return location-Y_OFFSET;
}
