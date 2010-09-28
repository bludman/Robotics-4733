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
	 * @param x The integer to be converted.
	 * @return A byte array of size two (2).
	 */
	protected static byte[] intToByte(int x)
	{
		byte[] returnedBytes = new byte[2];
		
		if (x >= 0)
		{
			returnedBytes[0] = (byte)(x >> 8);
			returnedBytes[1] = (byte)x;
		}
		else
		{
			returnedBytes[0] = (byte)(x >> 8 & 0x00FF);
			returnedBytes[1] = (byte)(x & 0x00FF);
		}
		
		return returnedBytes;
	}
	
	@Override
	public byte[] start()
	{
		byte[] data = new byte[1];		
		data[0] = (byte)128;		
		return data;
	}
	
	@Override
	public byte[] baud(int baudCode)
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
	
	@Override
	public byte[] control()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)130;	
		return data;
	}
	
	@Override
	public byte[] safe()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)131;	
		return data;
	}
	
	@Override
	public byte[] full()
	{
		byte[] data = new byte[1];
		data[0] = (byte)132;
		return data;
	}
	
	@Override
	public byte[] spot()
	{
		byte[] data = new byte[1];
		data[0] = (byte)134;
		return data;
	}
	
	@Override
	public byte[] cover()
	{
		byte[] data = new byte[1];
		data[0] = (byte)135;
		return data;
	}
	
	@Override
	public byte[] demo(int demoMode)
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
	
	@Override
	public byte[] drive(int velocity, int radius)
	{
		if (velocity >= -500 && velocity <= 500 &&
				radius >= -2000 && radius <= 2000)
		{
			byte[] data = new byte[5];
			byte[] conversion = new byte[2];
			
			data[0] = (byte)137;
			
			conversion = intToByte(velocity);
			data[1] = conversion[0];
			data[2] = conversion[1];
			
			conversion = intToByte(radius);
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
	
	@Override
	public byte[] lowSideDrivers(int outputBits)
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
	
	@Override
	public byte[] setLEDs(int LEDBits, int color, int intensity)
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
	
	@Override
	public byte[] song()
	{
		return null;
	}
	
	@Override
	public byte[] play(int songNumber)
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
	
	@Override
	public byte[] coverAndDock()
	{
		byte[] data = new byte[1];
		data[0] = (byte)143;
		return data;
	}
	
	@Override
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
	
	@Override
	public byte[] driveDirect(int right, int left)
	{
		if (right >= -500 && right <= 500 &&
				left >= -500 && left <= 500)
		{
			byte[] data = new byte[5];
			byte[] conversion = new byte[2];
			
			data[0] = (byte)145;
			
			conversion = intToByte(right);
			data[1] = conversion[0];
			data[2] = conversion[1];
			
			conversion = intToByte(left);
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
	
	@Override
	public byte[] digitalOutputs(int outputBits)
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
	
		
	@Override
	public byte[] pauseResumeStream(int range)
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
	
	@Override
	public byte[] sendIR(int value)
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
	
	@Override
	public byte[] script()
	{
		return null;
	}
	
	@Override
	public byte[] playScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)153;
		return data;
	}
	
	@Override
	public byte[] showScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)154;
		return data;
	}
	
	@Override
	public byte[] waitTime(int time)
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
	
	@Override
	public byte[] waitDistance(int distance)
	{
		if (distance <= 32768 && distance >= -32768)
		{
			byte[] data = new byte[3];	
			byte[] conversion = new byte[2];
			
			data[0] = (byte)156;
			
			conversion = intToByte(distance);
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
	
	@Override
	public byte[] waitAngle(int angle)
	{
		if (angle <= 32768 && angle >= -32768)
		{
			byte[] data = new byte[3];	
			byte[] conversion = new byte[2];
			
			data[0] = (byte)157;
			
			conversion = intToByte(angle);
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
	
	@Override
	public byte[] waitEvent(int eventID)
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
		private static final long serialVersionUID = -9071240201017346038L;
		public NotImplementedException(String message) {
         super(message);
      }
		public NotImplementedException() {}
	}


}
