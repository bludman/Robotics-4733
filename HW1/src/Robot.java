/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Robot {
	OpenCommPort ocp;
	CommandSet command;
	SensorData sensors;
	
	
	public class NotImplementedException extends RuntimeException{
		 public NotImplementedException(String message) {
          super(message);
       }
	};
	
	
	Robot()
	{
		ocp= new OpenCommPort();
		command = new CommandSetImpl();
		sensors = new SensorData();
	}
	
	public  byte[] Start(){ throw new NotImplementedException("TODO");}

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

	public  byte[] Stream(){ throw new NotImplementedException("TODO");}

	public  byte[] QueryList(){ throw new NotImplementedException("TODO");}

	public  byte[] PauseResumeStream(int range){ throw new NotImplementedException("TODO");}

	public  byte[] SendIR(int value){ throw new NotImplementedException("TODO");}

	public  byte[] Script(){ throw new NotImplementedException("TODO");}

	public  byte[] PlayScript(){ throw new NotImplementedException("TODO");}

	public  byte[] StopScript(){ throw new NotImplementedException("TODO");}

	public  byte[] WaitTime(int time){ throw new NotImplementedException("TODO");}

	public  byte[] WaitDistance(int highDistance, int lowDistance){ throw new NotImplementedException("TODO");}

	public  byte[] WaitAngle(int highAngle, int lowAngle){ throw new NotImplementedException("TODO");}

	public  byte[] WaitEvent(int eventID){ throw new NotImplementedException("TODO");}

	
	
}
