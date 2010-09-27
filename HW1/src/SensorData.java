/**
 * 
 */

/**
 * @author Benjamin Ludman
 * @author Mike Hernandez
 *
 */
public class SensorData {
	
	/** The command to execute */
	private byte[] command;
	/** Raw sensor data before parsing */
	private byte[] rawSensorData;
	/** Persistent storage of parsed sensor data storage 
	 * 	NOTE: Group and unused packets will never be set
	 */
	private int[] sensorData;
	/** Size of expected response to the current command */
	private int expectedDataLength;
	
	public SensorData() {
		sensorData= new int[TOTAL_NUMBER_OF_PACKETS];
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
		//FIXME: doesn't expand groups
		int size=0;
		if(command==null || command.length<2)
			throw new IllegalArgumentException("Invalid command");
		
		/* Single sensor packet request */ 
		if(command[0]==OPCODE_SENSORS && isValidPacketId(command[1]))
		{
			return size+=RESPONSE_SIZES[command[1]];
		}
		/* Query list or Stream */
		else if(command[0]==OPCODE_STREAM || command[0]==OPCODE_QUERY_LIST)
		{
			for(int i=2;i<command.length;i++)
			{
				if(isValidPacketId(command[i]))
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
	 * Convert the raw sensor data read from the robot to the persistent internal version
	 */
	public void processSensorData()
	{
		if(command==null || command.length<2)
			throw new IllegalArgumentException("Invalid command");
		
		int commandOpCode = command[0];
		
		/* Single sensor packet request 
		 * NOTE: this single packet could be a group or a single sensor
		 */ 
		if(commandOpCode==OPCODE_SENSORS)
		{
			int packetId = command[1]; //convenience
			if(isValidGroupSensorPacketId(packetId))
			{
				parseGroupSensorValues(packetId, rawSensorData);
			}
			else if(isValidSingleSensorPacketId(packetId))
			{
				sensorData[packetId]=parseSingleSensorValue(packetId, rawSensorData);
			}
			else
			{
				throw new IllegalArgumentException("Invalid Packet ID");
			}
			
		}
		/* Query list or Stream */
		else if(commandOpCode==OPCODE_STREAM || commandOpCode==OPCODE_QUERY_LIST)
		{
			int numberOfPackets= command[1]; //convenience
			int rawSensorDataIndex=0;
			for(int i=2;i<numberOfPackets+2;i++) //first two elements are opcode and packet count
			{
				int packetId = command[i]; //convenience
				
				/** The size (in bytes) of the single sensor packet about to be read */
				int responseSize=RESPONSE_SIZES[packetId]; //convenience
				
				if(isValidGroupSensorPacketId(packetId))
				{
					byte[] rawGroupSensorData=new byte[responseSize];
					System.arraycopy(rawSensorData, rawSensorDataIndex, rawGroupSensorData, 0, responseSize);
					parseGroupSensorValues(packetId, rawGroupSensorData);
					rawSensorDataIndex+=responseSize;
				}
				else if(isValidSingleSensorPacketId(packetId))
				{
					byte[] rawSingleSensorData=new byte[responseSize];
					System.arraycopy(rawSensorData, rawSensorDataIndex, rawSingleSensorData, 0, responseSize);
					sensorData[packetId]=parseSingleSensorValue(packetId, rawSingleSensorData);
					rawSensorDataIndex+=responseSize;
				}
				else
				{
					throw new IllegalArgumentException("Invalid Packet ID");
				}
			}
			
			
			/*
			int lengthOffset=0;
			for(int packetIndex=2, dataByteIndex=0;packetIndex<command.length;packetIndex++,dataByteIndex+=lengthOffset)
			{
				int packetId=command[packetIndex];
				if(isValidPacketId(packetId))
				{
					if(RESPONSE_SIZES[packetId]==1)
					{
						sensorData[packetId]=(int)rawSensorData[dataByteIndex];
						lengthOffset=1;
					}else if(RESPONSE_SIZES[packetId]==2)
					{
						//FIXME: sensorData[packetId]=twoBytesToInt(rawSensorData[dataByteIndex],rawSensorData[dataByteIndex+1]);
						lengthOffset=2;
					}
				}
			}
			*/
		}
		else{
			throw new IllegalArgumentException("Opcode is not a valid sensor request code. " +
					"Cannot calculate expected response size.");
		}
		
	}
	
	/**
	 * Given a group packet id and all of the raw data for the group, 
	 * parse the individual sensor values
	 * @param groupPacketId a valid group sensor packet if
	 * @param rawSensorData the raw data for all individual sensors in the sensor group
	 */
	private void parseGroupSensorValues(int groupPacketId, byte[] groupRawData)
	{
		if(!isValidGroupSensorPacketId(groupPacketId))
			throw new IllegalArgumentException(groupPacketId+" is not a valid group packet id.");
		
		int[] singlePacketIds=GROUPS_PACKET_CONTENTS.values()[groupPacketId].contents();
		int rawSensorDataIndex=0;
		for(int packetId : singlePacketIds)
		{
			/* The size (in bytes) of the single sensor packet about to be read */
			int responseSize=RESPONSE_SIZES[packetId];
			
			/* The raw data for this single sensor packet */
			byte[] singleSensorRawData= new byte[responseSize];
			System.arraycopy(groupRawData,rawSensorDataIndex,singleSensorRawData,0,responseSize);
			sensorData[packetId]=parseSingleSensorValue(packetId, singleSensorRawData);
			
			/* Advance the raw data index to the next sensor's raw data */
			rawSensorDataIndex+=responseSize;
		}
		
	}
	
	/**
	 * Converts raw sensor data to the internal version, given a packet id and the raw sensor data
	 * @param packetId a valid single sensor packet id
	 * @param rawSensorData 1 or 2 bytes of data that correspond to to the data received for this sensor packet id
	 * @return the int representation of the sensor data with proper signedness
	 */
	private int parseSingleSensorValue(int packetId,byte[] rawData)
	{
		if(!isValidSingleSensorPacketId(packetId))
			throw new IllegalArgumentException("Invalid single sensor packet ID: "+packetId+" Should be between 7 and 42.");
		
		if(rawData.length==1)
		{
			return parseOneByteSensorData(packetId, rawData[0]);
			
		}
		else if(rawData.length==2)
		{
			return parseTwoByteSensorData(packetId, rawData);
		}
		else
		{
			throw new IllegalArgumentException("Wrong length of raw data to parse!");
		}
	}
		
	private int parseOneByteSensorData(int packetId, byte packetData){
		if(RESPONSE_SIGNEDNESSES[packetId]==SIGNEDNESS.SIGNED)
		{
			return 0; //TODO
		}
		else if(RESPONSE_SIGNEDNESSES[packetId]==SIGNEDNESS.UNSIGNED)
		{
			return 0; //TODO
		}
		else
		{
			throw new IllegalArgumentException("Unknown Signedness");
		}
	}
	
	private int parseTwoByteSensorData(int packetId, byte[] packetData){
		if(packetData.length != 2)
			throw new IllegalArgumentException("Wrong size of raw data! Expected two bytes!");
		
		if(RESPONSE_SIGNEDNESSES[packetId]==SIGNEDNESS.SIGNED)
		{
			return 0; //TODO
		}
		else if(RESPONSE_SIGNEDNESSES[packetId]==SIGNEDNESS.UNSIGNED)
		{
			return 0; //TODO
		}
		else
		{
			throw new IllegalArgumentException("Unknown Signedness");
		}
	}
	
	/**
	 * Convert two read bytes into a single int, interpreting the bytes as signed or unsigned
	 * @param highByte
	 * @param lowByte
	 * @param signedness
	 * @return
	 */
	private int twoBytesToInt(byte highByte, byte lowByte,SIGNEDNESS signedness ) {
		int value=0;
		if(signedness==SIGNEDNESS.SIGNED)
		{
			value|=highByte;
			value=value<<8;
			value|=lowByte;
			return value;
		}else if(signedness==SIGNEDNESS.UNSIGNED)
		{
			value|=highByte;
			value=value<<8;
			value|=lowByte;
			return value;
		}
		
		throw new IllegalArgumentException("Invalid SIGNEDNESS"); //flow shouldn't reach here
	}
	
	/**
	 * Convert a byte to an int preserving the intended signedness
	 * @param singleByte
	 * @param signedness
	 * @return
	 */
	private int oneBytesToInt(byte singleByte, SIGNEDNESS signedness ) {
		if(signedness==SIGNEDNESS.SIGNED)
		{
			return singleByte;
		}else if(signedness==SIGNEDNESS.UNSIGNED)
		{
			return unsignedByteToInt(singleByte);
		}
		
		throw new IllegalArgumentException("Invalid SIGNEDNESS"); //flow shouldn't reach here
	}

	/**
	 * Convert an unsigned byte into an int
	 * @param uByte
	 * @return
	 */
	private static int unsignedByteToInt(byte uByte) {
	    return (int) uByte & 0xFF;
	    }

	
	/**
	 * Get the raw int value of sensor data
	 * Note: in this formate, the raw bytes have been converted to ints 
	 * @param id
	 * @return
	 */
	public int getRawSensorData(PACKET_IDS id)
	{
		return sensorData[id.ordinal()];
	}
	
	
	/**
	 * Get the boolean value of a specific sensor in the BUMPS_AND_WHEEL_DROPS sesor packet 
	 * @param sensor
	 * @return
	 */
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
	
	private static enum SIGNEDNESS{UNSIGNED,SIGNED};
	private int OPCODE_SENSORS= 142;
	private int OPCODE_STREAM= 148;
	private int OPCODE_QUERY_LIST= 149;
	
	
	
	
	
	/*
	 * ---------------------------------------------
	 * 	Helper Validation Functions
	 * ---------------------------------------------
	 */
	
	/**
	 * Check if a packet id is within range
	 * @param id of packet
	 * @return true if packet id is within the valid range
	 */
	public static boolean isValidPacketId(int id)
	{
		return isValidGroupSensorPacketId(id) || isValidSingleSensorPacketId(id);
	}
	
	/**
	 * Check if a packet id is within range to be a single sensor packet
	 * @param id of packet
	 * @return true if packet id is within the valid range for a single sensor packet
	 */
	public static boolean isValidSingleSensorPacketId(int id)
	{
		return MIN_SINGLE_PACKET_ID<=id && id<=MAX_SINGLE_PACKET_ID;
	}
	
	/**
	 * Check if a packet id is within range to be a group sensor packet
	 * @param id of packet
	 * @return true if packet id is within the valid range for a group sensor packet
	 */
	public static boolean isValidGroupSensorPacketId(int id)
	{
		return MIN_GROUP_PACKET_ID<=id && id<=MAX_GROUP_PACKET_ID;
	}
	
	private static final int MIN_GROUP_PACKET_ID = 0;
	private static final int MAX_GROUP_PACKET_ID = 6;
	private static final int MIN_SINGLE_PACKET_ID = 7;
	private static final int MAX_SINGLE_PACKET_ID = 42;
	
	
	/*
	 * ---------------------------------------------
	 * 	Packet ID information
	 * ---------------------------------------------
	 */
	
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
	
	/** Keep track of the expected response size for each packet type */
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
	

	/** Keep track of which response packets are signed and which are unsigned*/
	final private static SIGNEDNESS[] RESPONSE_SIGNEDNESSES={
		null , //GROUP0 /* Groups don't have signedness, members should be checked instead */
		null , //GROUP1 
		null , //GROUP2
		null , //GROUP3 
		null, //GROUP4 
		null , //GROUP5
		null , //GROUP6 
		SIGNEDNESS.UNSIGNED , 	//BUMPS_AND_WHEEL_DROPS
		SIGNEDNESS.UNSIGNED , 	//WALL
		SIGNEDNESS.UNSIGNED , 	//CLIFF_LEFT
		SIGNEDNESS.UNSIGNED , 	//CLIFF_FRONT_LEFT
		SIGNEDNESS.UNSIGNED , 	//CLIFF_FRONT_RIGHT
		SIGNEDNESS.UNSIGNED , 	//CLIFF_RIGHT
		SIGNEDNESS.UNSIGNED , 	//VIRTUAL_WALL
		SIGNEDNESS.UNSIGNED , 	//OVERCURRENTS
		null , 					//UNUSED
		null , 					//UNUSED
		SIGNEDNESS.UNSIGNED , 	//IR_BYTE
		SIGNEDNESS.UNSIGNED ,	//BUTTONS
		SIGNEDNESS.SIGNED , 	//DISTANCE
		SIGNEDNESS.SIGNED , 	//ANGLE
		SIGNEDNESS.UNSIGNED ,	//CHARGING_STATE
		SIGNEDNESS.UNSIGNED , 	//VOLTAGE
		SIGNEDNESS.SIGNED , 	//CURRENT
		SIGNEDNESS.SIGNED , 	//BATTERY_TEMPERATURE
		SIGNEDNESS.UNSIGNED , 	//BATTERY_CHARGE
		SIGNEDNESS.UNSIGNED , 	//BATTERY_CAPACITY
		SIGNEDNESS.UNSIGNED , 	//WALL_SIGNAL
		SIGNEDNESS.UNSIGNED , 	//CLIFF_LEFT_SIGNAL
		SIGNEDNESS.UNSIGNED , 	//CLIFF_FRONT_LEFT_SIGNAL
		SIGNEDNESS.UNSIGNED , 	//CLIFF_FRONT_RIGHT_SIGNAL
		SIGNEDNESS.UNSIGNED , 	//CLIFF_RIGHT_SIGNAL
		SIGNEDNESS.UNSIGNED , 	//USER_DIGITAL_INPUTS
		SIGNEDNESS.UNSIGNED, 	//USER_ANALOG_INPUT
		SIGNEDNESS.UNSIGNED , 	//CHARGING_SOURCES_AVAILABLE
		SIGNEDNESS.UNSIGNED , 	//OI_MODE
		SIGNEDNESS.UNSIGNED , 	//SONG_NUMBER
		SIGNEDNESS.UNSIGNED , 	//SONG_PLAYING
		SIGNEDNESS.UNSIGNED , 	//NUMBER_OF_STREAM_PACKETS
		SIGNEDNESS.SIGNED , 	//VELOCITY
		SIGNEDNESS.SIGNED , 	//RADIUS
		SIGNEDNESS.SIGNED , 	//RIGHT_VELOCITY
		SIGNEDNESS.SIGNED  		//LEFT_VELOCITY
	};
	
	final private static int TOTAL_NUMBER_OF_PACKETS=42;

	/** Keep track of which single packets are contained in each group packet */
	public static enum GROUPS_PACKET_CONTENTS {
	    GROUP0 (7,26),
	    GROUP1 (7,16),
	    GROUP2 (17,20),
	    GROUP3 (21,26),
	    GROUP4 (27,34),
	    GROUP5 (35,42),
	    GROUP6 (7,42);

	    private final int[] contents; 
	    GROUPS_PACKET_CONTENTS(int first, int last) 
	    {
	        this.contents = new int[last-first+1];
	        for(int i=0;i<contents.length;i++){
	        	contents[i]=first+i;
	        }
	    }
	    public int[] contents()   { return this.contents; }

	}
	
	
	
}
