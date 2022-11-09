-- Evan Heinrich
-- 3/29/2022
-- CE2820 011 Lab 3
-- ALGO.vhd
-- Takes a 8-bit input and converts to a
-- 32-bit value representing a pulse width.

-- Equation is as follows:
-- out = (250 * in) + 50,000
-- Creates a minimum value of 50,000 clock cycles, aka 1ms
-- and a maximum value of 100,000 clock cycles, aka 2ms
-- If the input is greater than 200, output = 0

-- Import Libraries
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

-- Inputs:
--		INPUT:	8-bit input for position
--	Outputs:
--		OUTPUT:	32-bit number representing number of clock 
--					cycles for a PWM signal to remain high
entity ALGO is port(
	INPUT: in std_logic_vector(7 downto 0);
	OUTPUT: out std_logic_vector(31 downto 0)
);
end entity;

architecture BEHAV of ALGO is

	-- Unsigned values to integer math
	signal U_INPUT: unsigned(7 downto 0) := (others => '0');
	signal U_OUTPUT: unsigned(31 downto 0) := (others => '0');

begin

	-- Convert input to an unsigned value
	U_INPUT <= unsigned(INPUT);
	
	process(U_INPUT)
	begin
		
		-- If input > 200, output = 0
		if U_INPUT > to_unsigned(200,8) then U_OUTPUT <= (others => '0');
		
		-- If input <= 200, output = (250 * in) + 50,000
		else U_OUTPUT <= (U_INPUT * to_unsigned(250,8)) + (to_unsigned(50000,32));
		end if;
	
	end process;
	
	-- Push result
	OUTPUT <= std_logic_vector(U_OUTPUT);

end architecture;