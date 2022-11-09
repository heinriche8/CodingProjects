library ieee;
use ieee.std_logic_1164.all;

entity DE10_Lite_Computer is
port (
			--Onboard System Clocks
			CLOCK_50 : in std_logic;
			CLOCK2_50 : in std_logic;
			CLOCK_ADC_10 : in std_logic;
			
			--Pushbuttons
			KEY : in std_logic_vector(1 downto 0);
			
			--Sliders
			SW : in std_logic_vector(9 downto 0);
			
			--LEDS
			LEDR : out std_logic_vector(9 downto 0);
			
			--Seven Segment Displays
			HEX0, HEX1, HEX2, HEX3, HEX4, HEX5 : out std_logic_vector(7 downto 0);
			
			--SDRAM 
			DRAM_ADDR : out std_logic_vector(12 downto 0);
			DRAM_BA : out std_logic_vector(1 downto 0);
			DRAM_CAS_N : out std_logic;
			DRAM_CKE : out std_logic;
			DRAM_CLK : out std_logic;
			DRAM_CS_N : out std_logic;
			DRAM_DQ : inout std_logic_vector(15 downto 0);
			DRAM_LDQM : out std_logic;
			DRAM_RAS_N : out std_logic;
			DRAM_UDQM : out std_logic;
			DRAM_WE_N : out std_logic;
			
			-- PWM
			PWMX_OUT: out std_logic;
			PWMY_OUT: out std_logic;
			
			--LT24
			LCD_ON: out std_logic;
			LCD_RST_n: out std_logic;
			LCD_CS: out std_logic;
			LCD_DATA: out std_logic_vector(15 downto 0);
			LCD_RD: out std_logic;
			LCD_WR: out std_logic;
			LCD_RS: out std_logic;
			
			-- LT24 TOUCH
			TOUCH_DOUT: out std_logic;
			TOUCH_DIN: in std_logic;
			TOUCH_SCLK: out std_logic;
			TOUCH_CS: out std_logic;
			PENIRQ: in std_logic;
			
			-- Accel
			ACCEL_MODE: out std_logic;
			ACCEL_SDO: out std_logic;
			ACCEL_SCL: inout std_logic;
			ACCEL_SDA: inout std_logic;
			
			VGA_OUT_R: out std_logic_vector(3 downto 0);
			VGA_OUT_G: out std_logic_vector(3 downto 0);
			VGA_OUT_B: out std_logic_vector(3 downto 0);
			VGA_OUT_HS: out std_logic;
			VGA_OUT_VS: out std_logic;
			
			JOY_PB: in std_logic
		);
end entity DE10_Lite_Computer;

architecture STRUCTURAL of DE10_Lite_Computer is 

	 component Computer_System is 
	 port (
		gyro_sda_in                                        : in    std_logic                     := '0';             --                                       gyro.sda_in
		gyro_scl_in                                        : in    std_logic                     := '0';             --                                           .scl_in
		gyro_sda_oe                                        : out   std_logic;                                        --                                           .sda_oe
		gyro_scl_oe                                        : out   std_logic;                                        --                                           .scl_oe
		hex3_hex0_export                                   : out   std_logic_vector(31 downto 0);                    --                                  hex3_hex0.export
		hex5_hex4_export                                   : out   std_logic_vector(15 downto 0);                    --                                  hex5_hex4.export
		joyirq_ext_export                                  : in    std_logic                     := '0';             --                                 joyirq_ext.export
		leds_export                                        : out   std_logic_vector(9 downto 0);                     --                                       leds.export
		penirq_n_export                                    : in    std_logic                     := '0';             --                                   penirq_n.export
		pushbuttons_export                                 : in    std_logic_vector(1 downto 0)  := (others => '0'); --                                pushbuttons.export
		pwmx_conduit_clk                                   : in    std_logic                     := '0';             --                               pwmx_conduit.clk
		pwmx_conduit_pwm                                   : out   std_logic;                                        --                                           .pwm
		pwmy_conduit_clk                                   : in    std_logic                     := '0';             --                               pwmy_conduit.clk
		pwmy_conduit_pwm                                   : out   std_logic;                                        --                                           .pwm
		sdram_addr                                         : out   std_logic_vector(12 downto 0);                    --                                      sdram.addr
		sdram_ba                                           : out   std_logic_vector(1 downto 0);                     --                                           .ba
		sdram_cas_n                                        : out   std_logic;                                        --                                           .cas_n
		sdram_cke                                          : out   std_logic;                                        --                                           .cke
		sdram_cs_n                                         : out   std_logic;                                        --                                           .cs_n
		sdram_dq                                           : inout std_logic_vector(15 downto 0) := (others => '0'); --                                           .dq
		sdram_dqm                                          : out   std_logic_vector(1 downto 0);                     --                                           .dqm
		sdram_ras_n                                        : out   std_logic;                                        --                                           .ras_n
		sdram_we_n                                         : out   std_logic;                                        --                                           .we_n
		sdram_clk_clk                                      : out   std_logic;                                        --                                  sdram_clk.clk
		slider_switches_export                             : in    std_logic_vector(9 downto 0)  := (others => '0'); --                            slider_switches.export
		system_pll_ref_clk_clk                             : in    std_logic                     := '0';             --                         system_pll_ref_clk.clk
		system_pll_ref_reset_reset                         : in    std_logic                     := '0';             --                       system_pll_ref_reset.reset
		touch_spi_MISO                                     : in    std_logic                     := '0';             --                                  touch_spi.MISO
		touch_spi_MOSI                                     : out   std_logic;                                        --                                           .MOSI
		touch_spi_SCLK                                     : out   std_logic;                                        --                                           .SCLK
		touch_spi_SS_n                                     : out   std_logic;                                        --                                           .SS_n
		vga_CLK                                            : out   std_logic;                                        --                                        vga.CLK
		vga_HS                                             : out   std_logic;                                        --                                           .HS
		vga_VS                                             : out   std_logic;                                        --                                           .VS
		vga_BLANK                                          : out   std_logic;                                        --                                           .BLANK
		vga_SYNC                                           : out   std_logic;                                        --                                           .SYNC
		vga_R                                              : out   std_logic_vector(3 downto 0);                     --                                           .R
		vga_G                                              : out   std_logic_vector(3 downto 0);                     --                                           .G
		vga_B                                              : out   std_logic_vector(3 downto 0);                     --                                           .B
		video_lt24_controller_0_external_interface_lcd_on  : out   std_logic;                                        -- video_lt24_controller_0_external_interface.lcd_on
		video_lt24_controller_0_external_interface_reset_n : out   std_logic;                                        --                                           .reset_n
		video_lt24_controller_0_external_interface_csx     : out   std_logic;                                        --                                           .csx
		video_lt24_controller_0_external_interface_data    : out   std_logic_vector(15 downto 0);                    --                                           .data
		video_lt24_controller_0_external_interface_rdx     : out   std_logic;                                        --                                           .rdx
		video_lt24_controller_0_external_interface_wrx     : out   std_logic;                                        --                                           .wrx
		video_lt24_controller_0_external_interface_dcx     : out   std_logic;                                        --                                           .dcx
		video_pll_0_ref_clk_clk                            : in    std_logic                     := '0';             --                        video_pll_0_ref_clk.clk
		video_pll_0_ref_reset_reset                        : in    std_logic                     := '0'              --                      video_pll_0_ref_reset.reset
	);
    end component Computer_System;
	 
	 component dseg7 is
		port (
			ASCII:	in 	std_logic_vector(7 downto 0);
			DATA:		out	std_logic_vector(7 downto 0)
		);
	 end component dseg7;
	 
	 component DEBOUNCE is port (
			D: in std_logic;
			CLK: in std_logic;
			Q: out std_logic
	);
	end component DEBOUNCE;
	 
	 signal HEX3_HEX0 : std_logic_vector(31 downto 0);
	 signal HEX5_HEX4 : std_logic_vector(15 downto 0);
	 signal JOYPB_INT: std_logic;
	 signal I2C_SCL_IN: std_logic;
	 signal I2C_SDA_IN: std_logic;
	 signal I2C_SDA_OE: std_logic;
	 signal I2C_SCL_OE: std_logic;
	 signal DBCLK_INT: std_logic;
	 signal DRAM_DQM : std_logic_vector(1 downto 0);
	 
begin 


    u0 : component Computer_System
        port map (
            hex3_hex0_export           => HEX3_HEX0,           --            hex3_hex0.export
            hex5_hex4_export           => HEX5_HEX4,           --            hex5_hex4.export
            leds_export                => LEDR,                --            leds.export
            pushbuttons_export         => not KEY,        	 --          pushbuttons.export
            sdram_addr                 => DRAM_ADDR,                 --                sdram.addr
            sdram_ba                   => DRAM_BA,                   --                     .ba
            sdram_cas_n                => DRAM_CAS_N,                --                     .cas_n
            sdram_cke                  => DRAM_CKE,                  --                     .cke
            sdram_cs_n                 => DRAM_CS_N,                 --                     .cs_n
            sdram_dq                   => DRAM_DQ,                   --                     .dq
            sdram_dqm                  => DRAM_DQM,                  --                     .dqm
            sdram_ras_n                => DRAM_RAS_N,                --                     .ras_n
            sdram_we_n                 => DRAM_WE_N,                 --                     .we_n
            sdram_clk_clk              => DRAM_CLK,              --            sdram_clk.clk
            slider_switches_export     => SW,     --      slider_switches.export
            system_pll_ref_clk_clk     => CLOCK_50,     --   system_pll_ref_clk.clk
            system_pll_ref_reset_reset => '0',  -- system_pll_ref_reset.reset
				pwmx_conduit_clk => CLOCK2_50,
				pwmy_conduit_clk => CLOCK2_50,
				pwmx_conduit_pwm => PWMX_OUT,
				pwmy_conduit_pwm => PWMY_OUT,
				video_pll_0_ref_clk_clk => CLOCK2_50,
				video_pll_0_ref_reset_reset => '0',
				video_lt24_controller_0_external_interface_lcd_on => LCD_ON,
				video_lt24_controller_0_external_interface_reset_n => LCD_RST_n,
				video_lt24_controller_0_external_interface_csx => LCD_CS,
				video_lt24_controller_0_external_interface_data => LCD_DATA,
				video_lt24_controller_0_external_interface_rdx => LCD_RD,
				video_lt24_controller_0_external_interface_wrx => LCD_WR,
				video_lt24_controller_0_external_interface_dcx => LCD_RS,
				joyirq_ext_export => JOYPB_INT,
				touch_spi_MISO => TOUCH_DIN,
				touch_spi_MOSI => TOUCH_DOUT,
				touch_spi_SCLK => TOUCH_SCLK,
				touch_spi_SS_n => TOUCH_CS,
				penirq_n_export => PENIRQ,
				gyro_scl_oe => I2C_SCL_OE,
				gyro_sda_oe => I2C_SDA_OE,
				gyro_scl_in => I2C_SCL_IN,
				gyro_sda_in => I2C_SDA_IN,
				
				vga_HS => VGA_OUT_HS,
				vga_VS => VGA_OUT_VS,
				
				vga_R => VGA_OUT_R,
				vga_G => VGA_OUT_G,
				vga_B => VGA_OUT_B
        );	  
	
	-- Decoder connected to HEX0
	seg0 : component dseg7
	port map (
		-- Input from HEX3_HEX0 bus
		ASCII => HEX3_HEX0(7 downto 0),
		
		-- Output inverted to HEX0
		DATA => HEX0
	);
	
	
	-- Decoder connected to HEX1
	seg1 : component dseg7
	port map (
		-- Input from HEX3_HEX0 bus
		ASCII => HEX3_HEX0(15 downto 8),
		
		-- Output inverted to HEX1
		DATA => HEX1
	);
	
	
	-- Decoder connected to HEX2
	seg2 : component dseg7
	port map (
		-- Input from HEX3_HEX0 bus
		ASCII => HEX3_HEX0(23 downto 16),
		
		-- Output inverted to HEX2
		DATA => HEX2
	);
	
	
	-- Decoder connected to HEX3
	seg3 : component dseg7
	port map (
		-- Input from HEX3_HEX0 bus
		ASCII => HEX3_HEX0(31 downto 24),
		
		-- Output inverted to HEX3
		DATA => HEX3
	);
	
	
	-- Decoder connected to HEX4
	seg4 : component dseg7
	port map (
		-- Input from HEX5_HEX4 bus
		ASCII => HEX5_HEX4(7 downto 0),
		
		-- Output inverted to HEX4
		DATA => HEX4
	);
	
	
	-- Decoder connected to HEX5
	seg5 : component dseg7
	port map (
		-- Input from HEX5_HEX4 bus
		ASCII => HEX5_HEX4(15 downto 8),
		
		-- Output inverted to HEX5
		DATA => HEX5
	);
	
	debouncer: component DEBOUNCE
	port map(
		D => JOY_PB,
		Q => JOYPB_INT,
		CLK => CLOCK2_50
	);
	
	-- Force accelerometer to I2C mode permanently
	ACCEL_MODE <= '1';
	ACCEL_SDO <= '0'; -- Addr = 0x53
	
	-- Other accelerometer configurations
	I2C_SCL_IN <= ACCEL_SCL;
	ACCEL_SCL <= '0' when I2C_SCL_OE = '1' else 'Z';
	I2C_SDA_IN <= ACCEL_SDA;
	ACCEL_SDA <= '0' when I2C_SDA_OE = '1' else 'Z';
	
	DRAM_LDQM <= DRAM_DQM(0);
	DRAM_UDQM <= DRAM_DQM(1);


end architecture STRUCTURAL;