-- Evan Heinrich
-- 3/31/2022
-- CE2820 011 Lab 4
-- BUSMUX32.vhd
-- 32-bit Bus Mux

library ieee;
use ieee.std_logic_1164.all;

entity BUSMUX32 is port(
	D0: in  std_logic_vector(31 downto 0);
	D1: in  std_logic_vector(31 downto 0);
	S:  in  std_logic;
	Q:  out std_logic_vector(31 downto 0)
);
end entity;

architecture BEHAV of BUSMUX32 is

begin

	with S select Q <=
		D0 when '0',
		D1 when others;

end architecture;