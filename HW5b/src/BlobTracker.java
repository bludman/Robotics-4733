/**
 * Class designed to track and process information regarding blobs in the environment.
 */

public class BlobTracker 
{
	// Current area, width of the frame, original calibrated area
	private int newArea, imageWidth, preferredArea;
	// Centroid of the blob in question
	private JPoint2D centroid;
	// Allowable percentage change in the size (motion forward/backward)
	private final int MARGIN = 25;
	// Allowable change to either side of the image (motion left/right)
	private final double X_MARGIN = 0.25;
	
	// Signal to recalibrate the tracked blob's area next frame
	private boolean recalibrate;
	
	// Forward and backward, left and right
	private DIRECTION fb = DIRECTION.NONE;
	private DIRECTION lr = DIRECTION.NONE;
	
	public enum DIRECTION{FORWARDS, BACKWARDS, LEFT, RIGHT, NONE};

	/* Initializes necessary values for the tracker.
	 * @param myWidth: the width of the frame
	 */
	public BlobTracker(int myWidth)
	{
		centroid = new JPoint2D();
		newArea = 0;
		preferredArea = 0;
		imageWidth = myWidth;
		recalibrate = false;
	}
	
	/* Determines whether the area of the blob has changed by a significant degree
	 * and prepares the movement of the robot appropriately.
	 */
	private void checkArea() 
	{
		double percent = 0;
		
		// Check to make sure that an ArithmeticException will not be caused
		if (preferredArea != 0)
		{
			// Ratio of new area to calibrated area, subtract 100 to center the value around 0
			percent = (100.0 * newArea / preferredArea) - 100;
		}
		
		if (newArea == 0)
		{
			percent = 0;
		}
		//System.out.println("Percent: " + percent);
		
		// Object has moved closer to the camera
		if (percent > MARGIN)
		{
			// System.out.println("Backwards");
			fb = DIRECTION.BACKWARDS;
		}
		// Object has moved further from the camera
		else if (percent < -MARGIN)
		{
			// System.out.println("Forwards");
			fb=DIRECTION.FORWARDS;
		}
		// Object is in a good position, do not move
		else
		{
			fb=DIRECTION.NONE;
		}
	}

	/* Determines whether or not the centroid of the object has moved too far to the left
	 * or the right.
	 */
	private void checkCentroid() 
	{
		// Object has moved too far to the left
		if (centroid.getX() < imageWidth * X_MARGIN)
		{
			// System.out.println("Turning left");
			lr=DIRECTION.LEFT;
		}
		// Object has moved too far to the right
		else if (centroid.getX() > imageWidth - (imageWidth * X_MARGIN))
		{
			// System.out.println("Turning right: "+imageWidth );
			lr=DIRECTION.RIGHT;
		}
		// Object is in a good position, do not move
		else
		{
			lr=DIRECTION.NONE;
		}
	}

	public void update(JBlob blob, int imageWidth) 
	{
		this.imageWidth= imageWidth;
		centroid = blob.getCentroid();
		if(centroid==null)
			centroid=new JPoint2D(imageWidth/2,0);
		
		newArea = blob.getNumPoints();
		if(recalibrate)
		{
			preferredArea = blob.getNumPoints();
			recalibrate = false;
		}
		 
		 checkArea();
		 checkCentroid();
	}

	/* Essentially a mutator method to prepare the program for recalibration.
	 */
	public void recalibrate() 
	{
		recalibrate = true;
	}
	
	/* Accessor method for the necessary turning direction.
	 * @return the direction
	 */
	public DIRECTION getLeftRightDirection()
	{
		return lr;
	}
	
	/* Accessor method for the necessary travel direction.
	 * @return the direction
	 */
	public DIRECTION getForwardsBackwardsDirection()
	{
		return fb;
	}
	
	


}
