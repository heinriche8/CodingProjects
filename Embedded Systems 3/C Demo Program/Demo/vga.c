#include "system.h"
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
	uint32_t fBuffer;
	uint32_t bBuffer;
	uint32_t resolution;
	uint32_t status;
} VGA_Buff;


static VGA_Buff* pixelbuf;
static uint8_t* charbuf;
static uint32_t pixel_hRes;
static uint32_t pixel_vRes;
static uint32_t pixel_hBits;
static uint32_t pixel_vBits;
static uint32_t numPixels;
static const uint32_t char_wBits = 7;
static const uint32_t char_vBits = 6;

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
 * Prompts the pixel buffer to swap frames after the current
 * frame finishes drawing. Blocks until the frame swap is
 * successful.
 */
void VGA_Pixelbuf_Swapframes() {
	// Front buffer is read-only, but writing any value
	// to the front buffer register triggers a swap
	// after the current frame finishes
	pixelbuf -> fBuffer = 1;

	// Wait until a frame is done drawing for timing reasons
	uint8_t framePending = (pixelbuf -> status)&(1);
	while(framePending) {
		framePending = (pixelbuf -> status)&(1);
	}
}

/*
 * Initializes the character buffer API and clears whatever may
 * be stored in the character buffer.
 */
void VGA_Charbuf_Init() {
	// Initialize the pointer
	charbuf = (uint8_t*) VGA_SUBSYSTEM_CHAR_BUF_SUBSYSTEM_ONCHIP_SRAM_BASE;

	// "Clear" the display by setting to spaces
	memset(charbuf, ' ', (1<<char_wBits)*(1<<char_vBits)*sizeof(uint8_t));

}

/*
 * Clears the character buffer by setting all entries to
 * the ASCII space character.
 */
void VGA_Charbuf_Clear() {
	memset(charbuf, ' ', (1<<char_wBits)*(1<<char_vBits)*sizeof(uint8_t));
}

/*
 * Prints a single character to the character buffer. Enforces
 * dimension checks.
 * Params:
 * 		const char print: ASCII character to be printed
 * 		uint32_t x: X coordinate of the character's position
 * 		uint32_t y: Y coordinate of the character's position
 */
void VGA_Charbuf_Char(const char print, uint32_t x, uint32_t y) {
	if(x < (1<<char_wBits) && y < (1<<char_vBits)) {
		charbuf[(y<<char_wBits)+x] = print;
	}
}

/*
 * Prints a null-terminated string to the character buffer. Enforces
 * dimension checks, such that a string must fit on a single 80-char
 * row. Does not handle newline chars or tab chars.
 * Params:
 * 		const char* str: string to be printed
 * 		uint32_t x: Starting location x-coordinate
 */
void VGA_Charbuf_String(const char* str, uint32_t x, uint32_t y) {
	uint32_t length = strlen(str);

	if((x+length) < (1<<char_wBits) && y < (1<<char_vBits)) {
		for(int i = 0; i < length; i++) {
			charbuf[(y<<char_wBits)+(i+x)] = str[i];
		}
	}
}

/*
 * Initializes the VGA screen's pixel buffer. Resolution should
 * be 160x120 pixels, upscaled to 640x480 in hardware.
 */
void VGA_Pixelbuf_Init() {
	// Attach the physical component to the struct
	pixelbuf = (VGA_Buff*)VGA_SUBSYSTEM_VGA_PIXEL_DMA_BASE;

	// Determine X/Y bit depth
	pixel_vBits = (pixelbuf->status >> 24) & 0xFF;
	pixel_hBits = (pixelbuf->status >> 16) & 0xFF;

	// Calculate resolution from bit depth
	// Should be 256x128
	pixel_hRes = (1<<pixel_hBits);
	pixel_vRes = (1<<pixel_vBits);

	numPixels = pixel_hRes*pixel_vRes;

	uint16_t* front = malloc(numPixels*sizeof(uint16_t));
	uint16_t* back = malloc(numPixels*sizeof(uint16_t));

	// Update the back buffer and swap it into the front
	pixelbuf->bBuffer = (uint32_t)front;
	VGA_Pixelbuf_Swapframes();


	// Update the new back buffer
	pixelbuf->bBuffer = (uint32_t)back;
}

/*
 * Fills the rear pixel buffer with the specified color.
 * Automatically swaps buffers.
 * Params:
 * 		uint32_t color: RGB888 or ARGB8888 color value. Converts to RGB565 for display.
 * 						ignores the alpha channel if present.
 */
void VGA_Pixelbuf_Fill(uint32_t color) {
	// Convert the RGB888 color to RGB565
	uint16_t newColor = to565(color);

	// Get access to the back buffer
	uint16_t* frameBuffer = (uint16_t*)pixelbuf->bBuffer;

	// Y-loop
	for(int y = 0; y < pixel_vRes; y++) {
		// X-loop
		for(int x = 0; x < pixel_hRes; x++) {

			// Set the current pixel to the new color
			frameBuffer[((y<<pixel_hBits)+x)] = newColor;
		}

	}

	VGA_Pixelbuf_Swapframes();
}

/*
 * Sets the specified pixel to the specified RGB888 or ARGB8888 color.
 * Does not automatically swap buffers.
 * Params:
 * 		uint32_t x: X coordinate of the target pixel
 * 		uint32_t y: Y coordinate of the target pixel
 * 		uint32_t color: RGB888 or ARGB8888 target color. Will be converted to RGB565 for display.
 * 						Will ignore alpha channel if present.
 */
void VGA_Pixelbuf_Pixel(uint32_t x, uint32_t y, uint32_t color) {
	// Dimension Check
	if(x < pixel_hRes && y < pixel_vRes) {
		uint16_t newColor = to565(color);
		uint16_t* frameBuffer = (uint16_t*)pixelbuf->bBuffer;
		frameBuffer[(y<<pixel_hBits)+x] = newColor;
	}
}

/*
 * Draws a specified rectangle of the specified dimensions in the specified colors.
 * Params:
 * 		uint32_t anchorX: X coordinate of the anchor pixel
 * 		uint32_t anchorY: Y coordinate of the anchor pixel
 * 		uint32_t xLen: width of the rectangle
 * 		uint32_t yLen: height of the rectangle
 * 		uint32_t color: RGB888 or ARGB8888 color. Will be converted to RGB565 ignoring alpha
 *
 */
void VGA_Pixelbuf_Rect(uint32_t anchorX, uint32_t anchorY, uint32_t xLen, uint32_t yLen, uint32_t Color) {
	uint16_t newColor = to565(Color);
	uint16_t* frameBuffer = (uint16_t*)pixelbuf->bBuffer;

	// Dimension Check
	if(((anchorX+xLen) < pixel_hRes) && ((anchorY+yLen) < pixel_vRes)) {

		for(int y = anchorY; y < (anchorY+yLen); y++) {
			for(int x = anchorX; x < (anchorX+xLen); x++) {

				frameBuffer[((y<<pixel_hBits)+x)] = newColor;

			}
		}
	}

}
