	component Char_Buf_Subsystem is
		port (
			avalon_char_source_ready             : in  std_logic                     := 'X';             -- ready
			avalon_char_source_startofpacket     : out std_logic;                                        -- startofpacket
			avalon_char_source_endofpacket       : out std_logic;                                        -- endofpacket
			avalon_char_source_valid             : out std_logic;                                        -- valid
			avalon_char_source_data              : out std_logic_vector(39 downto 0);                    -- data
			char_buf_rgb_read                    : in  std_logic                     := 'X';             -- read
			char_buf_rgb_readdata                : out std_logic_vector(31 downto 0);                    -- readdata
			char_buffer_control_slave_address    : in  std_logic_vector(1 downto 0)  := (others => 'X'); -- address
			char_buffer_control_slave_byteenable : in  std_logic_vector(3 downto 0)  := (others => 'X'); -- byteenable
			char_buffer_control_slave_read       : in  std_logic                     := 'X';             -- read
			char_buffer_control_slave_write      : in  std_logic                     := 'X';             -- write
			char_buffer_control_slave_writedata  : in  std_logic_vector(31 downto 0) := (others => 'X'); -- writedata
			char_buffer_control_slave_readdata   : out std_logic_vector(31 downto 0);                    -- readdata
			char_buffer_slave_address            : in  std_logic_vector(10 downto 0) := (others => 'X'); -- address
			char_buffer_slave_clken              : in  std_logic                     := 'X';             -- clken
			char_buffer_slave_chipselect         : in  std_logic                     := 'X';             -- chipselect
			char_buffer_slave_write              : in  std_logic                     := 'X';             -- write
			char_buffer_slave_readdata           : out std_logic_vector(31 downto 0);                    -- readdata
			char_buffer_slave_writedata          : in  std_logic_vector(31 downto 0) := (others => 'X'); -- writedata
			char_buffer_slave_byteenable         : in  std_logic_vector(3 downto 0)  := (others => 'X'); -- byteenable
			sys_clk_clk                          : in  std_logic                     := 'X';             -- clk
			sys_reset_reset_n                    : in  std_logic                     := 'X'              -- reset_n
		);
	end component Char_Buf_Subsystem;

	u0 : component Char_Buf_Subsystem
		port map (
			avalon_char_source_ready             => CONNECTED_TO_avalon_char_source_ready,             --        avalon_char_source.ready
			avalon_char_source_startofpacket     => CONNECTED_TO_avalon_char_source_startofpacket,     --                          .startofpacket
			avalon_char_source_endofpacket       => CONNECTED_TO_avalon_char_source_endofpacket,       --                          .endofpacket
			avalon_char_source_valid             => CONNECTED_TO_avalon_char_source_valid,             --                          .valid
			avalon_char_source_data              => CONNECTED_TO_avalon_char_source_data,              --                          .data
			char_buf_rgb_read                    => CONNECTED_TO_char_buf_rgb_read,                    --              char_buf_rgb.read
			char_buf_rgb_readdata                => CONNECTED_TO_char_buf_rgb_readdata,                --                          .readdata
			char_buffer_control_slave_address    => CONNECTED_TO_char_buffer_control_slave_address,    -- char_buffer_control_slave.address
			char_buffer_control_slave_byteenable => CONNECTED_TO_char_buffer_control_slave_byteenable, --                          .byteenable
			char_buffer_control_slave_read       => CONNECTED_TO_char_buffer_control_slave_read,       --                          .read
			char_buffer_control_slave_write      => CONNECTED_TO_char_buffer_control_slave_write,      --                          .write
			char_buffer_control_slave_writedata  => CONNECTED_TO_char_buffer_control_slave_writedata,  --                          .writedata
			char_buffer_control_slave_readdata   => CONNECTED_TO_char_buffer_control_slave_readdata,   --                          .readdata
			char_buffer_slave_address            => CONNECTED_TO_char_buffer_slave_address,            --         char_buffer_slave.address
			char_buffer_slave_clken              => CONNECTED_TO_char_buffer_slave_clken,              --                          .clken
			char_buffer_slave_chipselect         => CONNECTED_TO_char_buffer_slave_chipselect,         --                          .chipselect
			char_buffer_slave_write              => CONNECTED_TO_char_buffer_slave_write,              --                          .write
			char_buffer_slave_readdata           => CONNECTED_TO_char_buffer_slave_readdata,           --                          .readdata
			char_buffer_slave_writedata          => CONNECTED_TO_char_buffer_slave_writedata,          --                          .writedata
			char_buffer_slave_byteenable         => CONNECTED_TO_char_buffer_slave_byteenable,         --                          .byteenable
			sys_clk_clk                          => CONNECTED_TO_sys_clk_clk,                          --                   sys_clk.clk
			sys_reset_reset_n                    => CONNECTED_TO_sys_reset_reset_n                     --                 sys_reset.reset_n
		);

