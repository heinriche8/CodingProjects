#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "uart_driver.h"
#include "delay.h"
#include "music.h"
#include "memconsole.h"
#include "period.h"
#include "music.h"
#include "waveforms.h"
#include "console.h"
#include "multitask.h"
#include "LED.h"

#define F_CPU 16000000UL
#define MOTION 50

void krl() {
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
}

int main(void) {
	init_usart2(115200,F_CPU);

	delay_Init();

	music_Init();

	//period_Init();

	wave_Init();

	LED_Init();

	init_tasker(2, 10);
	init_task(1, 1000, console, 5);

	stk_Init();

	krl();

	exit(EXIT_SUCCESS);
	return 0;
}


