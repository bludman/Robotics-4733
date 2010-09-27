import java.util.Arrays;

import sun.management.Sensor;

/**
 * 
 */

/**
 * A wrapper class that allows easy interaction with the iRobot Create.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 *
 */
public class Robot {
	private OpenCommPort ocp;
	private CommandSet commandSet;
	private SensorData sensors;
	
	
	public class NotImplementedException extends RuntimeException{
		 public NotImplementedException(String message) {
          super(message);
       }
	};
	
	
	Robot()
	{
		ocp= new OpenCommPort();
		commandSet = new CommandSetImpl();
		sensors = new SensorData();
	}
	
	
	/**
	 * Setup communications with the robot
	 * @return if setup was succesfull, false if it wasn't
	 */
	public boolean Setup()
	{
		if(ocp.setUpBam()==0)
			return true;
		
		return false;
	}
	
	public void Start(){ 
			ocp.write(commandSet.Start());
	}

	public  byte[] Baud(int baudCode){ throw new NotImplementedException("TODO");}

	public  byte[] Control(){ throw new NotImplementedException("TODO");}

	public  byte[] Safe(){ throw new NotImplementedException("TODO");}

	public  byte[] Full(){ throw new NotImplementedException("TODO");}

	public  byte[] Spot(){ throw new NotImplementedException("TODO");}

	public  byte[] Cover(){ throw new NotImplementedException("TODO");}

	public  byte[] Demo(int demoMode){ throw new NotImplementedException("TODO");}

	public  byte[] Drive(int velocityHigh, int velocityLow,
			int radiusHigh, int radiusLow){ throw new NotImplementedException("TODO");}

	public  byte[] LowSideDrivers(int outputBits){ throw new NotImplementedException("TODO");}

	public  byte[] LEDs(int LEDBits, int color, int intensity){ throw new NotImplementedException("TODO");}

	public  byte[] Song(){ throw new NotImplementedException("TODO");}

	public  byte[] Play(int songNumber){ throw new NotImplementedException("TODO");}

	public  byte[] CoverAndDock(){ throw new NotImplementedException("TODO");}

	public  byte[] PMWLowSideDrivers(int lsd2, int lsd1, int lsd0){ throw new NotImplementedException("TODO");}

	public  byte[] DriveDirect(int rightHigh, int rightLow, int leftHigh,
			int leftLow){ throw new NotImplementedException("TODO");}

	public  byte[] DigitalOutputs(int outputBits){ throw new NotImplementedException("TODO");}

	public  byte[] PauseResumeStream(int range){ throw new NotImplementedException("TODO");}

	public  byte[] SendIR(int value){ throw new NotImplementedException("TODO");}

	public  byte[] Script(){ throw new NotImplementedException("TODO");}

	public  byte[] PlayScript(){ throw new NotImplementedException("TODO");}

	public  byte[] StopScript(){ throw new NotImplementedException("TODO");}

	public  byte[] WaitTime(int time){ throw new NotImplementedException("TODO");}

	public  byte[] WaitDistance(int highDistance, int lowDistance){ throw new NotImplementedException("TODO");}

	public  byte[] WaitAngle(int highAngle, int lowAngle){ throw new NotImplementedException("TODO");}

	public  byte[] WaitEvent(int eventID){ throw new NotImplementedException("TODO");}
	
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
