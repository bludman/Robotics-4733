/**
 * 
 */

/**
 * @author Ben
 *
 */
public class DoorFindingRobot extends Robot {

	private final int speed = 100;
	private final int angle = 15;
	private final int distance = 100;
	
	public enum STATE{APPROACHING,KNOCKING};
	
	private STATE state= STATE.APPROACHING;
	private BlobTracker tracker;
	
	public DoorFindingRobot() 
	{
		tracker = new BlobTracker(myWidth);
	}
	
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
	
	
	
	public void doMove()
	{
		switch(state)
		{
			
			case KNOCKING:
			{
				this.driveDistance(-speed, -distance);
				this.driveDistance(speed, distance);
				this.driveDistance(-speed, -distance);
				this.driveDistance(speed, distance);
				this.playSong();
				break;
			}
			default:
			case APPROACHING:
			{
				JBlob door = lookForDoor();
				JPoint2D targetPoint = door.getPointBelow(door.getCentroid());
				driveTowards(targetPoint);
			}
		}
		
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
	
	public static void main(String[] args) throws Exception {

		DoorFindingRobot bot = new DoorFindingRobot();
		boolean setupStatus = bot.setup();
		if (setupStatus)
		{
			bot.start();
		}
	
		
		Thread.sleep(3000);
		System.out.println("Switching to safe mode");
		bot.safe();
		System.out.println("Creating song");
		bot.createSong();
		Thread.sleep(3000);
		System.out.println("Playing song");
		bot.playSong();

	
	}
	
}
