/*
 * Evan Heinrich
 * 5/10/2022
 * CE2820 MS10
 * SoC Demo Program
 */

// University Program Functions
#include "unistd.h"

// Hex/LED displays
#include "display.h"

// Pushbutton/Slider input
#include "keys.h"

// LT24 Controls/Touch interface
#include "LT24.h"

// Interrupt related stuff
#include "altera_avalon_pio_regs.h"
#include "sys/alt_irq.h"

// Joystick readings and IRQ initialization
#include "cstick.h"

// Servo controls
#include "servo.h"

// VGA controls
#include "vga.h"

// Accelerometer controls
#include "accel.h"

// printf and sprintf
#include <stdio.h>

#define JOY_TO_SERVO 0.0461

void click_isr(void* context);
void rainbow_Init();
void Demo_VGA_Init();
void Demo_LCD_Init();
void Demo_Cstick_Init();

int main() {
	printf("Lots of initialization that needs to happen, please wait...\n");

	volatile uint16_t switches;
	volatile uint32_t cstick_X, cstick_Y;

	char pos[7] = "";
	char accelerometer[31] = "";
	const char* done_init = "Initialization is done! Enjoy!";
	const char* accel_msg = "Accelerometer Readings:";
	const char* notYet = "(No readings yet)";
	const char* paused = "(Accelerometer Paused)";

	// Just a lot of spaces to "clear" a line
	const char* clearLine = "                                                          ";


	Demo_VGA_Init();
	Demo_LCD_Init();
	Demo_Cstick_Init();
	Accel_Init();

	printf("Initialization done! Enjoy!\n");
	VGA_Charbuf_String(done_init, 2, 12);
	VGA_Charbuf_String(accel_msg, 2, 20);
	VGA_Charbuf_String(notYet, 2, 22);


	while(1) {
		switches = switch_Read();

		// Switches = 1 -> Joystick/Servo
		// Switches = 0 -> Accelerometer/VGA

		if(switches & 1) {

			// Joystick/Servo Program

			// Read the ADC samples for the X and Y values of the
			// joystick
			cstick_X = cstick_getx();
			cstick_Y = cstick_gety();

			// Convert joystick readings to degrees, assuming
			// a range of 0-3900 for the joystick
			uint8_t servoX, servoY;
			servoX = cstick_X * JOY_TO_SERVO;
			servoY = cstick_Y * JOY_TO_SERVO;

			// Move the servos
			servo_deg(servoX, 0);
			servo_deg(servoY, 1);

			// Create a string and print it to the hex displays
			sprintf(pos, "%3d%3d", servoX, servoY);
			Hex_printString(pos);

			// Update the char buffer to show that the accelerometer is paused
			VGA_Charbuf_String(paused, 2, 24);

		} else {

			// Accelerometer/VGA program

			// Clear the lines for the reading and paused message
			VGA_Charbuf_String(clearLine, 2, 24);
			VGA_Charbuf_String(clearLine, 2, 22);

			// Get the current accelerometer readings
			accel_reading current = Accel_getVals();

			// If the accelerometer reading is valid, print it
			// to the VGA character buffer
			if(current.valid) {
				sprintf(accelerometer, "X:%d   Y:%d   Z:%d", current.X, current.Y, current.Z);
				VGA_Charbuf_String(accelerometer, 2, 22);
			}

		}

		// ~10ms sleep
		usleep(10000);
	}

	return 0;
}

void rainbow_Init() {
	// Initial Conditions
	uint32_t anchor = 20;
	uint32_t xLen = 320-(2*anchor);
	uint32_t yLen = 240-(2*anchor);
	const uint32_t offset = 3;

	// ROYGBV
	LCD_Fill(0);

	// Red fill
	LCD_Fill(0xCC0000);

	// Swap black back to foreground
	LCD_swapFrames();

	// Orange
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0xFF8C00);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Yellow
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0xFFFF00);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Green
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0x32CD32);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Blue
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0x0000FF);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Violet
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0x8A2BE2);

	// Swap frames, rainbow to front, blank to back
	LCD_swapFrames();

	// Reset initial conditions
	anchor = 20;
	xLen = 320-(2*anchor);
	yLen = 240-(2*anchor);

	// Fill and re-swap rainbow to front, violet to back
	LCD_Fill(0x8A2BE2);
	LCD_swapFrames();

	// Blue
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0x0000FF);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Green
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0x32CD32);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Yellow
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0xFFFF00);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Orange
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0xFF8C00);
	anchor += 20;
	xLen = xLen - 40;
	yLen = yLen - 40;

	// Red
	LCD_drawRect(anchor-offset, anchor, xLen, yLen, 0xCC0000);
	LCD_swapFrames();
}

void click_isr(void* context) {

	// Clear to ensure ISR only fires once
	IOWR_ALTERA_AVALON_PIO_EDGE_CAP(JOYIRQ_BASE, 1);

	// Disable interrupts while in here
	IOWR_ALTERA_AVALON_PIO_IRQ_MASK(JOYIRQ_BASE, 0);

	LCD_swapFrames();

	IOWR_ALTERA_AVALON_PIO_IRQ_MASK(JOYIRQ_BASE, 1);

}

void Demo_VGA_Init() {
	const char* helloWorld = "Hello World! This is Evan's CE2820 V10 SoC!";
	const char* info_1 = "Slider switch 0 controls program behavior.";
	const char* info_2 = "0 = Accelerometer/VGA";
	const char* info_3 = "1 = Joystick/Servos";
	const char* initializing = "Please wait, lots of initialization is happening...";

	VGA_Charbuf_Init();
	VGA_Pixelbuf_Init();
	VGA_Pixelbuf_Fill(0);
	VGA_Pixelbuf_Fill(0);
	VGA_Charbuf_String(helloWorld, 2, 2);
	VGA_Charbuf_String(info_1, 2, 4);
	VGA_Charbuf_String(info_2, 2, 6);
	VGA_Charbuf_String(info_3, 2, 8);
	VGA_Charbuf_String(initializing, 2, 10);
}

void Demo_LCD_Init() {
	LCD_Init();
	rainbow_Init();
}

void Demo_Cstick_Init() {
	cstick_Init();
	cstick_Start();
	click_init(click_isr);
}

