import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Obstacle 
{
	private Point2D[] vertices;
	private Point2D[] grownVertices;
	private Point2D[] convexHullVertices;
	
	public Obstacle(double[][] vertices) 
	{
		buildObstacle(vertices);
				
		calculateGrownPoints();
		calculateConvexHull();
	}
	
	
	public Obstacle(double[][] vertices, boolean isAlreadyGrown)
	{
		
	}
	
	private void buildObstacle(double[][] vertices)
	{
		this.vertices = new Point2D[vertices.length];
		//System.out.println("len: "+this.vertices.length);
		
		for(int i=0;i<vertices.length;i++)
		{
			//System.out.println("Point: "+vertices[i][0]+" "+ vertices[i][1]);
			this.vertices[i]= new Point2D.Double(vertices[i][0], vertices[i][1]);
		}
	}
	
	private void calculateConvexHull() {
		// TODO REFACTOR FROM DRIVER TO HERE
		
		convexHullVertices=this.vertices;
	}

	private void calculateGrownPoints() {
		// TODO REFACTOR FROM DRIVER TO HERE
		grownVertices=this.vertices;
	}

	public void setGrownVertices(double[][] vertices) 
	{
		this.grownVertices = new Point2D[vertices.length];
		//System.out.println("len: "+this.vertices.length);
		
		for(int i=0;i<vertices.length;i++)
		{
			//System.out.println("Point: "+vertices[i][0]+" "+ vertices[i][1]);
			this.grownVertices[i]= new Point2D.Double(vertices[i][0], vertices[i][1]);
		}
		convexHullVertices=grownVertices;
		findConvexHull();
		
	}
	
	public Point2D[] getVertices()
	{
		return vertices.clone();
	}
	
	public void findConvexHull()
	{
		
		
		
		// Contains the current P_0
		Point2D rightmostLowest= grownVertices[0];
		int rlIndex=0;
		
		// Stack for finding the convex hull
		Stack<Point2D> convexStack = new Stack<Point2D>();
		ArrayList<Point2D> convexHull= new ArrayList<Point2D>();
		
		//USE GROWN POINTS
		//Start the hull with the grown points
		for (int i=0;i< grownVertices.length;i++)
		{
			convexHull.add(grownVertices[i]);
		}
		
//		//USE ORIGINAL POINTS
//		for (int i=0;i< vertices.length;i++)
//		{
//			convexHull.add(vertices[i]);
//		}
		
		//Find the rightmost lowest point
		for (int i=0;i< convexHull.size();i++)
		{
			// Retain the point with the lowest y value
			if (convexHull.get(i).getY() < rightmostLowest.getY())
			{
				rightmostLowest = convexHull.get(i);
			}
			// If the y values match, pick the point with the greatest x value
			else if (convexHull.get(i).getY() == rightmostLowest.getY())
			{
				if (convexHull.get(i).getX() > rightmostLowest.getX())
				{
					rightmostLowest = convexHull.get(i);
				}
			}
		}
		
		
		
		
			
		
		
		// Put the point at the beginning of the list
		Point2D temp = grownVertices[0];
		convexHull.set(0, rightmostLowest);
		convexHull.set(rlIndex, temp);
		
		final Point2D p_0=rightmostLowest; //used for comparator
		
		Comparator<Point2D> c = new Comparator<Point2D>() {
			private Point2D p0=p_0;
			
				
			@Override
			public int compare(Point2D o1, Point2D o2) 
			{
				double angle1= Math.atan2((o1.getY()-p0.getY()),(o1.getX()-p0.getX()));
				double angle2= Math.atan2((o2.getY()-p0.getY()),(o2.getX()-p0.getX()));
				
				//System.out.println("Angle 1:"+angle1+" angle2: "+angle2);
				if(angle1<angle2)
				{
					return -1;
				}
				else if (angle1>angle2)
				{
					return 1;
				}
				else
				{
					if(p0.distance(o1)<p0.distance(o2))
						return -1;
					else if(p0.distance(o1)>p0.distance(o2))
						return +1;
					else 
						return 0;
				}
				
			}
		};
		Collections.sort(convexHull, c);
		
		for(Point2D p: convexHull)
		{
			double angle1= Math.atan2((p.getY()-p_0.getY()),(p.getX()-p_0.getX()));
			System.out.println(p+" Angle: "+angle1);
		}
		
		
		//Arrays.sort(grownVertices,c);
		System.out.println("sorted points:"+Arrays.toString(convexHull.toArray()));
			
		/*
		// Rightmost, lowest point found. Sort the rest of the points
		for (int i = 1; i < vertex.length; i++)
		{
			for (int j = i; j < vertex.length; j++)
			{
				// Points sorted from right (beginning of array) to left (end of array)
				if (!OnLeft(rightmostLowest, vertex[i], vertex[j]))
				{
					temp = vertex[j];
					vertex[j] = vertex[i];
					vertex[i] = temp;
				}
			}
		}
		
		*/
		// Push starting values onto the stack
		convexStack.push(convexHull.get(convexHull.size()-1));
		convexStack.push(convexHull.get(0));
		
		
		// Points sorted, find convex hull
		for (int i = 1; i <convexHull.size() && !convexStack.isEmpty(); )
		{
			
			temp = convexStack.pop();
			
			if (!convexStack.isEmpty() && OnLeft(temp, convexStack.peek(), convexHull.get(i)))
			{
				// Push current point onto the stack if it is strictly left of the line
				// formed by the two points on top of the stack
				convexStack.push(temp);
				convexStack.push(convexHull.get(i));
				i++;
			}
			System.out.println("Stack: "+convexStack.toString());
		}
	
		Point2D hull[] = new Point2D[convexStack.size()];
		
		// Store information that is in the stack
//		for (int i = 0; i < convexStack.size(); i++)
//		{	
//			hull[i] = convexStack.pop();
//		}
		
		hull=convexStack.toArray(hull);
		
		System.out.println("hull points:"+Arrays.toString(hull));
		this.convexHullVertices = hull;	
	}
	
	public double[][] getHullPoints()
	{
		return pointsToDouble(convexHullVertices);
		/*
		double[][] hull = new double[convexHullVertices.length][2];
		for(int i=0;i<convexHullVertices.length;i++)
		{
			if(convexHullVertices[i]!=null){
			hull[i][0]=convexHullVertices[i].getX();
			hull[i][1]=convexHullVertices[i].getY();
			}
		}
		return hull;
		*/
	}

	public static double[][] pointsToDouble(Point2D[] points)
	{
		double[][] newPoints = new double[points.length][2];
		for(int i=0;i<points.length;i++)
		{
			if(points[i]!=null){
			newPoints[i][0]=points[i].getX();
			newPoints[i][1]=points[i].getY();
			}
		}
		return newPoints;
	}

	private static boolean OnLeft(Point2D Point0, Point2D Point1, Point2D Point2)
	{
		//return (Point1.getX() - Point0.getX()) * (Point2.getY() - Point0.getY())
		//> (Point2.getX() - Point0.getX()) * (Point1.getY() - Point0.getY());
		
//		return (Point1.getX() - Point0.getX()) * (Point2.getY() - Point1.getY()) 
//		> (Point1.getY() - Point0.getY())*(Point2.getX() - Point1.getX()) ;
		
		return ((Point1.getX() - Point0.getX()) * (Point2.getY() - Point0.getY()) - ((Point1.getY() - Point0.getY())*(Point2.getX() - Point0.getX())))<0 ;
		
	}
	
	
	/**
	 * Find the shortest path from the start point to goal point navigating the obstacles
	 * @param start
	 * @param goal
	 * @param obstacles
	 * @return
	 */
	public static ArrayList<Point2D> findShortestPath(Point2D start, Point2D goal, ArrayList<Obstacle> obstacles)
	{
		
		
		
		return null;
		
	}

	public Point2D[] getGrownPoints() 
	{
		return this.grownVertices;
	}
	
	
	
}
