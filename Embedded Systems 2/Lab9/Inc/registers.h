/*
	CE2812
	Register addresses for our NODE F446RE boards
	Evan Heinrich
	NOTE: Running list of all registers used, updated as new
	peripherals are used
*/

#ifndef REG_LIST_ALIVE
#define REG_LIST_ALIVE 1

#define RCC_APB1ENR 0x40023840
#define RCC_AHB1ENR 0x40023830
#define TIM2_CR1 0x40000000
#define TIM2_PSC 0x40000028
#define TIM2_CNT 0x40000024
#define TIM2_EGR 0x40000014
#define TIM2_SR 0x40000010
#define TIM2_ARR 0x4000002C
#define GPIOB_MODER 0x40020400
#define GPIOB_ODR 0x40020414
#define GPIOB_BSRR 0x40020418

#endif
