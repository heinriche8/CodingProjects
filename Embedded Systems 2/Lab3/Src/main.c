/*
	CE2812 Lab 1
	IDE Setup
	Evan Heinrich
	12/3/2021
	Prime number generator
*/



#include <stdio.h>
#include <stdlib.h>
#include "uart_driver.h"
#include "LCD.h"
#include "delay.h"

#define F_CPU 16000000UL

// main
int main(void){
	init_usart2(57600,F_CPU);

	// Initializing the LCD also initializes the delay API
	LCD_Init();

	char test[12] = "Testing\0";
	int testnum = 12345;

	while(1==1) {
		printf("Testing PrintString, expecting 7\n");
		uint32_t reported = LCD_PrintString(test);
		printf("Function reported %d\n", (int)reported);
		delay_ms(2000);

		printf("Testing LCD_MoveCursor by a newline...\n");
		LCD_MoveCursor(1, 0);
		delay_ms(2000);

		printf("Testing PrintNum, expecting 5\n");
		reported = LCD_PrintNum(testnum);
		printf("Function reported %d\n", (int)reported);
		delay_ms(2000);

		printf("Testing Homing...");
		LCD_Home();
		delay_ms(2000);

		printf("\nClearing display in 3 seconds\n");
		delay_ms(3000);
		LCD_Clear();
		delay_ms(2000);
	}

	exit(EXIT_SUCCESS);

	return 0;
}

