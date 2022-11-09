#include <stdint.h>
typedef struct{
	uint8_t valid;
	int16_t X;
	int16_t Y;
	int16_t Z;
} accel_reading;

#define ACCEL_INVALID -32768

#ifndef ACCEL_API_ALIVE
#define ACCEL_API_ALIVE 1

uint8_t Accel_Init();
accel_reading Accel_getVals();

#endif
