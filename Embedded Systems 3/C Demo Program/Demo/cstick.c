/**
 * Evan Heinrich
 * 4/6/2022
 * CE2820 Milestone 5
 * Joystick API
 */

#include "system.h"

#include "altera_avalon_pio_regs.h"
#include "sys/alt_irq.h"

#include <stdint.h>
#include <stdlib.h>

// A couple values that will be used when configuring
#define CMD_RUN 1<<0
#define IER_OFF 1<<0

// Read-Only pointers
static volatile const uint32_t* ADC_SAMPLE0 = (uint32_t*)0xBEEF1200;
static volatile const uint32_t* ADC_SAMPLE1 = (uint32_t*)0xBEEF1204;

// R/W pointers
static volatile uint32_t* ADC_IER = (uint32_t*)0xBEEF1240;
static volatile uint32_t* ADC_CMD = (uint32_t*)0xBEEF1000;

/*
 * Initializes the required values for our ADC
 * to operate appropriately.
 */
void cstick_Init() {
	*ADC_IER &= ~(IER_OFF);
}

/*
 * Sets the Run bit in the ADC control register, which
 * starts the ADC sampling
 */
void cstick_Start() {
	*ADC_CMD |= CMD_RUN;
}

/*
 * Clears the Run bit in the ADC control register, which
 * stops the ADC from sampling
 */
void cstick_Stop() {
	*ADC_CMD &= ~(CMD_RUN);
}

/*
 * Returns the value stored in the X-value register
 */
uint32_t cstick_getx() {
	return *ADC_SAMPLE0;
}

/*
 * Returns the value stored in the Y-value register
 */
uint32_t cstick_gety() {
	return *ADC_SAMPLE1;
}

/*
 * Enables and initializes the interrupt associated with
 * the joystick clicker
 * Params: ISR is a function pointer to be executed when an IRQ occurs
 */
void click_init(void* ISR) {

	// Unmask Interrupt
	IOWR_ALTERA_AVALON_PIO_IRQ_MASK(JOYIRQ_BASE, 1);

	// Set edge capture
	IOWR_ALTERA_AVALON_PIO_EDGE_CAP(JOYIRQ_BASE, 1);

	/*
	 * This actually initializes the IRQ.
	 * Arg1: IRQ controller ID that the target IRQ is connected to
	 * Arg2: IRQ ID for the target IRQ
	 * Arg3: ISR function pointer to be executed when an IRQ is processed
	 * Arg4/5: Unused for this implementation
	 */
	alt_ic_isr_register(JOYIRQ_IRQ_INTERRUPT_CONTROLLER_ID, JOYIRQ_IRQ, ISR, NULL, NULL);
}
