

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

	public  byte[] PauseResumeStream(int range);

	public  byte[] SendIR(int value);

	public  byte[] Script();

	public  byte[] PlayScript();

	public  byte[] StopScript();

	public  byte[] WaitTime(int time);

	public  byte[] WaitDistance(int highDistance, int lowDistance);

	public  byte[] WaitAngle(int highAngle, int lowAngle);

	public  byte[] WaitEvent(int eventID);
	
	/**
	 * 
	 * @param packetId
	 * @return the relevant command
	 */
	public byte[] querySingleSensor(SensorData.PACKET_IDS packetId);
	public byte[] querySensorList(SensorData.PACKET_IDS[] packetIds);
	public byte[] streamSensorList(SensorData.PACKET_IDS[] packetIds);

}