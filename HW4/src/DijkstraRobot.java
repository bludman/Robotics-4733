import java.util.ArrayList;
import java.awt.geom.Point2D;

/**
 * @author Benjamin Ludman, Mike Hernandez
 * Implementation for a Dijkstra pathfinding iRobot Create.
 */
public class DijkstraRobot extends Robot 
{
	private static final int DEFAULT_SPEED = 100;
	private static final int MM_IN_M = 1000;
	
	/* 
	 * Constructor for Dijkstra robot
	 */
	public DijkstraRobot()
	{
		
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
		pathAlgorithm1(vertices);
		//pathAlgorithm2(vertices);
		//pathAlgorithm3(vertices);
		
	}

	/**
	 * Take in two Point2D objects and output the angle between them.
	 * @param p1 the first point
	 * @param p2 the second point
	 * @returns the angle measure in degrees
	 */
	private double getAngle(Point2D p1, Point2D p2)
	{
		double degrees;
		
		// atan2(y2 - y1, x2 - x1)
		degrees = Math.atan2(p2.getY() - p1.getY(),
							p2.getX() - p1.getX());
		
		degrees = Math.toDegrees(degrees);
		
		return degrees;
	}
	
	
	/**
	 * This implementation resets the robot back to 
	 * its original orientation between waypoints.
	 * This is less efficient than calculating the new angle 
	 * based on the new orientation but in testing, 
	 * gave more accurate results
	 * @param vertices
	 */
	private void pathAlgorithm1(ArrayList<Point2D> vertices) {
		double distance;
		double degrees;
		
		for (int i = 0; i < vertices.size() - 1; i++)
		{
			// Get the angle between the two points
			degrees = getAngle(vertices.get(i), vertices.get(i + 1));
			
			// Get the distance between the two points
			distance = vertices.get(i).distance(vertices.get(i + 1));
			
			// Convert the distance value from meters to millimeters
			distance = Math.floor(distance * MM_IN_M);
			
			// Perform the proper rotation and translation
			this.turnDegrees(DEFAULT_SPEED, (int)degrees);
			this.driveDistance(DEFAULT_SPEED, (int)distance);
			
			// Return to original configuration (theta = 0)
			this.turnDegrees(DEFAULT_SPEED, (int)-degrees);
		}
	}
	
	/**
	 * This implementation takes into acount the last angle
	 * the robot was in, and should be equivalent to above,
	 * but had worse odometry error 
	 * @param vertices
	 */
	private void pathAlgorithm2(ArrayList<Point2D> vertices) {
		double distance;
		double degrees;
		double lastAngle=0;
		for (int i = 0; i < vertices.size() - 1; i++)
		{
			// Get the angle between the two points
			degrees = getAngle(vertices.get(i), vertices.get(i + 1));
			
			// Get the distance between the two points
			distance = vertices.get(i).distance(vertices.get(i + 1));
			
			// Convert the distance value from meters to millimeters
			distance = Math.floor(distance * MM_IN_M);
			
			// Perform the proper rotation and translation
			this.turnDegrees(DEFAULT_SPEED, (int)(degrees-lastAngle));
			this.driveDistance(DEFAULT_SPEED, (int)distance);
			
			lastAngle=degrees;
			// Return to original configuration (theta = 0)
			//this.turnDegrees(DEFAULT_SPEED, (int)-degrees);
		}
		this.turnDegrees(DEFAULT_SPEED, (int)-lastAngle);
	}
	
	/**
	 * Another path following angle that should be more robust but 
	 * needs further testing
	 * @param vertices
	 */
	private void pathAlgorithm3(ArrayList<Point2D> vertices) {
		double distance;
		double degrees;
		double currentAngle = 0;
		for (int i = 0; i < vertices.size() - 1; i++)
		{
			// Get the angle between the two points
			degrees = getAngle(vertices.get(i), vertices.get(i + 1));
			
			// Get the distance between the two points
			distance = vertices.get(i).distance(vertices.get(i + 1));
			
			// Convert the distance value from meters to millimeters
			distance = Math.floor(distance * MM_IN_M);
			
			// Perform the proper rotation
			// Case 1: new angle is "below" old angle, but less than 180 degrees away
			if (currentAngle > degrees && currentAngle - 180 < degrees)
			{
				// Rotate right
				this.turnDegrees(DEFAULT_SPEED, (int)degrees);
				
			}
			// Case 2: new angle is "above" old angle, but less than 180 degrees away
			else if (currentAngle < degrees && degrees - 180 < currentAngle)
			{
				// Rotate left
				this.turnDegrees(DEFAULT_SPEED, (int)-degrees);
			}
			// Case 3: new angle is "below" old angle by at least 180 degrees
			else if (currentAngle > degrees && currentAngle - 180 > degrees)
			{
				// Rotate right
				this.turnDegrees(DEFAULT_SPEED, (int)(360 - degrees));
			}
			// Case 4: new angle is "above" old angle by at least 180 degrees
			else if (currentAngle < degrees && degrees - 180 > currentAngle)
			{
				// Rotate left
				this.turnDegrees(DEFAULT_SPEED, (int)(degrees - 360));
			}
			
			// Perform the proper translation
			this.driveDistance(DEFAULT_SPEED, (int)distance);
				
			// Save the current angle for the next iteration
			currentAngle = degrees;
		}
	}


}
