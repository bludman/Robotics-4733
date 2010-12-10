public class BlobTracker 
{
	private int oldArea, newArea, imageWidth, preferredArea;
	private JPoint2D centroid;
	private final int MARGIN = 25;
	private final double X_MARGIN = 0.25;
	private boolean recalibrate;

	public BlobTracker(int myWidth)
	{
		centroid = new JPoint2D();
		oldArea = 0;
		newArea = 0;
		preferredArea = 0;
		imageWidth = myWidth;
		recalibrate = false;
	}
	
	public void checkArea() 
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
			// Move backwards
		}
		// Object has moved further from the camera
		else if (percent<-MARGIN)
		{
			System.out.println("Forwards");
			// Move forwards
		}
	}

	public void checkCentroid() 
	{
		// Object has moved too far to the left
		if (centroid.getX() < imageWidth*X_MARGIN)
		{
			System.out.println("Turning left");
			// Turn left
		}
		// Object has moved too far to the right
		if (centroid.getX() > imageWidth - (imageWidth*X_MARGIN))
		{
			System.out.println("Turning right: "+imageWidth );
			// Turn right
		}
	}

	public void update(JBlob blob, int imageWidth) 
	{
		this.imageWidth= imageWidth;
		centroid = blob.getCentroid();
		if(centroid==null)
			centroid=new JPoint2D(imageWidth/2,0);
		
		oldArea = newArea;
		newArea = blob.getNumPoints();
		if(recalibrate)
		{
			preferredArea = blob.getNumPoints();
			recalibrate = false;
		}
		 System.out.println("Centroid: " + centroid.getX() + ", " + centroid.getY());
		// System.out.println("Old area: " + oldArea);
//		System.out.println("New area: " + newArea);
//		System.out.println("Preferred Area: " + preferredArea);
	}

	public void recalibrate() {
		recalibrate = true;
		
	}
	


}
