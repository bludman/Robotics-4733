

public interface CommandSet {

	public  byte[] Start();

	public  byte[] Baud(int baudCode);

	public  byte[] Control();

	/**
	 * Put robot in safe mode
	 * @return
	 */
	public  byte[] Safe();

	public  byte[] Full();

	public  byte[] Spot();

	public  byte[] Cover();

	public  byte[] Demo(int demoMode);

	public  byte[] Drive(int velocityHigh, int velocityLow,
			int radiusHigh, int radiusLow);

	public  byte[] LowSideDrivers(int outputBits);

	public  byte[] LEDs(int LEDBits, int color, int intensity);

	public  byte[] Song();

	public  byte[] Play(int songNumber);

	public  byte[] CoverAndDock();

	public  byte[] PMWLowSideDrivers(int lsd2, int lsd1, int lsd0);

	public  byte[] DriveDirect(int rightHigh, int rightLow, int leftHigh,
			int leftLow);

	public  byte[] DigitalOutputs(int outputBits);

	public  byte[] Stream();

	public  byte[] QueryList();

	public  byte[] PauseResumeStream(int range);

	public  byte[] SendIR(int value);

	public  byte[] Script();

	public  byte[] PlayScript();

	public  byte[] StopScript();

	public  byte[] WaitTime(int time);

	public  byte[] WaitDistance(int highDistance, int lowDistance);

	public  byte[] WaitAngle(int highAngle, int lowAngle);

	public  byte[] WaitEvent(int eventID);
	
	public SensorData querySingleSensor(int packetId);
	public SensorData querySensorList(int[] packetIds);
	public SensorData streamSensorList(int[] packetIds);

}