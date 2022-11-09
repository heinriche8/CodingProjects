################################################################################
# Automatically-generated file. Do not edit!
# Toolchain: GNU Tools for STM32 (9-2020-q2-update)
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
S_UPPER_SRCS += \
../Src/ASCII.S \
../Src/LCD_Control.S \
../Src/delay.S \
../Src/keypad.S \
../Src/main.S 

OBJS += \
./Src/ASCII.o \
./Src/LCD_Control.o \
./Src/delay.o \
./Src/keypad.o \
./Src/main.o 

S_UPPER_DEPS += \
./Src/ASCII.d \
./Src/LCD_Control.d \
./Src/delay.d \
./Src/keypad.d \
./Src/main.d 


# Each subdirectory must supply rules for building sources it contributes
Src/%.o: ../Src/%.S Src/subdir.mk
	arm-none-eabi-gcc -mcpu=cortex-m4 -g3 -DDEBUG -c -x assembler-with-cpp -MMD -MP -MF"$(@:%.o=%.d)" -MT"$@" --specs=nano.specs -mfpu=fpv4-sp-d16 -mfloat-abi=hard -mthumb -o "$@" "$<"

