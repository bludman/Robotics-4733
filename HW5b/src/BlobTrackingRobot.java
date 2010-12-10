/**
 * Robot that tracks a specified object of a particular color.
 */

public class BlobTrackingRobot extends Robot 
{
	// Default speed of the robot
	private final int speed = 50;
	
	// Default turn angle of the robot
	private final int angle = 15;
	
	// Default distance the robot will drive
	private final int distance = 50;
	
	/* Instructs the robot to move in a particular direction.
	 * @param fb: Forward or backwards
	 * @param lr: Left or right
	 */
	public void driveInstructions(BlobTracker.DIRECTION fb, BlobTracker.DIRECTION lr)
	{
		if (lr == BlobTracker.DIRECTION.LEFT)
		{
			// Turn left
			this.turnDegrees(speed, angle);
		}
		else if (lr == BlobTracker.DIRECTION.RIGHT)
		{
			// Turn right
			this.turnDegrees(speed, -angle);
		}
		
		if (fb == BlobTracker.DIRECTION.FORWARDS)
		{
			// Drive forwards
			this.driveDirect(speed, speed);
		}
		else if (fb == BlobTracker.DIRECTION.BACKWARDS)
		{
			// Drive backwards
			this.driveDirect(-speed, -speed);
		}
		else
		{
			// No need to move, stop robot if necessary
			this.stop();
		}
	}

	/* Determines the angle between the robot's center and some point in the environment.
	 * @param width: the width of the image
	 * @param height: the height of the image
	 * @return the measure of the angle in degrees
	 */
	public static double findTurnAngle(int width, int height, int x, int y)
	{
		final int OFFSET = 10;
		int fromX = width / 2;
		int fromY = height + OFFSET;
		
		int toX = x - fromX;
		int toY = fromY - y;
		double theta  = Math.toDegrees(Math.atan2(toY, toX));
		return theta;
	}
}
