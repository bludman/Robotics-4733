import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Benjamin Ludman, Mike Hernandez
 * Implementation for a wall-following iRobot Create.
 */
public class WallFollowingRobot extends Robot 
{
	final private static int TRIGGER=1;
	private List<PositionAndOrientation> trackedPositions;
	
	/**
	 * @return the trackedPositions
	 */
	public List<PositionAndOrientation> getTrackedPositions() {
		return trackedPositions;
	}



	/** Enumeration to keep track of what state the iRobot Create is in. */
	private enum DriveState{findingWall,turning, trackingWall, done};
	
	/** Default speed of the iRobot Create. */
	private final static int DEFAULT_BASE_SPEED = 120;
	
	/** Default speed by which to correct when following the wall. */
	private final static int DEFAULT_CORRECTION_SPEED = 12;
	
	/** Base speed of the iRobot Create, before adjustments. */
	private int baseSpeed;
	
	/** Adjustment to the base speed when corrections are necessary. */
	private int wheelSpeed;
	
	/** Amount to turn when contact with a wall is detected. */
	private int turnAngleIncrement;
	
	/** Distance to keep from wall */
	private int holdDistance;
	
	/** Tolerance for holdDistance */
	private int tolerance;
	
	/** Distance to move back from a wall after making contact. */
	private int backoffDistance;
	
	/** Speed at which to move away from a wall after making contact. */
	private int backoffSpeed;
	
	/** State of the iRobot Create. */
	private DriveState state;
	
	/** 
	 * Set up various parameters for the new robot.
	 */
	public WallFollowingRobot()
	{
		baseSpeed = DEFAULT_BASE_SPEED;
		wheelSpeed = 0;
		turnAngleIncrement = 30;
		holdDistance = 40;
		tolerance = 15;
		backoffDistance = 20;
		backoffSpeed = 100;
		
		state = DriveState.findingWall;
	}

	/**
	 * Finds and follows walls
	 * @return a list of positions and orientions the robot was in when following wall
	 */
	public Object findAndFollowWall(boolean trackPositions)
	{
		/** Set initial state so the iRobot Create searches for a wall. */
		state = DriveState.findingWall;
		int counter = 1;
		
		trackedPositions= new ArrayList<PositionAndOrientation>(); //reset the tracked positions
		this.positionAndOrientation = new PositionAndOrientation(0, 0, 90); //reset robot's positions
		
		/** iRobot Create starts moving. */
		while (state != DriveState.done)
		{
			
			/** Track the changed positions only every "counter" iterations */
			if(trackPositions && counter==TRIGGER)
			{
				//Figure out where the iRobot Create is add it to the list or tracked positions 
				//trackedPositions.add(this.updateMyPosition());
				PositionAndOrientation p=this.updateMyPosition();
				System.out.println(p.getX()+","+p.getY()+","+p.getAngle());
				trackedPositions.add(p);
				counter=1; //reset counter
			}
			else
			{
				counter++;
			}
			
			try 
			{
                if (Thread.interrupted()) {
                	throw new InterruptedException("Exiting");
                }
                Thread.sleep(50);
	        }
	        catch (InterruptedException e) {
	        	return trackedPositions; //stop running the algorithm and return
	        }
			
			/** iRobot Create will remain in this state until it hits a wall. */
			if (state == DriveState.findingWall)
			{
				driveDirect(baseSpeed, baseSpeed);//Driving towards wall
				this.waitEvent((byte) 5); //Waiting for wall
				
				/** Performs a persistent check to see if the iRobot Create has made contact with a wall. */
				while (!hitsWall())
					;//Spinning
				
				
				/** Initiate turning sequence once the wall has been hit. */
				state=DriveState.turning;
			}
			
			/** Initiated once the iRobot Create must turn and avoid a wall. */
			if (state == DriveState.turning)
			{
				/** Move back, turn, and move forward. */
				driveDistance(-backoffSpeed, -backoffDistance);
				turnDegrees(baseSpeed, turnAngleIncrement);
				driveDistance(backoffSpeed, backoffDistance);
				
				/** Put the iRobot Create to sleep in order to perform a rotation without accidentally
				 *  reading additional contact with the bump sensors.
				 */
				try 
				{
					Thread.sleep(1500);
				} 
				catch (InterruptedException e) 
				{	
					return trackedPositions; //stop running the algorithm and return
				}

				/** Once the turn is complete, begin following the wall. */
				state = DriveState.trackingWall;
				
			}
			
			/** The iRobot Create will follow a wall, and adjust its trajectory accordingly. */
			if(state == DriveState.trackingWall)
			{
				/** Check to see if contact has been made with a wall. */
				if(hitsWall())
				{
					/** Begin turning to compensate for contact made with the wall. */
					state = DriveState.turning;
				}
				/** Contact has not been made. Keep adjusting path. */
				else
				{
					int wallSignal = readWallDistance();
										
					/** iRobot Create is too far from the wall. Veer right. */
					if (wallSignal == 0 || wallSignal < (holdDistance - tolerance))
					{
						wheelSpeed = -DEFAULT_CORRECTION_SPEED;
						baseSpeed = DEFAULT_BASE_SPEED - 30;
					}
					/** iRobot Create is too close to the wall. Veer Left. */
					else if (wallSignal < 0 || wallSignal > (holdDistance + tolerance))
					{
						wheelSpeed = DEFAULT_CORRECTION_SPEED;
						baseSpeed = DEFAULT_BASE_SPEED - 30;
					}
					/** iRobot Create is at a good distance from the wall. Speed up a bit. */
					else
					{
						wheelSpeed = 0;
						baseSpeed = DEFAULT_BASE_SPEED;
					}
					
					/** Proceed forward with any applicable adjustments. */
					driveDirect(baseSpeed + wheelSpeed, baseSpeed - wheelSpeed);
				}
			}
		}
		
		return trackedPositions;
	}

	
	/**
	 * Checks to see if contact with a wall has been made.
	 * @return hit A boolean value representing whether or not a wall was hit.
	 */
	private boolean hitsWall() 
	{
		byte[] data = new byte[1];
		data = this.readBumpsAndWheelDrops();
		
		/** Check to see if either the left or the right bump sensor has been hit. */
		boolean hit = getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_LEFT, data[0]) || 
						getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_RIGHT, data[0]);
		// System.out.println("Hit: "+hit);
		return hit;
	}

	/**
	 * Determine how far the iRobot Create is from the wall.
	 * @return distance The signal value returned from the iRobot Create.
	 */
	private int readWallDistance() 
	{
		byte[] data = this.readWallSignal();
		
		/** Adjust returned data as specified in the original template program. */
		int distance = (int)((data[0] << 8) & 0x00FF) + (int)(data[1] & 0x00FF);
		// System.out.println("Distance: "+distance);
		return distance;
		
	}

	
	
	/**
	 * Get the boolean value of a specific sensor in the BUMPS_AND_WHEEL_DROPS sensor packet 
	 * @param sensor the sensor to read
	 * @param rawData the raw data from which to interpret the value
	 * @return
	 */
	public static boolean getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS sensor, byte rawData){
		/*
		 * Get the raw value of the BUMPS_AND_WHEEL_DROPS sensor and then parse the
		 * specific sensor boolean value using a bitwise AND to get the desired bit
		 */
		return (rawData & (1 << sensor.ordinal())) == 0 ? false : true;
	}
}
