
module Char_Buf_Subsystem (
	avalon_char_source_ready,
	avalon_char_source_startofpacket,
	avalon_char_source_endofpacket,
	avalon_char_source_valid,
	avalon_char_source_data,
	char_buf_rgb_read,
	char_buf_rgb_readdata,
	char_buffer_control_slave_address,
	char_buffer_control_slave_byteenable,
	char_buffer_control_slave_read,
	char_buffer_control_slave_write,
	char_buffer_control_slave_writedata,
	char_buffer_control_slave_readdata,
	char_buffer_slave_address,
	char_buffer_slave_clken,
	char_buffer_slave_chipselect,
	char_buffer_slave_write,
	char_buffer_slave_readdata,
	char_buffer_slave_writedata,
	char_buffer_slave_byteenable,
	sys_clk_clk,
	sys_reset_reset_n);	

	input		avalon_char_source_ready;
	output		avalon_char_source_startofpacket;
	output		avalon_char_source_endofpacket;
	output		avalon_char_source_valid;
	output	[39:0]	avalon_char_source_data;
	input		char_buf_rgb_read;
	output	[31:0]	char_buf_rgb_readdata;
	input	[1:0]	char_buffer_control_slave_address;
	input	[3:0]	char_buffer_control_slave_byteenable;
	input		char_buffer_control_slave_read;
	input		char_buffer_control_slave_write;
	input	[31:0]	char_buffer_control_slave_writedata;
	output	[31:0]	char_buffer_control_slave_readdata;
	input	[10:0]	char_buffer_slave_address;
	input		char_buffer_slave_clken;
	input		char_buffer_slave_chipselect;
	input		char_buffer_slave_write;
	output	[31:0]	char_buffer_slave_readdata;
	input	[31:0]	char_buffer_slave_writedata;
	input	[3:0]	char_buffer_slave_byteenable;
	input		sys_clk_clk;
	input		sys_reset_reset_n;
endmodule
