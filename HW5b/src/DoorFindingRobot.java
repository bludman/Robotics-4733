import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;


/**
 * 
 */

/**
 * @author Ben and Mike
 *
 */
public class DoorFindingRobot extends Robot {

	private final int speed = 100;
	private final int knockSpeed = 200;
	private final int distance = 75;
	
	private int[][] range;
	private static final int LOW = 0;
	private static final int HIGH = 1;
	
	public enum STATE{SEARCHING,APPROACHING,KNOCKING,DONE};
	
	private STATE state= STATE.SEARCHING;
	private boolean canDrive = false;
	
	public DoorFindingRobot() 
	{
		setRange();
	}
	
	/** Configure the color range for the door we are looking for */
	private void setRange()
	{
		// Values to be returned, calculated using range
		final int numChannels = 3;
		final int RANGE = 30;
		int[] low = new int[numChannels];
		int[] high = new int[numChannels];
		int[] values = {61, 62, 92};
		
		for(int i=0;i<numChannels;i++)
		{
			low[i] = (int)values[i] - RANGE;
			high[i] = (int)values[i] + RANGE;
		}
		
		range =  new int[][] {low, high};
	}
	
	
	/**
	 * Do the next move towards the goal of knocking on the door
	 * @param bi the image the robot sees
	 * @param original the display for the image
	 * @param mask the display for the mask
	 */
	public void doMove(BufferedImage bi,JImageDisplay original, JImageDisplay mask)
	{
		switch(state)
		{
			//Approaching the door
			case APPROACHING:
			{
				System.out.println("Approaching");
				JBlob door = lookForDoor(bi,original,mask);
				if(door!=null)
					System.out.println("Door is "+door.getBoundingBox());
				else
					System.out.println("Door is null");
				
				if(hitsWall())
					this.state = STATE.KNOCKING;
				break;
			}
			//Knocking on the door
			case KNOCKING:
			{
				System.out.println("Knocking");
				try{
					this.driveDistance(-knockSpeed, -distance);
					this.driveDistance(knockSpeed, distance);
					this.driveDistance(-knockSpeed, -distance);
					this.driveDistance(knockSpeed, distance);
					this.createSong();
					this.playSong();
					this.stop();
					state=STATE.DONE;
					
				}
				catch (Exception e){
					System.err.println("Couldn't send instruction");
				}
				break;
			}
			case DONE:
			{
				break;
			}
			default:
			case SEARCHING:
			{
				System.out.println("Searching");
				JBlob door = lookForDoor(bi,original,mask);
				JPoint2D targetPoint = door.getPointBelow(door.getCentroid());
				
				try{
						driveTowards(targetPoint, original.getWidth(), original.getHeight());
						if(canDrive)
							this.state = STATE.APPROACHING;
				}catch (Exception e)
				{
					System.err.println("Couldn't send instruction");
				}
			}
		}
		
	}
	
	
	/**
	 * Drive towards the a specific target point
	 * @param targetPoint
	 * @param width the width of the view the target point is in
	 * @param height the height of the view the target point is in
	 */
	private void driveTowards(JPoint2D targetPoint, int width, int height) 
	{
		
			int angle = (int)DoorFindingRobot.findTurnAngle(width, height, targetPoint.getX(), targetPoint.getY());
			System.out.println("Angle is: "+angle);
			if(canDrive){
				this.turnDegrees(speed, angle);
				this.driveDirect(speed, speed);
			}
		
	}

	private JBlob lookForDoor(BufferedImage bi,JImageDisplay original, JImageDisplay mask) 
	{
		JImage ji = new JImage(bi);

		//specify the range of RGB channels
		JImage jmask = new JImage(bi);
		jmask = JImageProcessing.threshold(jmask, range[LOW], range[HIGH]);

		
		//blob detection
		Vector<JBlob> jbs = new Vector<JBlob>();
		JBlobDetector jbd = new JBlobDetector();
		jbs = jbd.findBlobs(jmask);
		
		original.setBlobs(jbs);
		mask.setBlobs(jbs);

		JBlob max = JBlob.findBiggestBlob(jbs, original.getWidth(), original.getHeight());
		
		original.bluePoint= max.getCentroid();
		mask.bluePoint= max.getCentroid();
		
		original.redPoint= max.getPointBelow(max.getCentroid());
		
		
		
		//update the image in each display window
		original.updateImage(ji.getBufferedImage());
		mask.updateImage(jmask.getBufferedImage());
		
		//check if there is any update for original image
		//and output the selected rectangle if there is an update
		if(original.isSelectionUpdated())
		{
			System.out.println(original.getSelectedRectangle());
			
			range = JImageProcessing.findRangeByAverage(ji, original.getSelectedRectangle());
			System.out.println("low: "+Arrays.toString(range[0])+" high: "+Arrays.toString(range[1]));
		}
		return max;
	}

	public void createSong() {
		byte[] command = new byte[15];
		command[0] = (byte)140;//Song command 
		command[1] = (byte)0; //Create song 1
		command[2] = (byte)6; //Number of notes
		
		command[3] = (byte)96; //C note
		command[4] = (byte)8; // 1/16 sec 
		
		command[5] = (byte)98; //D note
		command[6] = (byte)8; // 1/16 sec
		
		command[7] = (byte)100; //E note
		command[8] = (byte)8; // 1/16 sec
		
		command[9] = (byte)96; //C note
		command[10] = (byte)8; // 1/16 sec
		
		command[11] = (byte)98; //D note
		command[12] = (byte)8; // 1/16 sec
		
		command[13] = (byte)103; //G note
		command[14] = (byte)16; // 1/8 sec
		
		this.write(command);
	}

	public void playSong()
	{
		byte[] command = new byte[2];
		command[0] = (byte)141;//Play Song command 
		command[1] = (byte)0; //Play song 1
		this.write(command);
	}


	/* (non-Javadoc)
	 * @see Robot#safe()
	 */
	@Override
	public void safe() {
		
		super.safe();
		canDrive = true;
	}

	public static double findTurnAngle(int width, int height, int x, int y)
	{
		final int OFFSET = 0;
		int fromX = width/2;
		int fromY = height + OFFSET;
		
		int toX = x-fromX;
		int toY = fromY-y;
		double theta  = Math.toDegrees(Math.atan2(toY, toX));
		theta -= 90;
		theta *= (2.0/3);
		//theta *= -1;
		return theta;
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
