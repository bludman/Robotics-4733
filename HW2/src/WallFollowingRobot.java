/**
 * 
 */

/**
 * @author Ben
 *
 */
public class WallFollowingRobot extends Robot {

	public void findAndFollowWall()
	{
		while(!detectWall())
		{
			driveForward();
		}
		
	}
}
