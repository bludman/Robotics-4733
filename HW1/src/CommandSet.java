/**
 * Interface with the list of basic operations the iRobotCreate can perform.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 *
 */
public interface CommandSet 
{
	/**
	 * Starts the Open Interface.
	 * @return The command written.
	 */
	public byte[] Start();

	/**
	 * Sets the baud rate.
	 * @param baudCode Baud code corresponding to a baud rate.
	 * @return The command written.
	 */
	public byte[] Baud(int baudCode);

	/**
	 * Unknown function, listed in the Command Quick Reference.
	 * @return The command written.
	 */
	public byte[] Control();

	/**
	 * Puts the iRobot Create into Safe Mode.
	 * @return The command written.
	 */
	public byte[] Safe();

	/**
	 * Puts the iRobot Create into Full Mode.
	 * @return The command written.
	 */
	public byte[] Full();

	/**
	 * Starts the iRobot Create's Spot Cover demo.
	 * @return The command written.
	 */
	public byte[] Spot();

	/**
	 * Starts the iRobot Create's Cover demo.
	 * @return The command written.
	 */
	public byte[] Cover();

	/**
	 * Starts the requested built in demo.
	 * @param demoMode the selected demo.
	 * @return The command written.
	 */
	public byte[] Demo(int demoMode);

	/**
	 * Controls the iRobot Create's drive wheels.
	 * @param velocity Speed value between -500 mm/s and 500 mm/s.
	 * @param radius Measured from the center of the turning circle to the center of the iRobot Create. Value between -2000 mm and 2000 mm.
	 * @return The command written.
	 */
	public byte[] Drive(int velocity, int radius);

	/**
	 * Controls the three low side drivers.
	 * @param outputBits The state of each low side driver.
	 * @return The command written.
	 */
	public byte[] LowSideDrivers(int outputBits);

	/**
	 * Controls the iRobot Create's LEDs.
	 * @param LEDBits State of the Play and Advance LEDs.
	 * @param color Specified LED color.
	 * @param intensity Specified LED intensity.
	 * @return The command written.
	 */
	public byte[] LEDs(int LEDBits, int color, int intensity);

	/**
	 * Specifies songs that the iRobot Create can play at a later time. Not currently implemented.
	 * @return The command written.
	 */
	public byte[] Song();

	/**
	 * Selects a song to play from those added to the iRobot Create.
	 * @param songNumber The song to select.
	 * @return The command written.
	 */
	public byte[] Play(int songNumber);

	/**
	 * Starts the iRobot Create's Cover and Dock demo.
	 * @return The command written.
	 */
	public byte[] CoverAndDock();

	/**
	 * Controls the three low side drivers with variable power.
	 * @param lsd2 PMW duty cycle for Low Side Driver 2.
	 * @param lsd1 PMW duty cycle for Low Side Driver 1.
	 * @param lsd0 PMW duty cycle for Low Side Driver 0.
	 * @return The command written.
	 */
	public byte[] PMWLowSideDrivers(int lsd2, int lsd1, int lsd0);

	/**
	 * Controls the forward and backward motion of the iRobot Create's drive wheels.
	 * @param right Speed value of the right wheel.
	 * @param left Speed value of the left wheel.
	 * @return The command written.
	 */
	public byte[] DriveDirect(int right, int left);

	/**
	 * Controls the state of the digital output pins on the Cargo Bay Connector.
	 * @param outputBits Digital output of the pins.
	 * @return The command written.
	 */
	public byte[] DigitalOutputs(int outputBits);

	public byte[] PauseResumeStream(int range);

	/**
	 * Sends the requested byte out of Low Side Driver 1 using the format expected by the iRobotCreate's IR reciever.
	 * @param value The byte to be sent.
	 * @return The command written.
	 */
	public byte[] SendIR(int value);

	/**
	 * Specifies a script to be played later. Currently not implemented.
	 * @return The command written.
	 */
	public byte[] Script();

	/**
	 * Loads a previously defined script to be played.
	 * @return The command written.
	 */
	public byte[] PlayScript();

	/**
	 * Returns the values of a previously stored script.
	 * @return The command written.
	 */
	public byte[] ShowScript();

	/**
	 * Forces the iRobot Create to wait until the specified time has elapsed before reacting to further input.
	 * @param time The time to wait in ms.
	 * @return The command written.
	 */
	public byte[] WaitTime(int time);

	/**
	 * Forces the iRobot Create to wait until it has traveled the specified distance before reacting to further input.
	 * @param distance The distance to wait in mm.
	 * @return The command written.
	 */
	public byte[] WaitDistance(int distance);

	/**
	 * Forces the iRobot Create to wait until it has turned the specified angle before reacting to further input.
	 * @param angle The rotation angle to wait in degrees.
	 * @return The command written.
	 */
	public byte[] WaitAngle(int angle);

	/**
	 * Forces the iRobot Create to wait until it detects the specified event bfeore reacting to further input.
	 * @param eventID The event number to wait for.
	 * @return The command written.
	 */
	public byte[] WaitEvent(int eventID);
	
	/**
	 * 
	 * @param packetId
	 * @return the relevant command
	 */
	public byte[] querySingleSensor(SensorData.PACKET_IDS packetId);
	public byte[] querySensorList(SensorData.PACKET_IDS[] packetIds);
	public byte[] streamSensorList(SensorData.PACKET_IDS[] packetIds);

}