#ifndef MUSIC_API_ALIVE
#define MUSIC_API_ALIVE 1
#include <stdint.h>
// Ok for future reference,
// length:
//		1 = 32nd note, shortest
//		2 = 16th note
//		3 = 8th note
//		4 = qtr note
//		5 = half note
//		6 = full note, longest
typedef struct{
	uint32_t period;
	uint32_t octave;
	uint32_t length;
} note;

void music_Init(void);
void music_Play(const note song[], int tempoScale);
void note_Play(uint32_t period, uint32_t duration);

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

// Rip and tear until it is done
static const int doomTempo = 63300;
static const note atDoomsGate[] = {
		{F, 2, 3},
		{F, 2, 3},
		{C, 5, 3},
		{F, 2, 3},
		{F, 2, 3},
		{B, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{A, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{Fs, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{G, 4, 3},
		{A, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{C, 5, 3},
		{F, 2, 3},
		{F, 2, 3},
		{B, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{G, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{Fs, 4, 3},
		{Fs, 4, 5},

		{F, 2, 3},
		{F, 2, 3},
		{C, 5, 3},
		{F, 2, 3},
		{F, 2, 3},
		{B, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{A, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{Fs, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{G, 4, 3},
		{A, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{C, 5, 3},
		{F, 2, 3},
		{F, 2, 3},
		{B, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{G, 4, 3},
		{F, 2, 3},
		{F, 2, 3},
		{Fs, 4, 3},
		{Fs, 4, 6},
		{0, 0, 0}
};

static const int zeldaTempo = 60000;
static const note zelda[] = {
		{A, 4, 5},
		{0, 1, 4},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 4},
		{B, 4, 3},
		{A, 4, 4},
		{0, 1, 4},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 4},
		{B, 4, 3},
		{A, 4, 4},
		{0, 1, 4},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{A, 4, 3},
		{B, 4, 3},
		{E, 4, 2},
		{E, 4, 2},
		{E, 4, 3},
		{E, 4, 2},
		{E, 4, 2},
		{E, 4, 3},
		{E, 4, 2},
		{E, 4, 2},
		{E, 4, 3},
		{E, 4, 3},
		{A, 4, 4},
		{E, 4, 4},
		{E, 4, 3},
		{A, 4, 3},
		{A, 4, 2},
		{B, 4, 2},
		{C, 5, 2},
		{D, 5, 2},
		{E, 5, 5},
		{0, 1, 3},
		{D, 5, 3},
		{E, 5, 3},
		{F, 5, 3},
		{G, 5, 3},
		{B, 6, 5},
		{D, 6, 3},
		{B, 6, 3},
		{G, 5, 3},
		{F, 5, 5},
		{G, 5, 2},
		{0, 1, 2},
		{F, 5, 2},
		{E, 5, 5},
		{E, 5, 4},
		{D, 5, 3},
		{D, 5, 2},
		{E, 5, 2},
		{F, 5, 5},
		{E, 5, 3},
		{D, 5, 3},
		{D, 5, 3},
		{C, 5, 2},
		{D, 5, 2},
		{E, 5, 5},
		{0, 0, 0}
};

#endif
