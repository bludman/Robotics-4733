/**
 * 
 * @author Mike Hernandez, Benjamin Ludman
 *
 */
public class CommandSet 
{
	OpenCommPort ocp = new OpenCommPort();
	
	public CommandSet()
	{
	}
	
	// TODO: Sensing commands
	
	public void Start()
	{
		byte[] data = new byte[1];		
		data[0] = (byte)128;		
		ocp.write(data);
	}
	
	public void Baud(int baudCode)
	{
		if (baudCode < 256)
		{		
			byte[] data = new byte[2];	
			data[0] = (byte)128;
			data[1] = (byte)baudCode;	
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Baud rate must be less than 256.");
		}
	}
	
	public void Control()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)130;	
		ocp.write(data);
	}
	
	public void Safe()
	{
		byte[] data = new byte[1];	
		data[0] = (byte)131;	
		ocp.write(data);
	}
	
	public void Full()
	{
		byte[] data = new byte[1];
		data[0] = (byte)132;
		ocp.write(data);
	}
	
	public void Spot()
	{
		byte[] data = new byte[1];
		data[0] = (byte)134;
		ocp.write(data);
	}
	
	public void Cover()
	{
		byte[] data = new byte[1];
		data[0] = (byte)135;
		ocp.write(data);
	}
	
	public void Demo(int demoMode)
	{
		if (demoMode <= 9 && demoMode >= -1)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)136;	
			data[1] = (byte)demoMode;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Demo mode must be between -1 and 9 (inclusive).");
		}
	}
	
	public void Drive(int velocityHigh, int velocityLow, int radiusHigh, int radiusLow)
	{
		if (velocityHigh + velocityLow >= -500 && velocityHigh + velocityLow <= 500 &&
				radiusHigh + radiusLow >= -2000 && radiusHigh + radiusLow <= 2000)
		{
			byte[] data = new byte[5];
			data[0] = (byte)137;
			data[1] = (byte)velocityHigh;
			data[2] = (byte)velocityLow;
			data[3] = (byte)radiusHigh;
			data[4] = (byte)radiusLow;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Velocity values must be between -500 and 500, and radius values must be between -2000 and 2000 (inclusive).");
		}
	}
	
	public void LowSideDrivers(int outputBits)
	{
		if (outputBits <= 7 && outputBits >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)138;
			data[1] = (byte)outputBits;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Output bits must be between 0 and 7 (inclusive).");
		}
	}
	
	public void LEDs(int LEDBits, int color, int intensity)
	{
		if (LEDBits <= 10 && LEDBits >= 0)
		{
			byte[] data = new byte[4];	
			data[0] = (byte)138;
			data[1] = (byte)LEDBits;
			data[2] = (byte)color;
			data[3] = (byte)intensity;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: LED bits must be between 0 and 10 and color/intensity must be between 0 and 255 (inclusive).");
		}
	}
	
	public void Song()
	{
		// TODO: Write this method.
	}
	
	public void Play(int songNumber)
	{
		if (songNumber <= 15 && songNumber >= 0)
		{
			byte[] data = new byte[1];
			data[0] = (byte)songNumber;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Song number must be between 0 and 15 (inclusive).");
		}
	}
	
	public void CoverAndDock()
	{
		byte[] data = new byte[1];
		data[0] = (byte)143;
	}
	
	public void PMWLowSideDrivers(int lsd2, int lsd1, int lsd0)
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
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Low Side Driver Duty Cycle values must be between 0 and 128 (inclusive).");
		}
	}
	
	public void DriveDirect(int rightHigh, int rightLow, int leftHigh, int leftLow)
	{
		if (rightHigh + rightLow >= -500 && rightHigh + rightLow <= 500 &&
				leftHigh + leftLow >= -500 && leftHigh + leftLow <= 500)
		{
			byte[] data = new byte[5];
			data[0] = (byte)145;
			data[1] = (byte)rightHigh;
			data[2] = (byte)rightLow;
			data[3] = (byte)leftHigh;
			data[4] = (byte)leftLow;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Velocity values must be between -500 and 500 (inclusive).");
		}
	}
	
	public void DigitalOutputs(int outputBits)
	{
		if (outputBits <= 7 && outputBits >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)147;
			data[1] = (byte)outputBits;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Output bits must be between 0 and 7 (inclusive).");
		}
	}
	
	public void Stream()
	{
		// TODO: Write this method.
	}
	
	public void QueryList()
	{
		// TODO: Write this method.
	}
	
	public void PauseResumeStream(int range)
	{
		if (range == 0 || range == 1)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)150;
			data[1] = (byte)range;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Range must be 0 or 1.");
		}
	}
	
	public void SendIR(int value)
	{
		if (value <= 255 && value >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)151;
			data[1] = (byte)value;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Value must be between 0 and 255 (inclusive).");
		}
	}
	
	public void Script()
	{
		//TODO: Write this method.
	}
	
	public void PlayScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)153;
		ocp.write(data);
	}
	
	public void StopScript()
	{
		byte[] data = new byte[1];
		data[0] = (byte)154;
		ocp.write(data);
	}
	
	public void WaitTime(int time)
	{
		// Note: Time is measured in tenths of a second.
		if (time <= 255 && time >= 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)155;
			data[1] = (byte)time;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Time must be between 0 and 255 (inclusive).");
		}
	}
	
	public void WaitDistance(int highDistance, int lowDistance)
	{
		if (highDistance + lowDistance <= 32768 && highDistance + lowDistance >= -32768)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)156;
			// Note: Assuming that the high distance byte must be sent before the low distance byte.
			data[1] = (byte)highDistance;
			data[2] = (byte)lowDistance;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Distance must be between -32768 and 32768 (inclusive).");
		}
	}
	
	public void WaitAngle(int highAngle, int lowAngle)
	{
		if (highAngle + lowAngle <= 32768 && highAngle + lowAngle >= -32768)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)157;
			// Note: Assuming that the high angle byte must be sent before the low angle byte.
			data[1] = (byte)highAngle;
			data[2] = (byte)lowAngle;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Angle must be between -32768 and 32768 (inclusive).");
		}
	}
	
	public void WaitEvent(int eventID)
	{
		if (eventID <= 20 && eventID >= -20 && eventID != 0)
		{
			byte[] data = new byte[2];	
			data[0] = (byte)158;
			data[1] = (byte)eventID;
			ocp.write(data);
		}
		else
		{
			System.err.println("Error: Event ID must be between -20 and 20 (inclusive), excluding 0.");
		}
	}
}
