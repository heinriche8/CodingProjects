/*
	CE2812 Lab 2
	Knight Rider LEDs in C
	Evan Heinrich
	12/10/2021
*/


#include "registers.h"
#include <stdio.h>
#include <stdlib.h>
#include "uart_driver.h"
#include "LED.h"
#include "delay.h"

#define F_CPU 16000000UL
#define MOTION 50

// main
int main(void){
	init_usart2(57600,F_CPU);

	delay_Init();
	LED_Init();

	// Infinite loop
	while(1==1) {
		int light = 1;

		for(int i = 0; i < 10; i++) {
			int shifted = light << i;
			LED_PrintNum(shifted);
			delay_ms(MOTION);
		}

		light = 1 << 9;

		for(int i = 0; i < 10; i++) {
			int shifted = light >> i;
			LED_PrintNum(shifted);
			delay_ms(MOTION);
		}
	}

	exit(EXIT_SUCCESS);

	return 0;
}

