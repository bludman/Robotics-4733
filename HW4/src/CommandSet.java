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
	public byte[] start();

	/**
	 * Sets the baud rate.
	 * @param baudCode Baud code corresponding to a baud rate.
	 * @return The command written.
	 */
	public byte[] baud(int baudCode);

	/**
	 * Unknown function, listed in the Command Quick Reference.
	 * @return The command written.
	 */
	public byte[] control();

	/**
	 * Puts the iRobot Create into Safe Mode.
	 * @return The command written.
	 */
	public byte[] safe();

	/**
	 * Puts the iRobot Create into Full Mode.
	 * @return The command written.
	 */
	public byte[] full();

	/**
	 * Starts the iRobot Create's Spot Cover demo.
	 * @return The command written.
	 */
	public byte[] spot();

	/**
	 * Starts the iRobot Create's Cover demo.
	 * @return The command written.
	 */
	public byte[] cover();

	/**
	 * Starts the requested built in demo.
	 * @param demoMode the selected demo.
	 * @return The command written.
	 */
	public byte[] demo(int demoMode);

	/**
	 * Controls the iRobot Create's drive wheels.
	 * @param velocity Speed value between -500 mm/s and 500 mm/s.
	 * @param radius Measured from the center of the turning circle to the center of the iRobot Create. Value between -2000 mm and 2000 mm.
	 * @return The command written.
	 */
	public byte[] drive(int velocity, int radius);

	/**
	 * Controls the three low side drivers.
	 * @param outputBits The state of each low side driver.
	 * @return The command written.
	 */
	public byte[] lowSideDrivers(int outputBits);

	/**
	 * Controls the iRobot Create's LEDs.
	 * @param LEDBits State of the Play and Advance LEDs.
	 * @param color Specified LED color.
	 * @param intensity Specified LED intensity.
	 * @return The command written.
	 */
	public byte[] setLEDs(int LEDBits, int color, int intensity);

	/**
	 * Specifies songs that the iRobot Create can play at a later time. Not currently implemented.
	 * @return The command written.
	 */
	public byte[] song();

	/**
	 * Selects a song to play from those added to the iRobot Create.
	 * @param songNumber The song to select.
	 * @return The command written.
	 */
	public byte[] play(int songNumber);

	/**
	 * Starts the iRobot Create's Cover and Dock demo.
	 * @return The command written.
	 */
	public byte[] coverAndDock();

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
	public byte[] driveDirect(int right, int left);

	/**
	 * Controls the state of the digital output pins on the Cargo Bay Connector.
	 * @param outputBits Digital output of the pins.
	 * @return The command written.
	 */
	public byte[] digitalOutputs(int outputBits);

	public byte[] pauseResumeStream(int range);

	/**
	 * Sends the requested byte out of Low Side Driver 1 using the format expected by the iRobotCreate's IR reciever.
	 * @param value The byte to be sent.
	 * @return The command written.
	 */
	public byte[] sendIR(int value);

	/**
	 * Specifies a script to be played later. Currently not implemented.
	 * @return The command written.
	 */
	public byte[] script();

	/**
	 * Loads a previously defined script to be played.
	 * @return The command written.
	 */
	public byte[] playScript();

	/**
	 * Returns the values of a previously stored script.
	 * @return The command written.
	 */
	public byte[] showScript();

	/**
	 * Forces the iRobot Create to wait until the specified time has elapsed before reacting to further input.
	 * @param time The time to wait in ms.
	 * @return The command written.
	 */
	public byte[] waitTime(int time);

	/**
	 * Forces the iRobot Create to wait until it has traveled the specified distance before reacting to further input.
	 * @param distance The distance to wait in mm.
	 * @return The command written.
	 */
	public byte[] waitDistance(int distance);

	/**
	 * Forces the iRobot Create to wait until it has turned the specified angle before reacting to further input.
	 * @param angle The rotation angle to wait in degrees.
	 * @return The command written.
	 */
	public byte[] waitAngle(int angle);

	/**
	 * Forces the iRobot Create to wait until it detects the specified event bfeore reacting to further input.
	 * @param eventID The event number to wait for.
	 * @return The command written.
	 */
	public byte[] waitEvent(int eventID);
}