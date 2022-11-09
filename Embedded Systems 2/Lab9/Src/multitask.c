// I need the header for the task and task_state typedefs
#include "multitask.h"
#include "registers_new.h"
#include <stdint.h>
#include <stdlib.h>

static task* tasks;
static uint32_t num_tasks;
static uint32_t current_task;
static uint32_t next_task;

void stk_Init() {
	// Systick struct
	volatile SYSTICK* STK = (SYSTICK*)STK_BASE;

	// Enable Systick interrupts
	STK->CTRL |= STK_INT;

	// Set to count according to internal clock
	STK->CTRL |= STK_CLK;

	// Set reload value to 1ms at 16MHz = 16,000
	STK->LOAD = 16000-1;

	STK->CTRL |= STK_EN;
}

void init_tasker(uint32_t total_tasks, uint32_t main_ticks) {
	num_tasks = total_tasks;

	tasks = calloc(total_tasks,sizeof(task));

	tasks[0].state = ACTIVE;
	tasks[0].ticks_starting = main_ticks;
	tasks[0].ticks_remaining = main_ticks;

	current_task = 0;
}

void init_task(uint32_t task_num, uint32_t stacksize, void(*entry_point)(void), uint32_t ticks) {
	tasks[task_num].stack_pointer = (uint32_t*)malloc(stacksize*sizeof(uint32_t));

	tasks[task_num].stack_pointer += stacksize;

	*(--tasks[task_num].stack_pointer) = 0x01000000;
	*(--tasks[task_num].stack_pointer) = (uint32_t)entry_point;
	*(--tasks[task_num].stack_pointer) = 0xFFFFFFFF;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0xFFFFFFF9;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;
	*(--tasks[task_num].stack_pointer) = 0x0;

	tasks[task_num].state = ACTIVE;
	tasks[task_num].ticks_starting = ticks;
	tasks[task_num].ticks_remaining = 0;
}

void tasker_tick() {
	tasks[current_task].ticks_remaining--;

	if(tasks[current_task].ticks_remaining == 0) {
		uint32_t i = 1;
		while(tasks[(next_task=(current_task+i)%num_tasks)].state!=ACTIVE) {
			i++;
		}

		tasks[next_task].ticks_remaining = tasks[next_task].ticks_starting;

		*SCB_ICSR |= 1<<PENDSVSET;
	}
}

// ISR to swap tasks
void PendSV_Handler(void) {
	register uint32_t* stack_pointer asm("sp");
	asm volatile ("push {R4-R11,LR}");

	tasks[current_task].stack_pointer = stack_pointer;
	current_task = next_task;
	stack_pointer = tasks[current_task].stack_pointer;

	asm volatile("pop {R4-R11,LR}\n\t" "BX LR");
}



void SysTick_Handler() {
	volatile SYSTICK* STK = (SYSTICK*)STK_BASE;
	STK->VAL = 0;
	tasker_tick();
}

