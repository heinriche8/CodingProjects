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

#define RCC_BASE 0x40023800
#define RCC_GPIOAEN (uint32_t)	1<<0
#define RCC_GPIOBEN	(uint32_t)	1<<1
#define RCC_TIM2EN	(uint32_t)	1<<0
#define RCC_TIM3EN	(uint32_t)	1<<1
#define RCC_TIM4EN	(uint32_t)	1<<2
#define RCC_TIM5EN	(uint32_t) 	1<<3
#define RCC_TIM6EN	(uint32_t)	1<<4
#define RCC_DACEN	(uint32_t)	1<<29

#define NVIC_BASE 0xE000E100

// These timers might have extra registers at the end
#define TIM11_BASE	0x40014800
#define TIM10_BASE	0x40014400
#define TIM9_BASE	0x40014000
#define TIM8_BASE	0x40010400
#define TIM6_BASE	0x40001000
#define TIM7_BASE	0x40001400
#define TIM1_BASE	0x40010000

// The actual general purpose timers that the struct was based on
#define TIM2_BASE	0x40000000	// Used by busy delay
#define TIM3_BASE	0x40000400	// Connected to PB4's Piezo
#define TIM4_BASE	0x40000800
#define TIM5_BASE	0x40000C00	// Used by background timer
#define OC1M_PWM	(uint32_t)	0b110<<4
#define OC1M_PE		(uint32_t)	1<<3
#define CCER_CC1E	(uint32_t)	1<<0
#define TIM_OPM		(uint32_t)	1<<3
#define TIM_DIR		(uint32_t)	1<<4
#define TIM_CEN		(uint32_t)	1<<0
#define TIM_UIE		(uint32_t)	1<<0

#define GPIOA_BASE 0x40020000
#define GPIOB_BASE 0x40020400
#define GPIOC_BASE 0x40020800
#define GPIOD_BASE 0x40020C00
#define GPIOE_BASE 0x40021000
#define GPIOF_BASE 0x40021800
#define GPIOG_BASE 0x40021800
#define GPIOH_BASE 0x40021C00
#define GPIO_ALTFUN (uint32_t)	0b10
#define GPIO_ANALOG (uint32_t)	0b11
#define PB4_PIEZO	(uint32_t)	2<<16

#define DAC_BASE	0x40007400
#define DAC_TEN1	(uint32_t)	1<<2
#define DAC_CH1EN	(uint32_t)	1<<0
#define DAC_SWTGR	(uint32_t)	0b111

// Timer structure
// To instantiate properly, create a TIMER* set to the base
// 		address of the desired timer
typedef struct {
	// Register Map Available in the Reference Manual
	// 17.4.21
	uint32_t CR1;	// 0x00
	uint32_t CR2;	// 0x04
	uint32_t SMCR;	// 0x08
	uint32_t DIER;	// 0x0C
	uint32_t SR;	// 0x10
	uint32_t EGR;	// 0x14
	uint32_t CCMR1;	// 0x18
	uint32_t CCMR2;	// 0x1C
	uint32_t CCER;	// 0x20
	uint32_t CNT;	// 0x24
	uint32_t PSC;	// 0x28
	uint32_t ARR;	// 0x2C
	uint32_t RESERVED_01;	// 0x30
	uint32_t CCR1;	// 0x34
	uint32_t CCR2;	// 0x38
	uint32_t CCR3;	// 0x3C
	uint32_t CCR4;	// 0x40
	uint32_t RESERVED_02;	// 0x44
	uint32_t DCR;	// 0x48
	uint32_t DMAR;	// 0x4C
	uint32_t OR;	// 0x50

} TIMER;

typedef struct {
	uint32_t CR1;			// 0x00
	uint32_t CR2;			// 0x04
	uint32_t RESERVED_01;	// 0x08
	uint32_t DIER;			// 0x0C
	uint32_t SR;			// 0x10
	uint32_t EGR;			// 0x14
	uint32_t RESERVED[3];	// 0x18 - 0x20
	uint32_t CNT;			// 0x24
	uint32_t PSC;			// 0x28
	uint32_t ARR;			// 0x2C
} TIMER_BASIC;

// GPIO structure
// To instantiate properly, create a GPIO* set to the base
// 		address of the desired GPIO port
typedef struct {
	// Register Map Availible in the Reference Manual
	// 7.4.11
	uint32_t MODER;		// 0x00
	uint32_t OTYPER;	// 0x04
	uint32_t OSPEEDR;	// 0x08
	uint32_t PUPDR;		// 0x0C
	uint32_t IDR;		// 0x10
	uint32_t ODR;		// 0x14
	uint32_t BSRR;		// 0x18
	uint32_t LCKR;		// 0x1C
	uint32_t AFRL;		// 0x20
	uint32_t AFRH;		// 0x24
} GPIO;

// RCC structure
// To instantiate properly, create a RCC* set to the RCC base address
// In class, all we really use are AHB1ENR and APB1ENR, so that's all for now
typedef struct {
	uint32_t RESERVED01[12];// 0x00 to 0x2C
	uint32_t AHB1ENR;		// 0x30
	uint32_t AHB2ENR;		// 0x34
	uint32_t AHB3ENR;		// 0x38
	uint32_t RESERVED02;	// 0x3C
	uint32_t APB1ENR;		// 0x40

} RCC;

typedef struct {
	// NVIC Register map can be found in the programming manual
	//	4.3.11

	// Interrupt set enable
	uint32_t ISER[3];	// 0x000 to 0x008

	uint32_t RESERVED_01[30];

	// Interrupt clear enable
	uint32_t ICER[3];	// 0x080 to 0x088

	uint32_t RESERVED_02[30];

	// Interrupt set pending
	uint32_t ISPR[3]; 	// 0x100 to 0x108

	uint32_t RESERVED_03[30];

	// Interrupt clear pending
	uint32_t ICPR[3];	// 0x180 to 0x188

	uint32_t RESERVED_04[798];

	// Software trigger interrupts
	uint32_t STIR;		// 0xE00
} NVIC;

typedef struct {
	uint32_t CR;		// 0x00
	uint32_t SW_TRIGR;	// 0x04
	uint32_t CH1_R12;	// 0x08, channel 1 right aligned 12 bit
	uint32_t CH1_L12;	// 0x0C, channel 1 left aligned 12 bit
	uint32_t CH1_8;		// 0x10, channel 1 8 bit
	uint32_t CH2_R12;	// 0x14, channel 2 right aligned 12 bit
	uint32_t CH2_L12;	// 0x18, channel 2 left aligned 12 bit
	uint32_t CH2_8;		// 0x1C, channel 2 8 bit
	uint32_t DUAL_R12;	// 0x20, dual channel right aligned 12 bit
	uint32_t DUAL_L12;	// 0x24, dual channel left aligned 12 bit
	uint32_t DUAL_8;	// 0x28, dual channel 8 bit
	uint32_t CH1_DOR;	// 0x2C, channel 1 output register
	uint32_t CH2_DOR;	// 0x30, channel 2 output register
	uint32_t SR;		// 0x34
} DAC;

#endif
