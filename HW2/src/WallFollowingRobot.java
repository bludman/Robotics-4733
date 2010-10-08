import java.util.Arrays;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class WallFollowingRobot extends Robot 
{
	private enum DriveState{findingWall,turning, trackingWall, done};
	
	private int baseSpeed=150;
	private int wheelSpeed=0;
	private int turnAngleIncrement= 30;
	/** Distance to keep from wall */
	private int holdDistance=30;
	/** Tolerance for holdDistance */
	private int tolerance = 0;
	private int backoffDistance=30;
	private int backoffSpeed=100;
	private int correctionSpeed = 40;
	DriveState state= DriveState.findingWall;
	
	public void kill()
	{
		System.out.println("killing");
		state=DriveState.done;
	}


	public void findAndFollowWall()
	{
		state=DriveState.findingWall;
		
		while(state!=DriveState.done)
		{
			if(state==DriveState.findingWall)
			{
				//driveUntil(baseSpeed,Bump);
				System.out.println("Finding wall");
				driveDirect(baseSpeed, baseSpeed);
				System.out.println("Driving towards wall");
				this.waitEvent((byte)5);
				System.out.println("waiting for wall");
				while(!hitsWall())
					System.out.println("Spinning");
				
				System.out.println("not spinning");
				state=DriveState.turning;
			}
			
			if(state==DriveState.turning)
			{
				System.out.println("turning");
				driveDistance(-backoffSpeed,-backoffDistance); //back up a bit
				turnDegrees(baseSpeed, turnAngleIncrement);
				driveDistance(backoffSpeed, backoffDistance);
				
				try {
					System.out.println("Sleeping");
					Thread.sleep(2000);
					System.out.println("waking");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				state=DriveState.trackingWall;
				
			}
			
			if(state==DriveState.trackingWall)
			{
				System.out.println("Tracking wall");
				//this.start(); //TODO: take me out, jsut for easy debugging
						
				if(hitsWall())
				{
					this.stop();
					state=DriveState.turning;
				}
				else
				{
					int distanceToWall=readWallDistance();
										
					if(distanceToWall<=0 || distanceToWall>holdDistance+tolerance)
						wheelSpeed=-correctionSpeed;
					else if(distanceToWall >0 && distanceToWall<(holdDistance-tolerance) ) //too close, turn left
						wheelSpeed=correctionSpeed;
					else
						wheelSpeed= 0;
					
					
					driveDirect(baseSpeed+wheelSpeed, baseSpeed-wheelSpeed);
				}
				
				
				
				
			}
		}
		
		
		System.out.println("done");
		
		
	}

	

	public void findAndFollowWall2()
	{
		state=DriveState.findingWall;
		
		while(state!=DriveState.done)
		{
			if(state==DriveState.findingWall)
			{
				//driveUntil(baseSpeed,Bump);
				System.out.println("Finding wall");
				driveDirect(baseSpeed, baseSpeed);
				System.out.println("Driving towards wall");
				this.waitEvent((byte)5);
				System.out.println("waiting for wall");
				while(!hitsWall())
					System.out.println("Spinning");
				
				System.out.println("not spinning");
				state=DriveState.turning;
			}
			
			while(state==DriveState.turning)
			{
				System.out.println("turning");
				driveDistance(-backoffSpeed,-backoffDistance); //back up a bit
				turnDegrees(baseSpeed, turnAngleIncrement);
				driveDistance(backoffSpeed, backoffDistance);
				
				try {
					System.out.println("Sleeping");
					Thread.sleep(2000);
					System.out.println("waking");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				state=DriveState.trackingWall;
				
			}
			
			while(state==DriveState.trackingWall)
			{
				System.out.println("Tracking wall");
				//this.start(); //TODO: take me out, jsut for easy debugging
						
				if(hitsWall())
				{
					this.stop();
					state=DriveState.turning;
//				
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				else
				{
					int distanceToWall=readWallDistance();
					
					
					if(distanceToWall<=0 || distanceToWall>holdDistance+tolerance)
						wheelSpeed=-correctionSpeed;
					else if(distanceToWall >0 && distanceToWall<(holdDistance-tolerance) ) //too close, turn left
						wheelSpeed=correctionSpeed;
					else
						wheelSpeed= 0;
					
					
					driveDirect(baseSpeed+wheelSpeed, baseSpeed-wheelSpeed);
				}
				
				
				
				
			}
		}
		
		
		System.out.println("done");
		
		
	}
	
	
	private boolean hitsWall() {
		byte[] data = new byte[1];
		data = this.readBumpsAndWheelDrops();
		boolean hit=getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_LEFT, data[0]) || getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_RIGHT, data[0]);
		System.out.println("Hit: "+hit);
		return hit;
	}

	private int readWallDistance() {
		byte[] data = this.readWallSignal();
		int distance= (int)(data[0] << 8) + data[1];
		System.out.println("Distance: "+distance);
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
