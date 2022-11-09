################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (9-2020-q2-update)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../Src/delay.c \
../Src/main.c \
../Src/memconsole.c \
../Src/music.c \
../Src/period.c \
../Src/syscalls.c \
../Src/sysmem.c \
../Src/uart_driver.c \
../Src/waveforms.c 

OBJS += \
./Src/delay.o \
./Src/main.o \
./Src/memconsole.o \
./Src/music.o \
./Src/period.o \
./Src/syscalls.o \
./Src/sysmem.o \
./Src/uart_driver.o \
./Src/waveforms.o 

C_DEPS += \
./Src/delay.d \
./Src/main.d \
./Src/memconsole.d \
./Src/music.d \
./Src/period.d \
./Src/syscalls.d \
./Src/sysmem.d \
./Src/uart_driver.d \
./Src/waveforms.d 


# Each subdirectory must supply rules for building sources it contributes
Src/%.o: ../Src/%.c Src/subdir.mk
	arm-none-eabi-gcc "$<" -mcpu=cortex-m4 -std=gnu11 -g3 -DDEBUG -DSTM32 -DSTM32F4 -DSTM32F446RETx -DNUCLEO_F446RE -c -I../Inc -O0 -ffunction-sections -fdata-sections -Wall -fstack-usage -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=soft -mthumb -o "$@"

