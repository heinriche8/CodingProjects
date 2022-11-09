	component Computer_System is
		port (
			gyro_sda_in                                        : in    std_logic                     := 'X';             -- sda_in
			gyro_scl_in                                        : in    std_logic                     := 'X';             -- scl_in
			gyro_sda_oe                                        : out   std_logic;                                        -- sda_oe
			gyro_scl_oe                                        : out   std_logic;                                        -- scl_oe
			hex3_hex0_export                                   : out   std_logic_vector(31 downto 0);                    -- export
			hex5_hex4_export                                   : out   std_logic_vector(15 downto 0);                    -- export
			joyirq_ext_export                                  : in    std_logic                     := 'X';             -- export
			leds_export                                        : out   std_logic_vector(9 downto 0);                     -- export
			penirq_n_export                                    : in    std_logic                     := 'X';             -- export
			pushbuttons_export                                 : in    std_logic_vector(1 downto 0)  := (others => 'X'); -- export
			pwmx_conduit_clk                                   : in    std_logic                     := 'X';             -- clk
			pwmx_conduit_pwm                                   : out   std_logic;                                        -- pwm
			pwmy_conduit_clk                                   : in    std_logic                     := 'X';             -- clk
			pwmy_conduit_pwm                                   : out   std_logic;                                        -- pwm
			sdram_addr                                         : out   std_logic_vector(12 downto 0);                    -- addr
			sdram_ba                                           : out   std_logic_vector(1 downto 0);                     -- ba
			sdram_cas_n                                        : out   std_logic;                                        -- cas_n
			sdram_cke                                          : out   std_logic;                                        -- cke
			sdram_cs_n                                         : out   std_logic;                                        -- cs_n
			sdram_dq                                           : inout std_logic_vector(15 downto 0) := (others => 'X'); -- dq
			sdram_dqm                                          : out   std_logic_vector(1 downto 0);                     -- dqm
			sdram_ras_n                                        : out   std_logic;                                        -- ras_n
			sdram_we_n                                         : out   std_logic;                                        -- we_n
			sdram_clk_clk                                      : out   std_logic;                                        -- clk
			slider_switches_export                             : in    std_logic_vector(9 downto 0)  := (others => 'X'); -- export
			system_pll_ref_clk_clk                             : in    std_logic                     := 'X';             -- clk
			system_pll_ref_reset_reset                         : in    std_logic                     := 'X';             -- reset
			touch_spi_MISO                                     : in    std_logic                     := 'X';             -- MISO
			touch_spi_MOSI                                     : out   std_logic;                                        -- MOSI
			touch_spi_SCLK                                     : out   std_logic;                                        -- SCLK
			touch_spi_SS_n                                     : out   std_logic;                                        -- SS_n
			video_lt24_controller_0_external_interface_lcd_on  : out   std_logic;                                        -- lcd_on
			video_lt24_controller_0_external_interface_reset_n : out   std_logic;                                        -- reset_n
			video_lt24_controller_0_external_interface_csx     : out   std_logic;                                        -- csx
			video_lt24_controller_0_external_interface_data    : out   std_logic_vector(15 downto 0);                    -- data
			video_lt24_controller_0_external_interface_rdx     : out   std_logic;                                        -- rdx
			video_lt24_controller_0_external_interface_wrx     : out   std_logic;                                        -- wrx
			video_lt24_controller_0_external_interface_dcx     : out   std_logic;                                        -- dcx
			video_pll_0_ref_clk_clk                            : in    std_logic                     := 'X';             -- clk
			video_pll_0_ref_reset_reset                        : in    std_logic                     := 'X';             -- reset
			vga_CLK                                            : out   std_logic;                                        -- CLK
			vga_HS                                             : out   std_logic;                                        -- HS
			vga_VS                                             : out   std_logic;                                        -- VS
			vga_BLANK                                          : out   std_logic;                                        -- BLANK
			vga_SYNC                                           : out   std_logic;                                        -- SYNC
			vga_R                                              : out   std_logic_vector(3 downto 0);                     -- R
			vga_G                                              : out   std_logic_vector(3 downto 0);                     -- G
			vga_B                                              : out   std_logic_vector(3 downto 0)                      -- B
		);
	end component Computer_System;

	u0 : component Computer_System
		port map (
			gyro_sda_in                                        => CONNECTED_TO_gyro_sda_in,                                        --                                       gyro.sda_in
			gyro_scl_in                                        => CONNECTED_TO_gyro_scl_in,                                        --                                           .scl_in
			gyro_sda_oe                                        => CONNECTED_TO_gyro_sda_oe,                                        --                                           .sda_oe
			gyro_scl_oe                                        => CONNECTED_TO_gyro_scl_oe,                                        --                                           .scl_oe
			hex3_hex0_export                                   => CONNECTED_TO_hex3_hex0_export,                                   --                                  hex3_hex0.export
			hex5_hex4_export                                   => CONNECTED_TO_hex5_hex4_export,                                   --                                  hex5_hex4.export
			joyirq_ext_export                                  => CONNECTED_TO_joyirq_ext_export,                                  --                                 joyirq_ext.export
			leds_export                                        => CONNECTED_TO_leds_export,                                        --                                       leds.export
			penirq_n_export                                    => CONNECTED_TO_penirq_n_export,                                    --                                   penirq_n.export
			pushbuttons_export                                 => CONNECTED_TO_pushbuttons_export,                                 --                                pushbuttons.export
			pwmx_conduit_clk                                   => CONNECTED_TO_pwmx_conduit_clk,                                   --                               pwmx_conduit.clk
			pwmx_conduit_pwm                                   => CONNECTED_TO_pwmx_conduit_pwm,                                   --                                           .pwm
			pwmy_conduit_clk                                   => CONNECTED_TO_pwmy_conduit_clk,                                   --                               pwmy_conduit.clk
			pwmy_conduit_pwm                                   => CONNECTED_TO_pwmy_conduit_pwm,                                   --                                           .pwm
			sdram_addr                                         => CONNECTED_TO_sdram_addr,                                         --                                      sdram.addr
			sdram_ba                                           => CONNECTED_TO_sdram_ba,                                           --                                           .ba
			sdram_cas_n                                        => CONNECTED_TO_sdram_cas_n,                                        --                                           .cas_n
			sdram_cke                                          => CONNECTED_TO_sdram_cke,                                          --                                           .cke
			sdram_cs_n                                         => CONNECTED_TO_sdram_cs_n,                                         --                                           .cs_n
			sdram_dq                                           => CONNECTED_TO_sdram_dq,                                           --                                           .dq
			sdram_dqm                                          => CONNECTED_TO_sdram_dqm,                                          --                                           .dqm
			sdram_ras_n                                        => CONNECTED_TO_sdram_ras_n,                                        --                                           .ras_n
			sdram_we_n                                         => CONNECTED_TO_sdram_we_n,                                         --                                           .we_n
			sdram_clk_clk                                      => CONNECTED_TO_sdram_clk_clk,                                      --                                  sdram_clk.clk
			slider_switches_export                             => CONNECTED_TO_slider_switches_export,                             --                            slider_switches.export
			system_pll_ref_clk_clk                             => CONNECTED_TO_system_pll_ref_clk_clk,                             --                         system_pll_ref_clk.clk
			system_pll_ref_reset_reset                         => CONNECTED_TO_system_pll_ref_reset_reset,                         --                       system_pll_ref_reset.reset
			touch_spi_MISO                                     => CONNECTED_TO_touch_spi_MISO,                                     --                                  touch_spi.MISO
			touch_spi_MOSI                                     => CONNECTED_TO_touch_spi_MOSI,                                     --                                           .MOSI
			touch_spi_SCLK                                     => CONNECTED_TO_touch_spi_SCLK,                                     --                                           .SCLK
			touch_spi_SS_n                                     => CONNECTED_TO_touch_spi_SS_n,                                     --                                           .SS_n
			video_lt24_controller_0_external_interface_lcd_on  => CONNECTED_TO_video_lt24_controller_0_external_interface_lcd_on,  -- video_lt24_controller_0_external_interface.lcd_on
			video_lt24_controller_0_external_interface_reset_n => CONNECTED_TO_video_lt24_controller_0_external_interface_reset_n, --                                           .reset_n
			video_lt24_controller_0_external_interface_csx     => CONNECTED_TO_video_lt24_controller_0_external_interface_csx,     --                                           .csx
			video_lt24_controller_0_external_interface_data    => CONNECTED_TO_video_lt24_controller_0_external_interface_data,    --                                           .data
			video_lt24_controller_0_external_interface_rdx     => CONNECTED_TO_video_lt24_controller_0_external_interface_rdx,     --                                           .rdx
			video_lt24_controller_0_external_interface_wrx     => CONNECTED_TO_video_lt24_controller_0_external_interface_wrx,     --                                           .wrx
			video_lt24_controller_0_external_interface_dcx     => CONNECTED_TO_video_lt24_controller_0_external_interface_dcx,     --                                           .dcx
			video_pll_0_ref_clk_clk                            => CONNECTED_TO_video_pll_0_ref_clk_clk,                            --                        video_pll_0_ref_clk.clk
			video_pll_0_ref_reset_reset                        => CONNECTED_TO_video_pll_0_ref_reset_reset,                        --                      video_pll_0_ref_reset.reset
			vga_CLK                                            => CONNECTED_TO_vga_CLK,                                            --                                        vga.CLK
			vga_HS                                             => CONNECTED_TO_vga_HS,                                             --                                           .HS
			vga_VS                                             => CONNECTED_TO_vga_VS,                                             --                                           .VS
			vga_BLANK                                          => CONNECTED_TO_vga_BLANK,                                          --                                           .BLANK
			vga_SYNC                                           => CONNECTED_TO_vga_SYNC,                                           --                                           .SYNC
			vga_R                                              => CONNECTED_TO_vga_R,                                              --                                           .R
			vga_G                                              => CONNECTED_TO_vga_G,                                              --                                           .G
			vga_B                                              => CONNECTED_TO_vga_B                                               --                                           .B
		);

