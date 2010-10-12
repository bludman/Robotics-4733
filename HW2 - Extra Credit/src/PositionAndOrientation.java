/**
 * 
 */

/**
 * @author Ben
 *
 */
public class PositionAndOrientation {
	
	private double x;
	private double y;
	private double angle;
	
	public PositionAndOrientation(double x, double y, double angle) 
	{
		this.x=x;
		this.y=y;
		this.angle=angle;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PositionAndOrientation [angle=" + angle + ", x=" + x + ", y="
				+ y + "]";
	}

	
}
