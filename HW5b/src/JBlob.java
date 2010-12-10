import java.awt.Rectangle;
import java.util.Vector;

/** 
 * Represents a blob of related pixels in the image.
 */
public class JBlob 
{
	// bounding box; (x,y) is upper-left corner
	public int mX, mY, mWidth, mHeight; 
	// entire image
	public int mMinX, mMinY, mMaxX, mMaxY;
	// Points in the blob
	public Vector<JPoint2D> mPoints;

	/* Initializes various values.
	 */
	public JBlob()
	{
		mX = 0;
		mY = 0;
		mWidth = 0;
		mHeight = 0;
		
		mMinX = 100000000;
		mMinY = mMinX;
		mMaxX = -mMinX;
		mMaxY = mMaxX;
		
		mPoints = new Vector<JPoint2D>();
	}
	
	/** Adds a point to the set.
	 * @param p: the point to be added
	 */
	public void addPoint(JPoint2D p)
	{
		addPoint(p.getX(), p.getY());
	}
	
	/** Adds a point to the set and updates the bounding box.
	 * @param x: x parameter of the point
	 * @param y: y parameter of the point
	 */
	public void addPoint(int x, int y)
	{
		mPoints.add(new JPoint2D(x,y));
		
		// Update bounding box
		if (x < mMinX) mMinX = x;
		if (y < mMinY) mMinY = y;
		if (x > mMaxX) mMaxX = x;
		if (y > mMaxY) mMaxY = y;
		mX = mMinX;
		mY = mMinY;
		mWidth = mMaxX - mMinX + 1;
		mHeight = mMaxY - mMinY + 1;
	}
	
	/** Find the number of points in a particular blob.
	 * @return: the size of the points vector
	 */
	public int getNumPoints()
	{
		return mPoints.size();
	}
	
	/** Accessor method 
	 * @return: x
	 */
	public int getX()
	{
		return mX;
	}
	
	/** Accessor method 
	 * @return: xy
	 */
	public int getY()
	{
		return mY;
	}
	
	/** Accessor method 
	 * @return: width
	 */
	public int getWidth()
	{
		return mWidth;
	}
	
	/** Accessor method 
	 * @return: height
	 */
	public int getHeight()
	{
		return mHeight;
	}
	
	/** Accessor method for an individual point
	 * @param idx: the index of the point
	 * @return: the point requested
	 */
	public JPoint2D getPoint(int idx)
	{
		return mPoints.get(idx);
	}
	
	/** Calculates and returns the centroid of the blob.
	 * @return: a point representing the centroid
	 */
	public JPoint2D getCentroid()
	{
		int xSum = 0;
		int ySum = 0;
		
		// Break if the blob has zero area
		if (mPoints.size() == 0)
		{
			return null;
		}
		
		// For all points in the blob, update the total x and y values
		for(JPoint2D p : mPoints)
		{
			xSum += p.getX();
			ySum += p.getY();
		}
		
		// Divide to find the acutal x and y coordinates of the centroid
		xSum /= mPoints.size();
		ySum /= mPoints.size();
		
		return new JPoint2D(xSum, ySum);
	}
	
	/** Finds the biggest blob in an image, given the set of blobs and other necessary values.
	 * @param jbs: The set of detected blobs in the image
	 * @param width: The width of the frame (used for error correction)
	 * @param height The height of the frame (used for error correction)
	 * @return: The largest blob
	 */
	public static JBlob findBiggestBlob(Vector<JBlob> jbs,  int width, int height) 
	{	
		JBlob biggest = new JBlob();
		
		// Break if there are no blobs
		if (jbs == null)
		{
			return null;
		}
		
		// Check every blob, and retain the biggest, so long as the blob is not encompassing the whole
		// image. This is the error correction for which height and width are used.
		for (JBlob b : jbs)
		{
			if (b.getBoundingBox().getHeight() * b.getBoundingBox().getWidth() < height * width &&
					b.getNumPoints() > biggest.getNumPoints())
			{
				biggest = b;
			}
		}
		
		// System.out.println(biggest.getBoundingBox());
		return biggest;
	}
	
	/** Produces the size of the bounding box of a blob.
	 * @return A rectangle object representing the bounding box.
	 */
	public Rectangle getBoundingBox()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	/** Checks to see if the blob contains a particular point.
	 * @param m: The point being checked
	 * @return: true or false, depending on whether the blob contains the point
	 */
	public boolean contains(JPoint2D m) 
	{
		return this.mPoints.contains(m);
	}

	/** Takes a point in the blog and projects it downward towards the floor.
	 * @param point: the point to be projected
	 * @return: the projected point
	 */
	public JPoint2D getPointBelow(JPoint2D point)
	{
		// Break if there is no point to be checked
		if(point == null)
		{
			return null;
		}
		
		int y = point.getY();
		int x = point.getX();
		int maxY = y;
		
		for(JPoint2D p : mPoints)
		{
			// Retain the largest y-value within the blob
			if(p.getX() == x && p.getY() > y)
			{
				maxY=p.getY();
			}
		}
		
		return new JPoint2D(x,maxY);
	}

}
