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

// RCC
#define RCC_APB1ENR (volatile uint32_t*) 0x40023840
#define RCC_AHB1ENR (volatile uint32_t*) 0x40023830

// Delay funct timer
#define TIM2_CR1 	(volatile uint32_t*) 0x40000000
#define TIM2_PSC 	(volatile uint32_t*) 0x40000028
#define TIM2_CNT 	(volatile uint32_t*) 0x40000024
#define TIM2_EGR 	(volatile uint32_t*) 0x40000014
#define TIM2_SR 	(volatile uint32_t*) 0x40000010
#define TIM2_ARR 	(volatile uint32_t*) 0x4000002C

// GPIOB
#define GPIOB_MODER (volatile uint32_t*) 0x40020400
#define GPIOB_ODR 	(volatile uint32_t*) 0x40020414
#define GPIOB_BSRR 	(volatile uint32_t*) 0x40020418
#define GPIOB_AFRL	(volatile uint32_t*) 0x40020420
#define RCC_GPIOBEN	(uint32_t)			 1<<1
#define GPIO_ALTFUN (uint32_t)			 0b10
#define PB4_PIEZO	(uint32_t)			 2<<16

// GPIOA
#define GPIOA_MODER	(volatile uint32_t*) 0x40020000
#define GPIOA_ODR	(volatile uint32_t*) 0x40020014
#define GPIOA_BSRR	(volatile uint32_t*) 0x40020018

// GPIOC
#define GPIOC_MODER	(volatile uint32_t*) 0x40020800
#define GPIOC_ODR	(volatile uint32_t*) 0x40020814
#define GPIOC_BSRR	(volatile uint32_t*) 0x40020818

// Tone timer
#define TIM3_CR1	(volatile uint32_t*) 0x40000400
#define	TIM3_ARR	(volatile uint32_t*) 0x4000042C
#define TIM3_PSC	(volatile uint32_t*) 0x40000428
#define TIM3_CCMR1	(volatile uint32_t*) 0x40000418
#define TIM3_CCR	(volatile uint32_t*) 0x40000434
#define TIM3_CCER	(volatile uint32_t*) 0x40000420
#define TIM3_EGR	(volatile uint32_t*) 0x40000414
#define TIM3_SR		(volatile uint32_t*) 0x40000410
#define TIM3_CCMR	(volatile uint32_t*) 0x40000418
#define RCC_TIM3EN	(uint32_t)			 1<<1
#define OC1M_PWM	(uint32_t)			 0b110<<4
#define OC1M_PE		(uint32_t)			 1<<3
#define CCER_CC1E	(uint32_t)			 1<<0

#endif
