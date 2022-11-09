/*
	CE2812 Lab 5
	Songs
	Evan Heinrich
	1/14/2022
*/

/*
 * My personal experience with this lab
 *
 * This lab wasn't too terribly difficult to implement since we had already
 * worked with interrupts in the previous quarter. The hardest parts for me
 * personally were debugging the ISR that I wrote. I met with Dr. Rothe during
 * one of his office hours, and it turns out that the main issue I was facing
 * was due to not clearing the status register for timer 5, causing note timing
 * issues. Besides this, I also had messed up the RCC struct. I had thought I
 * incorrectly laid out the register map in the struct, but it turns out that I
 * just had the incorrect base address for the RCC.
 */

#include <music.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "uart_driver.h"
#include "delay.h"
#include "music.h"
#include "memconsole.h"

#define F_CPU 16000000UL

// Rip and tear until it is done
#define doomTempo 1500000	// Technically this track should be 240bpm but this sounds right
static note atDoomsGate[] = {
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{C, 5, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{B, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{A, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{Fs, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{G, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{A, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{C, 5, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{B, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{G, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{Fs, 4, (doomTempo>>2)+(doomTempo>>3)},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{C, 5, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{B, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{A, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{Fs, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{G, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{A, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{C, 5, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{B, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{G, 4, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{F, 2, doomTempo>>3},
		{0, 0, doomTempo>>5},
		{Fs, 4, (doomTempo>>2)+(doomTempo>>3)},
		{0, 0, 0}
};

// "130bpm"
#define zeldaTempo 800000		// This totally isn't 130bpm but it sounds right
#define betweenNotes 46000
static note zelda[] = {
		{A, 4, zeldaTempo>>1},
		{0, 1, zeldaTempo>>2},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>2},
		{0, 0, betweenNotes},
		{B, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>2},
		{0, 1, zeldaTempo>>2},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>2},
		{0, 0, betweenNotes},
		{B, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>2},
		{0, 1, zeldaTempo>>2},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{B, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>2},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>2},
		{0, 0, betweenNotes},
		{E, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{A, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{B, 4, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{C, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>1},
		{0, 1, zeldaTempo>>3},
		{D, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{F, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{G, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{B, 6, zeldaTempo>>5},
		{0, 0, betweenNotes},
		{D, 6, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{B, 6, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{G, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{F, 5, zeldaTempo>>1},
		{0, 0, betweenNotes},
		{G, 5, zeldaTempo>>4},
		{0, 1, zeldaTempo>>4},
		{F, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>1},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>2},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{F, 5, zeldaTempo>>1},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>3},
		{0, 0, betweenNotes},
		{C, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{D, 5, zeldaTempo>>4},
		{0, 0, betweenNotes},
		{E, 5, zeldaTempo>>1},
		{0, 0, 0}
};

// main
int main(void){
	init_usart2(115200,F_CPU);

	delay_Init();

	music_Init();

	// Blank string for input
	char input[30] = "";

	// Address to interact with
	uint32_t* address = 0;

	// Command variable
	int command = -1;

	// Last argument, either length to read or value to write
	uint32_t argument = 0;

	// Welcome message
	printf("Evan's Memory Management Console\n\r");
	printf("Type \'?\' for help\n\r");

	// Infinite loop for program
	while(1==1) {
		// Prompt
		printf("> ");
		fgets(input, 29, stdin);
		// First token, determines command
		char* token = strtok(input, " ");

		// Second token, determines address
		char* arg1 = strtok(NULL, " ");

		// Third token, optional third argument, required for wmw, optional for dm
		char* arg2 = strtok(NULL, " ");

		// If there is an extracted command
		if(token != NULL) {
			// Attempt to parse the command
			command = parseCommand(token);

			// Attempt to parse address
			if(arg1 != NULL) {
				address = parseAddress(arg1);
			}

			// Attempt to parse second argument
			if(arg2 != NULL) {
				argument = parseArgument(arg2);
			}

			// Switch case for reported commands
			switch (command) {
			// Help command
			case 0:
				help();
				break;

			// Dump memory command
			case 1:
				if(arg1 != NULL) {
					if(arg2 == NULL) {
						memdmpDefault((uint8_t*)address);
					} else {
						memdmp((uint8_t*)address, argument);
					}
				} else {
					printf("No address provided\n\r");
				}
				break;

			// Read word command
			case 2:
				if(arg1 != NULL) {
					memwrd(address);
				} else {
					printf("No address provided\n\r");
				}
				break;

			// Write word command
			case 3:
				if(arg1 != NULL) {
					if(arg2 != NULL) {
						wmemwrd(address, argument);
					} else {
						printf("No value to write provided\n\r");
					}
				} else {
					printf("No address provided\n\r");
				}
				break;

			// Music command
			case 4:
				// Determine song to be played
				if(strcmp(arg1, "doom") == 0 || strcmp(arg1, "doom\n") == 0) {

					// Play background/foreground accordingly
					if(strcmp(arg2, "background\n") == 0) {
						music_Background(atDoomsGate);
					} else {
						music_Play(atDoomsGate);
					}

				} else if(strcmp(arg1, "zelda") == 0 || strcmp(arg1, "zelda\n") == 0) {

					// Play background/foreground accordingly
					if(strcmp(arg2, "background\n") == 0) {
						music_Background(zelda);
					} else {
						music_Play(zelda);
					}

				} else {
					printf("Invalid song\n");
				}
				break;

			default:
				printf("Invalid command\n\r");
			}

		} else {
			printf("No input\n\r");
		}

		// fgets again because it will read the newline from previous entry
		fgets(input, 29, stdin);

		// Clear the input string
		memset(input, 0, strlen(input));

		}

	exit(EXIT_SUCCESS);
	return 0;
}
