#include <stdint.h>

#ifndef SERVO_API_ALIVE
#define SERVO_API_ALIVE 1

void servo_move(uint8_t newpos, uint8_t sel);
void servo_deg(uint8_t degrees, uint8_t sel);
uint8_t servo_read(uint8_t sel);
void servo_disable(uint8_t sel);

#endif
