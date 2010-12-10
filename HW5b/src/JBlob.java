import java.awt.Rectangle;
import java.util.Vector;


public class JBlob {
	public int mX, mY, mWidth, mHeight;  // bounding box; (x,y) is upper-left corner
	public int mMinX, mMinY, mMaxX, mMaxY;
	public Vector<JPoint2D> mPoints;

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
	
	public void addPoint(JPoint2D p)
	{
		addPoint(p.getX(), p.getY());
	}
	
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
	
	public int getNumPoints()
	{
		return mPoints.size();
	}
	
	public int getX()
	{
		return mX;
	}
	
	public int getY()
	{
		return mY;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public JPoint2D getPoint(int idx)
	{
		return mPoints.get(idx);
	}
	
	public JPoint2D getCentroid()
	{
		int xSum=0;
		int ySum=0;
		
		if (mPoints.size() == 0)
			return null;
		
		for(JPoint2D p: mPoints)
		{
			xSum+= p.getX();
			ySum+= p.getY();
		}
		
		
			
		xSum /= mPoints.size();
		ySum /= mPoints.size();
		
		return new JPoint2D(xSum,ySum);
	}
	
		
	public static JBlob findBiggestBlob(Vector<JBlob> jbs,  int width, int height) 
	{	
		JBlob biggest = new JBlob();
		if (jbs == null)
		{
			return null;
		}
		
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
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(mX, mY, mWidth, mHeight);
	}
	
	public boolean contains(JPoint2D m) {
		return this.mPoints.contains(m);
	}

	
	public JPoint2D getPointBelow(JPoint2D point)
	{
		if(point==null)
			return null;
		
		int y = point.getY();
		int x = point.getX();
		int maxY = y;
		for(JPoint2D p: mPoints)
		{
			if(p.getX()==x && p.getY()>y)
				maxY=p.getY();
		}
		
		return new JPoint2D(x,maxY);
	}

}
