-- Evan Heinrich
-- 3/29/2022
-- CE2820 011 Lab 3
-- CMPLT.vhd
-- Takes in a reference 32-bit value and a value
-- which will be compared to the reference. Outputs
-- logic high if the reference is greater, otherwise low

-- Import Libraries
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;


-- Inputs:
--		REF:	Reference 32-bit value for comparison
--		CMP:	32-bit Number to be compared to REF
--	Outputs:
--		RES:	Result bit, 1 if REF > CMP else 0
entity CMPLT is port (
	REF:	in std_logic_vector(31 downto 0);
	CMP:	in std_logic_vector(31 downto 0);
	RES:	out std_logic
);
end entity CMPLT;

architecture BEHAV of CMPLT is

begin

	COMPARE: process(CMP,REF)
	begin
	
		if unsigned(CMP) < unsigned(REF) then RES <= '1';
		else RES <= '0';
		end if;
	
	end process;

end architecture;