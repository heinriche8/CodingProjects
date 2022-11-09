-- Evan Heinrich
-- 3/31/2022
-- CE2820 011 Lab 4
-- REG32.vhd
-- 32-bit register to hold values

library ieee;
use ieee.std_logic_1164.all;

entity REG32 is port(
	EN: 		in  std_logic;
	RST_n: 	in  std_logic;
	CLK: 		in  std_logic;
	D: 		in  std_logic_vector(31 downto 0);
	Q: 		out std_logic_vector(31 downto 0)
);
end entity REG32;

architecture BEHAV of REG32 is

	signal SIG_Q: std_logic_vector(31 downto 0) := (others => '0');

begin

	REG: process(EN,CLK,SIG_Q)
	begin
	
		if EN = '1' then
		
			if rising_edge(CLK) then
			
				if RST_n = '1' then SIG_Q <= D;
				else SIG_Q <= (others => '0');
				end if;
			
			end if;
		
		else SIG_Q <= SIG_Q;
		end if;
	
	end process;
	
	Q <= SIG_Q;

end architecture;