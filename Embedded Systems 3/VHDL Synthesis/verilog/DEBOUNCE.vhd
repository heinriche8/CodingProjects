-- Generic D flip flop for debouncing

library ieee;
use ieee.std_logic_1164.all;

entity DEBOUNCE is port (
	D: in std_logic;
	CLK: in std_logic;
	Q: out std_logic
);
end entity;

architecture BEHAV of DEBOUNCE is

	signal NEWCLK: std_logic := '0';
	
	-- 50MHz / 5kHz = 10k
	signal COUNT: integer range 0 to 800000 := 0;

begin

	debouncer: process(NEWCLK)
	begin
		if rising_edge(NEWCLK) then
			Q <= D;
		end if;
	end process;
	
	clkdiv: process(CLK)
	begin
	
		if rising_edge(CLK) then
			if COUNT < 800000 then COUNT <= COUNT + 1;
			else 
				COUNT <= 0;
				NEWCLK <= not NEWCLK;
			end if;
		end if;
	end process;

end architecture;