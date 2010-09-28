import java.util.Arrays;

/**
 * A wrapper class that allows easy interaction with (and extension of) the iRobot Create.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 */

public class Robot 
{
	// OpenCommPort object that allows communication with the iRobot Create.
	private OpenCommPort ocp;
	
	// An instance of the CommandSet interface, containing basic operations for the iRobot Create.
	private CommandSet commandSet;
	
	// Similar to the above, contains basic methods of obtaining sensor data for the iRobot Create.
	private SensorData sensors;
	
	/**
	 * Constructor for the robot that instantiates the above objects.
	 */
	Robot()
	{
		ocp= new OpenCommPort();
		commandSet = new CommandSetImpl();
		sensors = new SensorData();
	}
	
	/**
	 * Note: Some of these methods are analagous to those specified in CommandSet.java. 
	 * Please refer to that file for the documentation of each method. For any additional
	 * methods, documentation has been specified in this file.
	 */
	
	public boolean Setup()
	{
		if(ocp.setUpBam() == 0)
			return true;
		
		return false;
	}
	
	public void Start()
	{ 
		ocp.write(commandSet.Start());
	}

	public void Baud(int baudCode)
	{
		ocp.write(commandSet.Baud(baudCode));
	}

	public void Control()
	{
		ocp.write(commandSet.Control());
	}

	public void Safe()
	{
		ocp.write(commandSet.Safe());
	}

	public void Full()
	{
		ocp.write(commandSet.Full());
	}

	public void Spot()
	{
		ocp.write(commandSet.Spot());
	}

	public void Cover()
	{
		ocp.write(commandSet.Cover());
	}

	public void Demo(int demoMode)
	{
		ocp.write(commandSet.Demo(demoMode));
	}

	public void Drive(int velocity, int radius)
	{ 
		ocp.write(commandSet.Drive(velocity, radius));
	}

	public byte LowSideDrivers(int outputBits)
	{
		ocp.write(commandSet.LowSideDrivers(outputBits));
		return (byte)outputBits;
	}

	public void LEDs(int LEDBits, int color, int intensity)
	{
		ocp.write(commandSet.LEDs(LEDBits, color, intensity));
	}

	public void Song()
	{
		ocp.write(commandSet.Song());
	}

	public void Play(int songNumber)
	{
		ocp.write(commandSet.Play(songNumber));
	}

	public void CoverAndDock()
	{
		ocp.write(commandSet.CoverAndDock());
	}

	public void PMWLowSideDrivers(int lsd2, int lsd1, int lsd0)
	{
		ocp.write(commandSet.PMWLowSideDrivers(lsd2, lsd1, lsd0));
	}

	public void DriveDirect(int right, int left)
	{
		ocp.write(commandSet.DriveDirect(right, left));
	}

	public byte DigitalOutputs(int outputBits)
	{
		ocp.write(commandSet.DigitalOutputs(outputBits));
		return (byte)outputBits;
	}

	public void PauseResumeStream(int range)
	{
		ocp.write(commandSet.PauseResumeStream(range));
	}

	public void SendIR(int value)
	{
		ocp.write(commandSet.SendIR(value));
	}

	public void Script()
	{
		ocp.write(commandSet.Script());
	}

	public void PlayScript()
	{
		ocp.write(commandSet.PlayScript());
	}

	public void ShowScript()
	{
		ocp.write(commandSet.ShowScript());
	}

	public void WaitTime(int time)
	{
		ocp.write(commandSet.WaitTime(time));
	}

	public void WaitDistance(int distance)
	{
		ocp.write(commandSet.WaitDistance(distance));
	}

	public void WaitAngle(int angle)
	{
		ocp.write(commandSet.WaitAngle(angle));
	}

	public void WaitEvent(int eventID)
	{
		ocp.write(commandSet.WaitEvent(eventID));
	}
	
	/**
	 * Instructs the iRobot Create to travel in the shape of a square.
	 * @param moveVelocityValue The velocity at which the iRobot Create will travel in mm/s.
	 * @param moveDistanceValue The distance of one side of the square in mm.
	 * @return
	 */
	public void Square(int moveVelocityValue, int moveDistanceValue)
	{
		for (int i = 0; i < 3; i++)
		{
			ocp.write(commandSet.DriveDirect(moveVelocityValue, moveVelocityValue));
			ocp.write(commandSet.WaitDistance(moveDistanceValue));
			ocp.write(commandSet.DriveDirect(moveVelocityValue, -moveVelocityValue));
			ocp.write(commandSet.WaitAngle(90));
		}
	}
	
	/**
	 * Stops the robot from moving.
	 */
	public void Stop()
	{
		ocp.write(commandSet.DriveDirect(0, 0));
	}
	
	/**
	 * Query a single sensor and parse the returned data
	 * @param packet
	 */
	public void querySingleSensor(SensorData.PACKET_IDS packet)
	{
		byte[] command=commandSet.querySingleSensor(packet);
		System.out.println("Setting query command: "+Arrays.toString(command));
		sensors.setQueryCommand(command);
		System.out.println("Writing command");
		ocp.write(command);
		System.out.println("Wrote command");
		System.out.println("Reading result");
		ocp.read(sensors.getReadBuffer());
		System.out.println("Read result");
		System.out.println("Parsing result");
		sensors.processSensorData();
	}

	public void test()
	{	
		SetMode();
		LED_On();	
	}
	
	private void SetMode()
    {
		byte[] data = new byte[2];
		
		data[0] = (byte)128;  //start
        data[1] = (byte)131;  //Safe Mode
		
		ocp.write(data);
    }

    private void LED_On()
    {
        byte[] data = new byte[4];
        int numBytes = 4;

        data[0] = (byte)139;  //LED command
        data[1] = (byte)8;    //Select LED (Play : 8, power : 2) 
        data[2] = (byte)0;    //Color 0 = green, 255 = red
        data[3] = (byte)128;  //Intensity

        ocp.write(data);

        data[0] = (byte)139;  //LED command
        data[1] = (byte)2;    //Select LED (Play : 8, power : 2) 
        data[2] = (byte)255;    //Color 0 = green, 255 = red
        data[3] = (byte)128;  //Intensity

        ocp.write(data);     
    }
    
    public boolean getBumpOrWheelDropStatus(SensorData.BUMPS_AND_WHEEL_DROPS sensor)
    {
    	 return sensors.getBumpOrWheelDropStatus(sensor);
    }
	
}
