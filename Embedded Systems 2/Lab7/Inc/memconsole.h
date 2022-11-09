#ifndef MEMCONSOLE_IS_ALIVE
#define MEMCONSOLE_IS_ALIVE 1

void memdmpDefault(uint8_t* const addr);
void memdmp(uint8_t* const addr, uint32_t len);
void memwrd(const uint32_t* addr);
void wmemwrd(uint32_t* dest, uint32_t contents);
int parseCommand(const char* input);
uint32_t* parseAddress(const char* input);
uint32_t parseArgument(const char* input);
void help(void);

#endif
