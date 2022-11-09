#ifndef MUSIC_API_ALIVE
#define MUSIC_API_ALIVE 1
#include <stdint.h>

typedef struct{
	uint32_t period;
	uint32_t octave;
	uint32_t length;
} note;

void music_Init(void);
void music_Play(const note song[]);
void note_Play(uint32_t period, uint32_t duration);
void music_Background(note song[]);
void music_StopBackground();
// Interrupt that triggers each time TIM5 completes its count
void TIM5_IRQHandler(void);

// 0th octave values
// In period instead of frequencies
// microseconds
// Side note, all of these values will fit into a uint16
// to get other octaves, divide by 2 * octave
// aka LSR  octave
#define C	61162
#define Cs	57736
#define Db	57736
#define D	54495
#define Ds	51413
#define Eb	51413
#define E	48543
#define F	45808
#define Fs	43252
#define Gb	43252
#define G	40816
#define Gs	38520
#define Ab	38520
#define A	36363
#define As	34317
#define Bb	34317
#define B	32393

#endif
