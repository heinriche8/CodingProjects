/*
	CE2812 Lab 5
	Songs
	Evan Heinrich
	1/14/2022
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

			case 4:
				if(arg1 != NULL) {
					if(strcmp(arg1, "doom\n") == 0) {
						for(int i = 0; i < 2; i++) {
							music_Play(atDoomsGate, doomTempo);
						}
					} else if(strcmp(arg1, "zelda\n") == 0) {
						music_Play(zelda, zeldaTempo);
					} else {
						printf("Invalid song name\n");
					}
				} else {
					printf("No song provided\n");
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
