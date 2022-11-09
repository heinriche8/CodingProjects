-- Evan Heinrich
-- 3/30/2022
-- CE2820 011 Lab 4
-- MemMap.vhd
-- Maps a PWM object to the Avalon Memory Map

library ieee;
use ieee.std_logic_1164.all;

entity MemMap is port(
	CLK:				in  std_logic;
	READ: 			in  std_logic;
	WRITE:			in  std_logic;
	READDATA: 		out std_logic_vector(31 downto 0);
	WRITEDATA:		in  std_logic_vector(31 downto 0);
	RST_n: 			in  std_logic;
	PWM_EXPORT: 	out std_logic;
	CLK50_EXPORT: 	in  std_logic
);
end entity MemMap;

architecture STRUCT of MemMap is

	-- The actual PWM component
	component PWM is port(
	CLK: in std_logic;
	RST_n: in std_logic;
	WIDTH: in std_logic_vector(7 downto 0);
	SIG: out std_logic
	);
	end component PWM;
	
	-- Register component
	component REG32 is port(
	EN: 		in  std_logic;
	RST_n: 	in  std_logic;
	CLK: 		in  std_logic;
	D: 		in  std_logic_vector(31 downto 0);
	Q: 		out std_logic_vector(31 downto 0)
	);
	end component REG32;
	
	-- 2-to-1 32-bit bus multiplexer
	component BUSMUX32 is port(
	D0: in  std_logic_vector(31 downto 0);
	D1: in  std_logic_vector(31 downto 0);
	S:  in  std_logic;
	Q:  out std_logic_vector(31 downto 0)
	);
	end component;
	
	signal REG_Q: 	 std_logic_vector(31 downto 0);
	signal MUX_OUT: std_logic_vector(31 downto 0);
	

begin

	-- Map pins of the PWM controller
	unit0: PWM
	port map(
		CLK => CLK50_EXPORT,
		RST_n => RST_n,
		WIDTH => REG_Q(7 downto 0),
		SIG => PWM_EXPORT
	);
	
	-- Map pins of the registers
	unit1: REG32
	port map(
		EN => WRITE,
		RST_n => RST_n,
		CLK => CLK,
		D => WRITEDATA,
		Q => REG_Q
	);
	
	-- Map pins of the bus mux
	unit2: BUSMUX32
	port map(
		D0 => (others => 'Z'),
		D1 => REG_Q,
		S => READ,
		Q => READDATA
	);

end architecture;