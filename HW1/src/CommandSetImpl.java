/**
 * Implementation of the CommandSet interface, containing various (potentially more complex) operations for the iRobot Create.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 *
 */
public class CommandSetImpl implements CommandSet 
{
	public CommandSetImpl()
	{
	}
	
	/**
	 * Converts an integer value to an array of two bytes.
	 * @param myInt The integer to be converted.
	 * @return A byte array of size two (2).
	 */
	protected byte[] ConvertToByte(int myInt)
	{
		byte[] returnedBytes = new byte[2];
		
		if (myInt >= 0)
		{
			returnedBytes[0] = (byte)(myInt >> 8);
			returnedBytes[1] = (byte)myInt;
		}
		else
		{
			returnedBytes[0] = (byte)(myInt >> 8 & 0x00FF);
			returnedBytes[1] = (byte)(myInt & 0x00FF);
		}
		
		return returnedBytes;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Start()
	 */
	public byte[] Start()
	{
		byte[] data = new byte[1];		
		data[0] = (byte)128;		
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Baud(int)
	 */
	public byte[] Baud(int baudCode)
	{
		if (baudCode < 256)
		{		
			byte[] data = new byte[2];	
			data[0] = (byte)128;
			data[1] = (byte)baudCode;	
			return data;
		}
		else
		{
			System.err.println("Error: Baud rate must be less than 256.");
			return null;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Control()
	 */
	public byte[] Control()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)130;	
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Safe()
	 */
	public byte[] Safe()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)131;	
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Full()
	 */
	public byte[] Full()
	{
		byte[] data = new byte[1];
		data[0] = (byte)132;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Spot()
	 */
	public byte[] Spot()
	{
		byte[] data = new byte[1];
		data[0] = (byte)134;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Cover()
	 */
	public byte[] Cover()
	{
		byte[] data = new byte[1];
		data[0] = (byte)135;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Demo(int)
	 */
	public byte[] Demo(int demoMode)
	{
		if (demoMode <= 9 && demoMode >= -1)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)136;	
			data[1] = (byte)demoMode;
			return data;
		}
		else
		{
			System.err.println("Error: Demo mode must be between -1 and 9 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Drive(int, int, int, int)
	 */
	public byte[] Drive(int velocity, int radius)
	{
		if (velocity >= -500 && velocity <= 500 &&
				radius >= -2000 && radius <= 2000)
		{
			byte[] data = new byte[5];
			byte[] conversion = new byte[2];
			
			data[0] = (byte)137;
			
			conversion = ConvertToByte(velocity);
			data[1] = conversion[0];
			data[2] = conversion[1];
			
			conversion = ConvertToByte(radius);
			data[3] = conversion[0];
			data[4] = conversion[1];
			
			return data;
		}
		else
		{
			System.err.println("Error: Velocity values must be between -500 and 500, and radius values must be between -2000 and 2000 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#LowSideDrivers(int)
	 */
	public byte[] LowSideDrivers(int outputBits)
	{
		if (outputBits <= 7 && outputBits >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)138;
			data[1] = (byte)outputBits;
			return data;
		}
		else
		{
			System.err.println("Error: Output bits must be between 0 and 7 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#LEDs(int, int, int)
	 */
	public byte[] LEDs(int LEDBits, int color, int intensity)
	{
		if (LEDBits <= 10 && LEDBits >= 0)
		{
			byte[] data = new byte[4];	
			data[0] = (byte)138;
			data[1] = (byte)LEDBits;
			data[2] = (byte)color;
			data[3] = (byte)intensity;
			return data;
		}
		else
		{
			System.err.println("Error: LED bits must be between 0 and 10 and color/intensity must be between 0 and 255 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Song()
	 */
	public byte[] Song()
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Play(int)
	 */
	public byte[] Play(int songNumber)
	{
		if (songNumber <= 15 && songNumber >= 0)
		{
			byte[] data = new byte[1];
			data[0] = (byte)songNumber;
			return data;
		}
		else
		{
			System.err.println("Error: Song number must be between 0 and 15 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#CoverAndDock()
	 */
	public byte[] CoverAndDock()
	{
		byte[] data = new byte[1];
		data[0] = (byte)143;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#PMWLowSideDrivers(int, int, int)
	 */
	public byte[] PMWLowSideDrivers(int lsd2, int lsd1, int lsd0)
	{
		if (lsd0 <= 128 && lsd0 >= 0 &&
				lsd1 <= 128 && lsd1 >= 0 &&
				lsd2 <= 128 && lsd2 >= 0)
		{
			byte[] data = new byte[4];
			data[0] = (byte)144;
			data[1] = (byte)lsd2;
			data[2] = (byte)lsd1;
			data[3] = (byte)lsd0;
			return data;
		}
		else
		{
			System.err.println("Error: Low Side Driver Duty Cycle values must be between 0 and 128 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#DriveDirect(int, int, int, int)
	 */
	public byte[] DriveDirect(int right, int left)
	{
		if (right >= -500 && right <= 500 &&
				left >= -500 && left <= 500)
		{
			byte[] data = new byte[5];
			byte[] conversion = new byte[2];
			
			data[0] = (byte)145;
			
			conversion = ConvertToByte(right);
			data[1] = conversion[0];
			data[2] = conversion[1];
			
			conversion = ConvertToByte(left);
			data[3] = conversion[0];
			data[4] = conversion[1];
			
			return data;
		}
		else
		{
			System.err.println("Error: Velocity values must be between -500 and 500 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#DigitalOutputs(int)
	 */
	public byte[] DigitalOutputs(int outputBits)
	{
		if (outputBits <= 7 && outputBits >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)147;
			data[1] = (byte)outputBits;
			return data;
		}
		else
		{
			System.err.println("Error: Output bits must be between 0 and 7 (inclusive).");
			return null;
		}
	}
	
		
	/* (non-Javadoc)
	 * @see CommandSet#PauseResumeStream(int)
	 */
	public byte[] PauseResumeStream(int range)
	{
		if (range == 0 || range == 1)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)150;
			data[1] = (byte)range;
			return data;
		}
		else
		{
			System.err.println("Error: Range must be 0 or 1.");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#SendIR(int)
	 */
	public byte[] SendIR(int value)
	{
		if (value <= 255 && value >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)151;
			data[1] = (byte)value;
			return data;
		}
		else
		{
			System.err.println("Error: Value must be between 0 and 255 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#Script()
	 */
	public byte[] Script()
	{
		return null;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#PlayScript()
	 */
	public byte[] PlayScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)153;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#StopScript()
	 */
	public byte[] ShowScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)154;
		return data;
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#WaitTime(int)
	 */
	public byte[] WaitTime(int time)
	{
		// Note: Time is measured in tenths of a second.
		if (time <= 255 && time >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)155;
			data[1] = (byte)time;
			return data;
		}
		else
		{
			System.err.println("Error: Time must be between 0 and 255 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#WaitDistance(int, int)
	 */
	public byte[] WaitDistance(int distance)
	{
		if (distance <= 32768 && distance >= -32768)
		{
			byte[] data = new byte[3];	
			byte[] conversion = new byte[2];
			
			data[0] = (byte)156;
			
			conversion = ConvertToByte(distance);
			data[1] = conversion[0];
			data[2] = conversion[1];
			return data;
		}
		else
		{
			System.err.println("Error: Distance must be between -32768 and 32768 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#WaitAngle(int, int)
	 */
	public byte[] WaitAngle(int angle)
	{
		if (angle <= 32768 && angle >= -32768)
		{
			byte[] data = new byte[3];	
			byte[] conversion = new byte[2];
			
			data[0] = (byte)157;
			
			conversion = ConvertToByte(angle);
			data[1] = conversion[0];
			data[2] = conversion[1];
			return data;
		}
		else
		{
			System.err.println("Error: Angle must be between -32768 and 32768 (inclusive).");
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see CommandSet#WaitEvent(int)
	 */
	public byte[] WaitEvent(int eventID)
	{
		if (eventID <= 20 && eventID >= -20 && eventID != 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)158;
			data[1] = (byte)eventID;
			return data;
		}
		else
		{
			System.err.println("Error: Event ID must be between -20 and 20 (inclusive), excluding 0.");
			return null;
		}
	}

	@Override
	public byte[] querySingleSensor(SensorData.PACKET_IDS thePacketId) 
	{
		int packetId=thePacketId.ordinal();
		if(SensorData.isValidPacketId(packetId))
		{
			byte[] command = new byte[2];
			command[0] = (byte)142;
			System.out.println("Setting opcode to: "+(byte)command[0]);
			command[1] = (byte)packetId;
			return command;
		}
		else
		{
			System.err.println("Error: packetId should be between 0 and 42 (inclusive).");
			return null;
		}
	}

	@Override
	public byte[] querySensorList(SensorData.PACKET_IDS[] packetIds)
	{
		if (packetIds==null)
			throw new IllegalArgumentException("Invalid number of packet ids");
		
		int expectedNumberOfCommands=packetIds.length;
		byte[] command = new byte[2+expectedNumberOfCommands];
		
		command[0] = (byte) SensorData.OPCODE_QUERY_LIST;
		command[1] = (byte) expectedNumberOfCommands;
		
		/* Read each packet id and add it to the command if it is valid */
		for(int i = 0; i < packetIds.length;i++)
		{
			int packetId=packetIds[i].ordinal();
			command[i+2] = (byte)packetId;
		}
		
		if(expectedNumberOfCommands>0)
			return command;
		else
			throw new IllegalArgumentException("Invalid number of packet ids");
	}

	@Override
	public byte[] streamSensorList(SensorData.PACKET_IDS[] packetIds)
	{
		throw new NotImplementedException();
	}

	public class NotImplementedException extends RuntimeException{
		 public NotImplementedException(String message) {
         super(message);
      }
		public NotImplementedException() {}
	};
}
