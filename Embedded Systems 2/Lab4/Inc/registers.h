/*
	CE2812
	Register addresses for our NODE F446RE boards
	Evan Heinrich
	NOTE: Running list of all registers used, updated as new
	peripherals are used
*/

#ifndef REG_LIST_ALIVE
#define REG_LIST_ALIVE 1
#include <stdint.h>

#define RCC_APB1ENR (volatile uint32_t*) 0x40023840
#define RCC_AHB1ENR (volatile uint32_t*) 0x40023830
#define TIM2_CR1 	(volatile uint32_t*) 0x40000000
#define TIM2_PSC 	(volatile uint32_t*) 0x40000028
#define TIM2_CNT 	(volatile uint32_t*) 0x40000024
#define TIM2_EGR 	(volatile uint32_t*) 0x40000014
#define TIM2_SR 	(volatile uint32_t*) 0x40000010
#define TIM2_ARR 	(volatile uint32_t*) 0x4000002C
#define GPIOB_MODER (volatile uint32_t*) 0x40020400
#define GPIOB_ODR 	(volatile uint32_t*) 0x40020414
#define GPIOB_BSRR 	(volatile uint32_t*) 0x40020418
#define GPIOA_MODER	(volatile uint32_t*) 0x40020000
#define GPIOA_ODR	(volatile uint32_t*) 0x40020014
#define GPIOA_BSRR	(volatile uint32_t*) 0x40020018
#define GPIOC_MODER	(volatile uint32_t*) 0x40020800
#define GPIOC_ODR	(volatile uint32_t*) 0x40020814
#define GPIOC_BSRR	(volatile uint32_t*) 0x40020818

#endif
