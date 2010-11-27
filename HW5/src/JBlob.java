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


}
