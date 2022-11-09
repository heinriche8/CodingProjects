	Char_Buf_Subsystem u0 (
		.avalon_char_source_ready             (<connected-to-avalon_char_source_ready>),             //        avalon_char_source.ready
		.avalon_char_source_startofpacket     (<connected-to-avalon_char_source_startofpacket>),     //                          .startofpacket
		.avalon_char_source_endofpacket       (<connected-to-avalon_char_source_endofpacket>),       //                          .endofpacket
		.avalon_char_source_valid             (<connected-to-avalon_char_source_valid>),             //                          .valid
		.avalon_char_source_data              (<connected-to-avalon_char_source_data>),              //                          .data
		.char_buf_rgb_read                    (<connected-to-char_buf_rgb_read>),                    //              char_buf_rgb.read
		.char_buf_rgb_readdata                (<connected-to-char_buf_rgb_readdata>),                //                          .readdata
		.char_buffer_control_slave_address    (<connected-to-char_buffer_control_slave_address>),    // char_buffer_control_slave.address
		.char_buffer_control_slave_byteenable (<connected-to-char_buffer_control_slave_byteenable>), //                          .byteenable
		.char_buffer_control_slave_read       (<connected-to-char_buffer_control_slave_read>),       //                          .read
		.char_buffer_control_slave_write      (<connected-to-char_buffer_control_slave_write>),      //                          .write
		.char_buffer_control_slave_writedata  (<connected-to-char_buffer_control_slave_writedata>),  //                          .writedata
		.char_buffer_control_slave_readdata   (<connected-to-char_buffer_control_slave_readdata>),   //                          .readdata
		.char_buffer_slave_address            (<connected-to-char_buffer_slave_address>),            //         char_buffer_slave.address
		.char_buffer_slave_clken              (<connected-to-char_buffer_slave_clken>),              //                          .clken
		.char_buffer_slave_chipselect         (<connected-to-char_buffer_slave_chipselect>),         //                          .chipselect
		.char_buffer_slave_write              (<connected-to-char_buffer_slave_write>),              //                          .write
		.char_buffer_slave_readdata           (<connected-to-char_buffer_slave_readdata>),           //                          .readdata
		.char_buffer_slave_writedata          (<connected-to-char_buffer_slave_writedata>),          //                          .writedata
		.char_buffer_slave_byteenable         (<connected-to-char_buffer_slave_byteenable>),         //                          .byteenable
		.sys_clk_clk                          (<connected-to-sys_clk_clk>),                          //                   sys_clk.clk
		.sys_reset_reset_n                    (<connected-to-sys_reset_reset_n>)                     //                 sys_reset.reset_n
	);

