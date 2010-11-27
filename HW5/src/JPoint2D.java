
public class JPoint2D {
	public int mX, mY;

	public JPoint2D()
	{
	}
	
	public JPoint2D(int x, int y)
	{
		mX = x;
		mY = y;
	}
	
	public int getX()
	{
		return mX;
	}
	
	public int getY()
	{
		return mY;
	}
	
	public void setX(int x)
	{
		mX = x;
	}
	
	public void setY(int y)
	{
		mY = y;
	}
	
	public JPoint2D north()
	{
		return new JPoint2D(mX,mY-1);
	}
	
	public JPoint2D south()
	{
		return new JPoint2D(mX,mY+1);
	}
	
	public JPoint2D east()
	{
		return new JPoint2D(mX-1,mY);
	}
	
	public JPoint2D west()
	{
		return new JPoint2D(mX+1,mY);
	}
}
