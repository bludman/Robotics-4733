/**
 * 
 */

/**
 * @author Ben
 *
 */
public class BlobTrackingRobot extends Robot {

	private final int speed = 100;
	private final int angle = 15;
	private final int distance = 100;
	
	public void driveInstructions(BlobTracker.DIRECTION fb, BlobTracker.DIRECTION lr)
	{
		if(lr==BlobTracker.DIRECTION.LEFT)
		{
			this.turnDegrees(speed, angle);
		}
		else if(lr==BlobTracker.DIRECTION.RIGHT)
		{
			this.turnDegrees(-speed, -angle);
		}
		
		if(fb==BlobTracker.DIRECTION.LEFT)
		{
			this.driveDistance(speed, distance);
		}
		else if(fb==BlobTracker.DIRECTION.RIGHT)
		{
			this.turnDegrees(-speed, -distance);
		}
	}
	
	public static double findTurnAngle(int width, int height, int x, int y)
	{
		final int OFFSET = 10;
		int fromX = width/2;
		int fromY = height + OFFSET;
		
		int toX = x-fromX;
		int toY = fromY-y;
		double theta  = Math.toDegrees(Math.atan2(toY, toX));
		return theta;
	}
}
