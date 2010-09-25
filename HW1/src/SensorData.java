/**
 * 
 */

/**
 * @author Ben
 *
 */
public class SensorData {
	
	/** The command to execute */
	private byte[] command;
	/** Raw sensor data before parsing */
	private byte[] readData;
	/** Parsed Sensor data storage */
	private int[] sensorData;
	
	
	public static enum PACKET_IDS{
		GROUP0,
		GROUP1,
		GROUP2,
		GROUP3,
		GROUP4,
		GROUP5,
		GROUP6,
		BUMPS_AND_WHEEL_DROPS,
		WALL,
		CLIFF_LEFT,
		CLIFF_FRONT_LEFT,
		CLIFF_FRONT_RIGHT,
		CLIFF_RIGHT,
		VIRTUAL_WALL,
		OVERCURRENTS,
		UNUSED1,
		UNUSED2,
		IR_BYTE,
		BUTTONS,
		DISTANCE,
		ANGLE,
		CHARGING_STATE,
		VOLTAGE,
		CURRENT,
		BATTERY_TEMPERATURE,
		BATTERY_CHARGE,
		BATTERY_CAPACITY,
		WALL_SIGNAL,
		CLIFF_LEFT_SIGNAL,
		CLIFF_FRONT_LEFT_SIGNAL,
		CLIFF_FRONT_RIGHT_SIGNAL,
		CLIFF_RIGHT_SIGNAL,
		USER_DIGITAL_INPUTS,
		USER_ANALOG_INPUT,
		CHARGING_SOURCES_AVAILABLE,
		OI_MODE,
		SONG_NUMBER,
		SONG_PLAYING,
		NUMBER_OF_STREAM_PACKETS,
		VELOCITY,
		RADIUS,
		RIGHT_VELOCITY,
		LEFT_VELOCITY
	};
	
	final private static int[] RESPONSE_SIZES={
		26 , //GROUP0 
		10 , //GROUP1 
		6 , //GROUP2
		10 , //GROUP3 
		14, //GROUP4 
		12 , //GROUP5
		52 , //GROUP6 
		1 , //BUMPS_AND_WHEEL_DROPS
		1 , //WALL
		1 , //CLIFF_LEFT
		1 , //CLIFF_FRONT_LEFT
		1 , //CLIFF_FRONT_RIGHT
		1 , //CLIFF_RIGHT
		1 , //VIRTUAL_WALL
		1 , //OVERCURRENTS
		1 , //UNUSED
		1 , //UNUSED
		1 , //IR_BYTE
		1 , //BUTTONS
		2 , //DISTANCE
		2 , //ANGLE
		1 , //CHARGING_STATE
		2 , //VOLTAGE
		2 , //CURRENT
		1 , //BATTERY_TEMPERATURE
		2 , //BATTERY_CHARGE
		2 , //BATTERY_CAPACITY
		2 , //WALL_SIGNAL
		2 , //CLIFF_LEFT_SIGNAL
		2 , //CLIFF_FRONT_LEFT_SIGNAL
		2 , //CLIFF_FRONT_RIGHT_SIGNAL
		2 , //CLIFF_RIGHT_SIGNAL
		1 , //USER_DIGITAL_INPUTS
		1 , //USER_ANALOG_INPUT
		1 , //CHARGING_SOURCES_AVAILABLE
		1 , //OI_MODE
		1 , //SONG_NUMBER
		1 , //SONG_PLAYING
		1 , //NUMBER_OF_STREAM_PACKETS
		2 , //VELOCITY
		2 , //RADIUS
		2 , //RIGHT_VELOCITY
		2  //LEFT_VELOCITY
	};
	

	
		
	/**
	 * Size of expected response to the current command
	 */
	private int expectedDataLength;
	
	public SensorData() {
		sensorData= new int[42];
	}

		
	public void setQueryCommand(byte[] command) {
		this.command=command;
		this.expectedDataLength=calculateExpectedResponseSize(command);
	}
	
	public byte[] getQueryCommand()
	{
		return this.command;
	}
	
	/**
	 * Calculate the expected size of the response packet based on the 
	 * ids of the requested sensor packets
	 * @param command
	 * @return expected size of the response packet (in bytes)
	 */
	private int calculateExpectedResponseSize(byte[] command)
	{
		int size=0;
		if(command==null || command.length<2)
			throw new IllegalArgumentException("Invalid command");
		
		/* Single sensor packet request */ 
		if(command[1]==OPCODE_SENSORS && packetIdIsInValidRange(command[2]))
		{
			return size+=RESPONSE_SIZES[command[2]];
		}
		/* Query list or Stream */
		else if(command[1]==OPCODE_STREAM || command[1]==OPCODE_QUERY_LIST)
		{
			for(int i=3;i<command.length;i++)
			{
				if(packetIdIsInValidRange(command[i]))
				{
					size+=RESPONSE_SIZES[command[i]];
				}
			}
		}
		else{
			throw new IllegalArgumentException("Opcode is not a valid sensor request code. " +
					"Cannot calculate expected response size.");
		}
		
		return size;
	}
		

	/**
	 * Check if a packet id is within range
	 * @param id of packet
	 * @return true if packet id is within the valid range
	 */
	public static boolean packetIdIsInValidRange(int id)
	{
		return 0<=id && id<=42;
	}
	
	public void parseSensorData()
	{
		//TODO: Implement this
		if(command==null || command.length<2)
			throw new IllegalArgumentException("Invalid command");
		
		/* Single sensor packet request */ 
		if(command[1]==OPCODE_SENSORS && packetIdIsInValidRange(command[2]))
		{
			sensorData[0]=(int)readData[0];
		}
		/* Query list or Stream */
		else if(command[1]==OPCODE_STREAM || command[1]==OPCODE_QUERY_LIST)
		{
			for(int i=3;i<command.length;i++)
			{
				if(packetIdIsInValidRange(command[i]))
				{
					if(RESPONSE_SIZES[command[i]]==1)
					{
						
					}else if(RESPONSE_SIZES[command[i]]==2)
					{
						
					}
				}
			}
		}
		else{
			throw new IllegalArgumentException("Opcode is not a valid sensor request code. " +
					"Cannot calculate expected response size.");
		}
		
	}
	
	public int getRawSensorData(PACKET_IDS id)
	{
		//TODO: Implement this
		return 0;
		
	}
	
	public boolean getBumpOrWheelDropStatus(BUMPS_AND_WHEEL_DROPS sensor){
		/*
		 * Get the raw value of the BUMPS_AND_WHEEL_DROPS sensor and then parse the
		 * specific sensor boolean value using a bitwise AND to get the desired bit
		 */
		return (this.sensorData[PACKET_IDS.BUMPS_AND_WHEEL_DROPS.ordinal()] & (1<<sensor.ordinal()))==0?false:true;
	}
	
	
	/* Ordinal represents the bit in the BUMPS_AND_WHEEL_DROPS sensor data's byte value */
	public static enum BUMPS_AND_WHEEL_DROPS{
		BUMP_RIGHT,			//bit 0
		BUMP_LEFT,			//bit 1
		WHEEL_DROP_RIGHT,	//bit 2
		WHEEL_DROP_LEFT,	//bit 3
		WHEEL_DROP_CASTER	//bit 4
		/* Ignore bits 5-7 */
	};
	
	private int OPCODE_SENSORS= 142;
	private int OPCODE_STREAM= 148;
	private int OPCODE_QUERY_LIST= 149;
}
