public class BlobTracker 
{
	private int newArea, imageWidth, preferredArea;
	private JPoint2D centroid;
	private final int MARGIN = 25;
	private final double X_MARGIN = 0.25;
	/** Signal to recalibrate the tracked blob's area next frame */
	private boolean recalibrate;
	
	private DIRECTION fb = DIRECTION.NONE;
	private DIRECTION lr = DIRECTION.NONE;
	
	
	public enum DIRECTION{FORWARDS, BACKWARDS,LEFT,RIGHT,NONE};

	public BlobTracker(int myWidth)
	{
		centroid = new JPoint2D();
		newArea = 0;
		preferredArea = 0;
		imageWidth = myWidth;
		recalibrate = false;
	}
	
	private void checkArea() 
	{
		double percent= 0;
		
		if(preferredArea!=0)
			percent= (100.0*newArea/preferredArea)-100;
		
		if(newArea==0)
			percent=0;
		
		System.out.println("Percent: "+percent);
		
		
		
		// Object has moved closer to the camera
		if (percent>MARGIN)
		{
			System.out.println("Backwards");
			fb=DIRECTION.BACKWARDS;
			// Move backwards
		}
		// Object has moved further from the camera
		else if (percent<-MARGIN)
		{
			System.out.println("Forwards");
			// Move forwards
			fb=DIRECTION.FORWARDS;
		}
		else
		{
			fb=DIRECTION.NONE;
		}
	}

	private void checkCentroid() 
	{
		// Object has moved too far to the left
		if (centroid.getX() < imageWidth*X_MARGIN)
		{
			System.out.println("Turning left");
			// Turn left
			lr=DIRECTION.LEFT;
		}
		// Object has moved too far to the right
		else if (centroid.getX() > imageWidth - (imageWidth*X_MARGIN))
		{
			System.out.println("Turning right: "+imageWidth );
			// Turn right
			lr=DIRECTION.RIGHT;
		}
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
//		 System.out.println("Centroid: " + centroid.getX() + ", " + centroid.getY());
//		 System.out.println("Old area: " + oldArea);
//		System.out.println("New area: " + newArea);
//		System.out.println("Preferred Area: " + preferredArea);
		 
		 
		 checkArea();
		 checkCentroid();
	}

	public void recalibrate() {
		recalibrate = true;
	}
	
	public DIRECTION getLeftRightDirection(){
		return lr;
	}
	
	public DIRECTION getForwardsBackwardsDirection(){
		return fb;
	}
	
	


}
