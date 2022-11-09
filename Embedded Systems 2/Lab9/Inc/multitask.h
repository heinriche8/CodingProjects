#ifndef MULTITASK_IS_ALIVE
#define MULTITASK_IS_ALIVE 1

#include <stdint.h>

typedef enum{PAUSED,ACTIVE} task_state;

#define SCB_ICSR (uint32_t*) 0xE000ED04
#define PENDSVSET 28

typedef struct {
	uint32_t* stack_pointer;
	task_state state;
	uint32_t ticks_starting;
	uint32_t ticks_remaining;
} task;

void tasker_tick();
void init_tasker(uint32_t total_tasks, uint32_t main_ticks);
void init_task(uint32_t task_num, uint32_t stack_size, void(*entry_point)(void), uint32_t ticks);


void PendSV_Handler(void) __attribute__((naked));
void stk_Init();
void SysTick_Handler();

#endif
