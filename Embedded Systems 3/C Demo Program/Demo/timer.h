#ifndef TIMER_STRUCT_ALIVE
#define TIMER_STRUCT_ALIVE 1

#define TIMER_CONT 1<<1
#define TIMER_START 1<<2
#define TIMER_IRQ 1<<0

#endif

typedef struct {
	uint32_t STATUS;
	uint32_t CTRL;
	uint32_t TP_L;
	uint32_t TP_H;
	uint32_t snap_L;
	uint32_t snap_H;
} TIMER32;
