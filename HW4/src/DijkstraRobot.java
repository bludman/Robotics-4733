import java.util.ArrayList;
import java.awt.geom.Point2D;

/**
 * @author Benjamin Ludman, Mike Hernandez
 * Implementation for a Dijkstra pathfinding iRobot Create.
 */
public class DijkstraRobot extends Robot 
{
	
	/* 
	 * Constructor for Dijkstra robot
	 */
	public DijkstraRobot()
	{
		
	}
	
	public static void main(String args[])
	{
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		
		// starting point
		points.add(new Point2D.Double(1, 0));
		
		// straight line
		points.add(new Point2D.Double(1, 1));
		
		// case 1
		points.add(new Point2D.Double(2, 1.5));
		
		// case 2
		points.add(new Point2D.Double(1, 1));
		
		// case 3
		points.add(new Point2D.Double(.5, 0));
		
		// case 4
		points.add(new Point2D.Double(1, 1));
	}
	
	/* 
	 * Initialize various aspects of the iRobot Create.
	 * @return nothing
	 */
	public void initializeRobot()
	{
		boolean setupStatus = this.setup();
		
		if (setupStatus)
		{
			this.start();
		}
		//this.safe();
	}
	
	/*
	 * Instructs robot to follow a path determined by a list of vertices.
	 * @param vertices an ArrayList of Point2D objects
	 * @return nothing.
	 */
	public void followPath(ArrayList<Point2D> vertices)
	{
		double distance, degrees, currentAngle = 0;
		
		for (int i = 0; i < vertices.size() - 1; i++)
		{
			// Get the angle between the two points
			degrees = getAngle(vertices.get(i), vertices.get(i + 1));
			
			// Get the distance between the two points
			distance = vertices.get(i).distance(vertices.get(i + 1));
			
			// Convert the distance value from meters to millimeters
			distance = Math.floor(distance * 1000);
			
			// Perform the proper rotation and translation
			this.turnDegrees(200, (int)degrees);
			this.driveDistance(200, (int)distance);
			
			// Return to original configuration (theta = 0)
			this.turnDegrees(200, (int)-degrees);
		}
		
		/*
		for (int i = 0; i < vertices.size() - 1; i++)
		{
			// Get the angle between the two points
			degrees = getAngle(vertices.get(i), vertices.get(i + 1));
			
			// Get the distance between the two points
			distance = vertices.get(i).distance(vertices.get(i + 1));
			
			// Convert the distance value from meters to millimeters
			distance = Math.floor(distance * 1000);
			
			// Perform the proper rotation
			// Case 1: new angle is "below" old angle, but less than 180 degrees away
			if (currentAngle > degrees && currentAngle - 180 < degrees)
			{
				// Rotate right
				this.turnDegrees(200, (int)degrees);
				
			}
			// Case 2: new angle is "above" old angle, but less than 180 degrees away
			else if (currentAngle < degrees && degrees - 180 < currentAngle)
			{
				// Rotate left
				this.turnDegrees(200, (int)-degrees);
			}
			// Case 3: new angle is "below" old angle by at least 180 degrees
			else if (currentAngle > degrees && currentAngle - 180 > degrees)
			{
				// Rotate right
				this.turnDegrees(200, (int)(360 - degrees));
			}
			// Case 4: new angle is "above" old angle by at least 180 degrees
			else if (currentAngle < degrees && degrees - 180 > currentAngle)
			{
				// Rotate left
				this.turnDegrees(200, (int)(degrees - 360));
			}
			
			// Perform the proper translation
			this.driveDistance(200, (int)distance);
				
			// Save the current angle for the next iteration
			currentAngle = degrees;
		}
		*/
	}
	
	/*
	 * Take in two Point2D objects and output the angle between them.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @returns the angle measure in degrees
	 */
	public double getAngle(Point2D p1, Point2D p2)
	{
		double degrees;
		
		// atan2(y2 - y1, x2 - x1)
		degrees = Math.atan2(p2.getY() - p1.getY(),
							p2.getX() - p1.getX());
		
		degrees = Math.toDegrees(degrees);
		
		return degrees;
	}
}
