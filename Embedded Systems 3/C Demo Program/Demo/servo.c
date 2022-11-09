#include "system.h"
#include <stdint.h>

static volatile uint32_t* PWMX = (uint32_t*) PWMX_BASE;
static volatile uint32_t* PWMY = (uint32_t*) PWMY_BASE;

/**
 * Moves a specified servo to the specified position
 * Inputs:
 * 		uint8_t newpos: the position to move to
 * 		uint8_t sel: X-channel = 0, Y-channel = 1 (else nothing happens)
 * Note:
 * 		The range of accepted values for servo position is [0,200]
 * 		If the input is greater than 200, the servos will disable,
 * 		meaning no holding torque will be applied
 */
void servo_move(uint8_t newpos, uint8_t sel) {

	if(sel == 1) {
		*PWMY = newpos;
	} else if(sel == 0) {
		*PWMX = newpos;
	}

}

/**
 * Moves a specified servo to the specified angle in degrees
 * 0 degrees = max left
 * 90 degrees = centered
 * 180 degrees = max right
 * Note: The conversion factor for degrees to counts is a fractional
 * 		 value so some accuracy is lost due to the float/int conversion
 * 		 Also, if the input is over 181 degrees (float/int conversion error)
 * 		 the selected servo will disable, meaning no holding torque will be
 * 		 applied
 * Params:
 * 		uint8_t degrees: Desired degree position of the selected servo channel
 * 		uint8_t sel: 0 = X-channel, 1 = Y-channel
 *
 */
void servo_deg(uint8_t degrees, uint8_t sel) {
	const float conversion = 1.11;

	uint8_t newval = (uint8_t)(conversion*degrees);

	servo_move(newval,sel);

}

/**
 * Reads the current value from the selected PWM channel
 * Params:
 * 		uint8_t sel: 0 = X-Servo, 1 = Y-Servo (other values will return 0)
 * Returns:
 * 		uint8_t value: The read value of the selected servo channel
 */
uint8_t servo_read(uint8_t sel) {
	uint8_t value = 0;

	if(sel == 0) {
		value = *PWMX;
	} else if(sel == 1) {
		value = *PWMY;
	}

	return value;
}

/**
 * Disables a servo's holding torque by writing 255 to the
 * control register
 * Params:
 * 		uint8_t sel: 0 = X-channel, 1 = Y-channel (others ignored)
 */
void servo_disable(uint8_t sel) {
	if(sel == 0) {
		*PWMX = 255;
	} else if(sel == 1) {
		*PWMY = 255;
	}
}
