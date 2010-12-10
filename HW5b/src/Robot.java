/**
 * A wrapper class that allows easy interaction with (and extension of) the iRobot Create.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 */
public class Robot 
{
	/*
	 * Note: Some of these methods are analogous to those specified in CommandSet.java. 
	 * Please refer to that file for the documentation of each method. For any additional
	 * methods, documentation has been specified in this file.
	 */
	
	/** OpenCommPort object that allows communication with the iRobot Create. */
	protected OpenCommPort ocp;
	
	/** An instance of the CommandSet interface, containing basic operations for the iRobot Create. */
	private CommandSet commandSet;
	
	/**
	 * Constructor for the robot that instantiates the above objects.
	 */
	Robot()
	{
		ocp = new OpenCommPort();
		commandSet = new CommandSetImpl();
	}
	
	/**
	 * Set up the communication interface with the robot
	 * @return whether or not the robot was set up correctly
	 */
	public boolean setup()
	{
		if(ocp.setUpBam() == 0)
			return true;
		
		return false;
	}
	
	/** Start the open interface with the robot. Should be called before sending commands */
	public void start()
	{ 
		ocp.write(commandSet.start());
	}

	public void baud(int baudCode)
	{
		ocp.write(commandSet.baud(baudCode));
	}

	public void control()
	{
		ocp.write(commandSet.control());
	}

	/**
	 * Put the robot into safe mode
	 */
	public void safe()
	{
		ocp.write(commandSet.safe());
	}

	/**
	 * Put the robot into full mode
	 */
	public void full()
	{
		ocp.write(commandSet.full());
	}

	public void spot()
	{
		ocp.write(commandSet.spot());
	}

	public void cover()
	{
		ocp.write(commandSet.cover());
	}

	public void demo(int demoMode)
	{
		ocp.write(commandSet.demo(demoMode));
	}

	public void drive(int velocity, int radius)
	{ 
		ocp.write(commandSet.drive(velocity, radius));
	}

	public byte lowSideDrivers(int outputBits)
	{
		ocp.write(commandSet.lowSideDrivers(outputBits));
		return (byte)outputBits;
	}

	public void setLEDs(int LEDBits, int color, int intensity)
	{
		ocp.write(commandSet.setLEDs(LEDBits, color, intensity));
	}

	public void song()
	{
		ocp.write(commandSet.song());
	}

	public void play(int songNumber)
	{
		ocp.write(commandSet.play(songNumber));
	}

	public void coverAndDock()
	{
		ocp.write(commandSet.coverAndDock());
	}

	public void PMWLowSideDrivers(int lsd2, int lsd1, int lsd0)
	{
		ocp.write(commandSet.PMWLowSideDrivers(lsd2, lsd1, lsd0));
	}

	public void driveDirect(int right, int left)
	{
		ocp.write(commandSet.driveDirect(right, left));
	}

	public byte digitalOutputs(int outputBits)
	{
		ocp.write(commandSet.digitalOutputs(outputBits));
		return (byte)outputBits;
	}

	public void pauseResumeStream(int range)
	{
		ocp.write(commandSet.pauseResumeStream(range));
	}

	public void sendIR(int value)
	{
		ocp.write(commandSet.sendIR(value));
	}

	public void script()
	{
		ocp.write(commandSet.script());
	}

	public void playScript()
	{
		ocp.write(commandSet.playScript());
	}

	public void showScript()
	{
		ocp.write(commandSet.showScript());
	}

	public void waitTime(int time)
	{
		ocp.write(commandSet.waitTime(time));
	}

	public void waitDistance(int distance)
	{
		ocp.write(commandSet.waitDistance(distance));
	}

	public void waitAngle(int angle)
	{
		ocp.write(commandSet.waitAngle(angle));
	}

	public void waitEvent(int eventID)
	{
		ocp.write(commandSet.waitEvent(eventID));
	}
	
	/**
	 * Instructs the iRobot Create to travel in the shape of pointA square.
	 * @param driveVelocity The velocity at which the iRobot Create will travel in mm/s.
	 * @param squareSideLength The distance of one side of the square in mm.
	 * @return
	 */
	public void driveSquare(int driveVelocity, int squareSideLength)
	{
		for (int i = 0; i < 4; i++)
		{
			ocp.write(commandSet.driveDirect(driveVelocity, driveVelocity));
			ocp.write(commandSet.waitDistance(squareSideLength));
			ocp.write(commandSet.driveDirect(driveVelocity, -driveVelocity));
			ocp.write(commandSet.waitAngle(90));
		}
		stop();
	}
	
	/**
	 * Stops the robot from moving.
	 */
	public void stop()
	{
		ocp.write(commandSet.driveDirect(0, 0));
	}
	
	public static enum DIRECTION{RIGHT,LEFT};
	
	/**
	 * Turn the robot in place
	 * @param direction which way
	 * @param speed how fast
	 */
	public void turnInPlace(DIRECTION direction, int speed){
		if(direction==DIRECTION.RIGHT)
			driveDirect(-speed, speed);
		else if (direction==DIRECTION.LEFT)
			driveDirect(speed, -speed);
	}
	
	/**
	 * Turn the robot in place pointA certain number of degrees
	 * @param speed how fast
	 * @param degrees how many
	 */
	public void turnDegrees(int speed, int degrees)
	{
		if (degrees < 0)
		{
			turnInPlace(DIRECTION.RIGHT, speed);
			waitAngle(degrees);
			stop();
		}
		else if(degrees > 0)
		{
			turnInPlace(DIRECTION.LEFT, speed);
			waitAngle(degrees);
			stop();
		}
	}
	
	/**
	 * Drive straight pointA certain distance at pointA certain speed
	 * NOTE: to go backwards, both speed and distance should be negative
	 * @param speed
	 * @param distance
	 */
	public void driveDistance(int speed, int distance)
	{
		driveDirect(speed, speed);
		waitDistance(distance);
		stop();
	}
	
	/**
	 * Get the angle that the iRobot Create has rotated since this was last called.
	 */
	public byte[] getAngle()
	{
		byte[] command = new byte[2];
		command[0] = (byte)142;
		command[1] = (byte)20;
		byte[] recieved = new byte[2];
		
		recieved = new byte[2];
		ocp.write(command);
		ocp.read(recieved);
		
		return recieved;
	}
	
	/**
	 * Write a command directly to the robot
	 * @param command
	 */
	protected void write(byte[] command)
	{
		this.ocp.write(command);
	}
}
