#include "altera_avalon_i2c.h"
#include "accel.h"

#define ACCEL_ADDR 0x53
#define ACCEL_DFR_ADDR 0x31
#define DFR_DATA 0x0B
#define ACCEL_PWR_ADDR 0x2D
#define PWR_DATA 0x08
#define ACCEL_READ 0x32



static ALT_AVALON_I2C_DEV_t* i2c_dev;
static ALT_AVALON_I2C_STATUS_CODE status;
static uint8_t accel_tx[2] = {0,0};
static int16_t accel_rx[3] = {0,0,0};

/*
 * Attempts to initialize the accelerometer
 * Returns 1 if successful, 0 otherwise
 */
uint8_t Accel_Init() {
	uint8_t successful = 0;

	// Attempt to retrieve access to the I2C controller
	i2c_dev = alt_avalon_i2c_open("/dev/gyro_i2c");

	// Update success flag
	if(i2c_dev != NULL) {
		successful = 1;
	}

	// Specify target address
	alt_avalon_i2c_master_target_set(i2c_dev, ACCEL_ADDR);

	// Load TX buffer
	accel_tx[0] = ACCEL_DFR_ADDR;
	accel_tx[1] = DFR_DATA;

	// Transmit and update flags
	status = alt_avalon_i2c_master_tx(i2c_dev, accel_tx, 2, ALT_AVALON_I2C_NO_INTERRUPTS);
	if(status != ALT_AVALON_I2C_SUCCESS) {
		successful = 0;
	}

	// Reload TX buffer
	accel_tx[0] = ACCEL_PWR_ADDR;
	accel_tx[1] = PWR_DATA;

	// Transmit and update flags
	status = alt_avalon_i2c_master_tx(i2c_dev, accel_tx, 2, ALT_AVALON_I2C_NO_INTERRUPTS);
	if(status != ALT_AVALON_I2C_SUCCESS) {
		successful = 0;
	}

	return successful;
}

/*
 * Returns a struct with the measurements from the accelerometer
 */
accel_reading Accel_getVals() {
	uint8_t valid = 1;

	// Load the read command into the transmit buffer
	accel_tx[0] = ACCEL_READ;

	// Transmit and receive
	status = alt_avalon_i2c_master_tx_rx(i2c_dev, accel_tx, 1, (uint8_t*)accel_rx, 6, ALT_AVALON_I2C_NO_INTERRUPTS);

	// If not successful, fill the values with an invalid flag
	if(status != ALT_AVALON_I2C_SUCCESS) {
		accel_rx[0] = ACCEL_INVALID;
		accel_rx[1] = ACCEL_INVALID;
		accel_rx[2] = ACCEL_INVALID;

		valid = 0;
	}

	accel_reading result = {valid, accel_rx[0], accel_rx[1], accel_rx[2]};

	return result;
}
