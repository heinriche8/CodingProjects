#ifndef PERIOD_MEASURE_ALIVE
#define PERIOD_MEASURE_ALIVE 1

void period_Init(void);
void TIM4_IRQHandler(void);
double period_Measure(void);

#endif
