-- Evan Heinrich
-- 3/29/2022
-- CE2820 011 Lab 3
-- COUNT.vhd
-- Provides a 32-bit counter for the PWM controller
-- Coded to count from 0 to 1,000,000 running off
-- the DE-10 Lite's 50MHz clock, meaning to count the
-- full 1,000,000 cycles takes approx. 20ms

-- Import Libraries
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

-- Inputs:
--		CLK:	 Clock for counter
--		RST_n: SYNCHRONUS active-low reset
--	Outputs:
--		VAL:	32-bit value of the counter
entity COUNT is port(
	CLK: in std_logic;
	RST_n: in std_logic;
	VAL: out std_logic_vector(31 downto 0)
);
end entity;

architecture BEHAV of COUNT is

	-- Unsigned value to avoid possible overflow/underflow problems
	-- Syntax was kinda wierd to get initial value of 0
	signal CURVAL: unsigned(31 downto 0) := (others => '0');

begin

	process(CLK,RST_n,CURVAL)
	begin

		-- Synchronus aspect, everything operates inside of a
		-- clock edge
		if rising_edge(CLK) then
		
		-- If the reset signal is high, increment every clock edge
		if RST_n = '1' then CURVAL <= CURVAL + to_unsigned(1,32);
		
		-- Otherwise reset the current count
		else CURVAL <= to_unsigned(0,32);
		end if;
			
		-- If the counter saturates, reset to 0
		if CURVAL > to_unsigned(999999,32) then CURVAL <= to_unsigned(0,32);
		end if;
			
		-- Push the value 
		VAL <= std_logic_vector(CURVAL);
		
		end if;
		
	end process;
	
	

end architecture;