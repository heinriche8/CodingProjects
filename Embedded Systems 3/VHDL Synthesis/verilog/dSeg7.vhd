-- ********************************************************
-- * project:     dseg7
-- * filename:    dseg7.vhd
-- * author:      heinriche@msoe.edu
-- * date:        MSOE Spring Quarter 2022
-- * provides:    Hardware decoder for ASCII to dseg7
-- * approach:    with/select
-- ********************************************************

-- use library packages 
-- std_logic_1164: 9-valued logic signal voltages 
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity dseg7 is port(
	ASCII:	in 	std_logic_vector(7 downto 0);
	DATA:		out	std_logic_vector(7 downto 0)
);
end entity dseg7;

architecture DATAFLOW of dseg7 is
begin
	with ASCII select DATA <=
		not(x"77") when x"61",	-- ascii a
		not(x"7C") when x"62",	-- ascii b
		not(x"58") when x"63",	-- ascii c
		not(x"5E") when x"64",	-- ascii d
		not(x"79") when x"65",	-- ascii e
		not(x"71") when x"66",	-- ascii f
		not(x"3D") when x"67",	-- ascii g
		not(x"74") when x"68",	-- ascii h
		not(x"04") when x"69",	-- ascii i
		not(x"1E") when x"6A",	-- ascii j
		not(x"75") when x"6B",	-- ascii k
		not(x"38") when x"6C",	-- ascii l
		not(x"37") when x"6D",	-- ascii m
		not(x"54") when x"6E",	-- ascii n
		not(x"5C") when x"6F",	-- ascii o
		not(x"73") when x"70",	-- ascii p
		not(x"67") when x"71",	-- ascii q
		not(x"50") when x"72",	-- ascii r
		not(x"6c") when x"73",	-- ascii s
		not(x"78") when x"74",	-- ascii t
		not(x"1C") when x"75",	-- ascii u
		not(x"3E") when x"76",	-- ascii v
		not(x"7E") when x"77",	-- ascii w
		not(x"76") when x"78",	-- ascii x
		not(x"6E") when x"79",	-- ascii y
		not(x"1B") when x"7A",	-- ascii z
		not(x"3F") when x"30",	-- ascii 0
		not(x"06") when x"31",	-- ascii 1
		not(x"5B") when x"32",	-- ascii 2
		not(x"4F") when x"33",	-- ascii 3
		not(x"66") when x"34",	-- ascii 4
		not(x"6D") when x"35",	-- ascii 5
		not(x"7D") when x"36",	-- ascii 6
		not(x"07") when x"37",	-- ascii 7
		not(x"7F") when x"38",	-- ascii 8
		not(x"6F") when x"39",	-- ascii 9
		not(x"00") when others;	-- blank

end architecture DATAFLOW;