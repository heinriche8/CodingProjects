
module Computer_System (
	gyro_sda_in,
	gyro_scl_in,
	gyro_sda_oe,
	gyro_scl_oe,
	hex3_hex0_export,
	hex5_hex4_export,
	joyirq_ext_export,
	leds_export,
	penirq_n_export,
	pushbuttons_export,
	pwmx_conduit_clk,
	pwmx_conduit_pwm,
	pwmy_conduit_clk,
	pwmy_conduit_pwm,
	sdram_addr,
	sdram_ba,
	sdram_cas_n,
	sdram_cke,
	sdram_cs_n,
	sdram_dq,
	sdram_dqm,
	sdram_ras_n,
	sdram_we_n,
	sdram_clk_clk,
	slider_switches_export,
	system_pll_ref_clk_clk,
	system_pll_ref_reset_reset,
	touch_spi_MISO,
	touch_spi_MOSI,
	touch_spi_SCLK,
	touch_spi_SS_n,
	video_lt24_controller_0_external_interface_lcd_on,
	video_lt24_controller_0_external_interface_reset_n,
	video_lt24_controller_0_external_interface_csx,
	video_lt24_controller_0_external_interface_data,
	video_lt24_controller_0_external_interface_rdx,
	video_lt24_controller_0_external_interface_wrx,
	video_lt24_controller_0_external_interface_dcx,
	video_pll_0_ref_clk_clk,
	video_pll_0_ref_reset_reset,
	vga_CLK,
	vga_HS,
	vga_VS,
	vga_BLANK,
	vga_SYNC,
	vga_R,
	vga_G,
	vga_B);	

	input		gyro_sda_in;
	input		gyro_scl_in;
	output		gyro_sda_oe;
	output		gyro_scl_oe;
	output	[31:0]	hex3_hex0_export;
	output	[15:0]	hex5_hex4_export;
	input		joyirq_ext_export;
	output	[9:0]	leds_export;
	input		penirq_n_export;
	input	[1:0]	pushbuttons_export;
	input		pwmx_conduit_clk;
	output		pwmx_conduit_pwm;
	input		pwmy_conduit_clk;
	output		pwmy_conduit_pwm;
	output	[12:0]	sdram_addr;
	output	[1:0]	sdram_ba;
	output		sdram_cas_n;
	output		sdram_cke;
	output		sdram_cs_n;
	inout	[15:0]	sdram_dq;
	output	[1:0]	sdram_dqm;
	output		sdram_ras_n;
	output		sdram_we_n;
	output		sdram_clk_clk;
	input	[9:0]	slider_switches_export;
	input		system_pll_ref_clk_clk;
	input		system_pll_ref_reset_reset;
	input		touch_spi_MISO;
	output		touch_spi_MOSI;
	output		touch_spi_SCLK;
	output		touch_spi_SS_n;
	output		video_lt24_controller_0_external_interface_lcd_on;
	output		video_lt24_controller_0_external_interface_reset_n;
	output		video_lt24_controller_0_external_interface_csx;
	output	[15:0]	video_lt24_controller_0_external_interface_data;
	output		video_lt24_controller_0_external_interface_rdx;
	output		video_lt24_controller_0_external_interface_wrx;
	output		video_lt24_controller_0_external_interface_dcx;
	input		video_pll_0_ref_clk_clk;
	input		video_pll_0_ref_reset_reset;
	output		vga_CLK;
	output		vga_HS;
	output		vga_VS;
	output		vga_BLANK;
	output		vga_SYNC;
	output	[3:0]	vga_R;
	output	[3:0]	vga_G;
	output	[3:0]	vga_B;
endmodule
