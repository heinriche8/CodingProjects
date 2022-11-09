-- Evan Heinrich
-- 3/29/2022
-- CE2820 011 Lab 3
-- PWM.vhd
-- Combines all of the components created for the
-- PWM project into one functional PWM controller

-- Import Libraries
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

-- Inputs:
--		CLK: 	 Clock input, designed for 50MHz
--		RST_n: Active-low SYNCHRONUS reset
--		WIDTH: 8-bit value to determine PWM width
-- Outputs:
--		SIG:	 Output PWM signal
entity PWM is port(
	CLK: in std_logic;
	RST_n: in std_logic;
	WIDTH: in std_logic_vector(7 downto 0);
	SIG: out std_logic
);
end entity PWM;

architecture STRUCT of PWM is 

-- Import counter component
component COUNT is port(
	CLK: in std_logic;
	RST_n: in std_logic;
	VAL: out std_logic_vector(31 downto 0)
);
end component;

-- Import the input Algorithm component
component ALGO is port(
	INPUT: in std_logic_vector(7 downto 0);
	OUTPUT: out std_logic_vector(31 downto 0)
);
end component;

-- Import the comparison component
component CMPLT is port (
	REF:	in std_logic_vector(31 downto 0);
	CMP:	in std_logic_vector(31 downto 0);
	RES:	out std_logic
);
end component CMPLT;

-- Only two signals needed, one from the counter
-- to the comparison's CMP input. Other from the
-- algorithm's output to the comparison's REF input
signal CNTOUT: std_logic_vector(31 downto 0);
signal ALGOUT:	std_logic_vector(31 downto 0);

begin

-- Import the counter and map it
unit0: COUNT
port map(
	CLK => CLK,
	RST_n => RST_n,
	VAL => CNTOUT
);

-- Import the algorithm and map it
unit1: ALGO
port map(
	INPUT => WIDTH,
	OUTPUT => ALGOUT
);

-- Import the comparison and map it
unit2: CMPLT
port map(
	REF => ALGOUT,
	CMP => CNTOUT,
	RES => SIG
);

end architecture;