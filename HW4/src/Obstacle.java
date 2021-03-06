import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
/**
 * Abstraction of an obstacle for use with path finding
 * @author Benjamin Ludman, Mike Hernandez
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
		convexHullVertices=this.vertices;
	}
	
	
	public Obstacle(double[][] vertices, boolean isAlreadyGrown)
	{
		
	}
	
	private void buildObstacle(double[][] vertices)
	{
		this.vertices = new Point2D[vertices.length];
		
		for(int i=0;i<vertices.length;i++)
		{
			this.vertices[i]= new Point2D.Double(vertices[i][0], vertices[i][1]);
		}
	}
	
	private void calculateGrownPoints() {
		// TODO REFACTOR FROM DRIVER TO HERE
		grownVertices=this.vertices;
	}

	/**
	 * Set the grown vertices that have been externally calculated
	 * TODO: refactor from driver into here
	 * @param vertices
	 */
	public void setGrownVertices(double[][] vertices) 
	{
		this.grownVertices = new Point2D[vertices.length];
		
		for(int i=0;i<vertices.length;i++)
		{
			this.grownVertices[i]= new Point2D.Double(vertices[i][0], vertices[i][1]);
		}
		convexHullVertices=grownVertices;
		findConvexHull();
	}
	
	/**
	 * Find the convex hull of the obstacle
	 */
	private void findConvexHull()
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
		}
		
		// Push starting values onto the stack
		convexStack.push(convexHull.get(convexHull.size()-1));
		convexStack.push(convexHull.get(0));
		
		// Points sorted, find convex hull
		for (int i = 1; i <convexHull.size() && !convexStack.isEmpty(); )
		{
			temp = convexStack.pop();
			
			if (!convexStack.isEmpty() && strictlyOnLeft(temp, convexStack.peek(), convexHull.get(i)))
			{
				// Push current point onto the stack if it is strictly left of the line
				// formed by the two points on top of the stack
				convexStack.push(temp);
				convexStack.push(convexHull.get(i));
				i++;
			}
		}
	
		// Store information that is in the stack
		Point2D hull[] = new Point2D[convexStack.size()];
		hull=convexStack.toArray(hull);
		
		this.convexHullVertices = hull;	
	}

	/**
	 * Check if a point is strictly left of two other points, used for finding convec hull
	 * @param Point0
	 * @param Point1
	 * @param Point2
	 * @return
	 */
	private static boolean strictlyOnLeft(Point2D Point0, Point2D Point1, Point2D Point2)
	{
		return ((Point1.getX() - Point0.getX()) * (Point2.getY() - Point0.getY()) - ((Point1.getY() - Point0.getY())*(Point2.getX() - Point0.getX())))<0 ;
	}

	/**
	 * Return a Path2D representation of an obstacle
	 * @return
	 */
	public Path2D toPath() {
		Path2D.Double p =  new Path2D.Double();
		
		p.moveTo(this.convexHullVertices[0].getX(), this.convexHullVertices[0].getY());
		for(Point2D point: this.convexHullVertices)
			p.lineTo(point.getX(), point.getY());
		
		p.closePath();
		return p;
	}

	/**
	 * Get the list of Edges that travers the exterior of the convex hull of the obstacle
	 * @return
	 */
	public ArrayList<PathFinder.Edge> getPerimeterAsEdges() 
	{
		ArrayList<PathFinder.Edge> edges = new ArrayList<PathFinder.Edge>();
		for(int i=0;i<(convexHullVertices.length-1);i++)
		{
			edges.add(new PathFinder.Edge(convexHullVertices[i], convexHullVertices[i+1]));
		}
		
		edges.add(new PathFinder.Edge(convexHullVertices[convexHullVertices.length-1],convexHullVertices[0]));
		
		return edges;
	}
	
	/**
	 * Check whether an edge intersects a list of obstacles, or goes outside a containing wall
	 * @param obstacles
	 * @param containingWalls
	 * @param edge
	 * @return
	 */
	public static boolean intersects(ArrayList<Path2D> obstacles, Path2D containingWalls, PathFinder.Edge edge) {
		final int SAMPLE_POINTS = 75;
		final double INCREMENT = 1.0/SAMPLE_POINTS;
		final int X=0;
		final int Y=1;
		double[][] basePoint = edge.toArray();
		
		//Sample the edge from P_0 to P_1 at INCREMENT intervals
		//do not include either enpoint
		for(double u=INCREMENT;u<1;u+=INCREMENT)
		{
			//find the sample point
			double x= (1.0-u)*basePoint[0][X]+u*basePoint[1][X];
			double y= (1.0-u)*basePoint[0][Y]+u*basePoint[1][Y];
			Point2D samplePoint = new Point2D.Double(x,y);
			
			//Check if sample point intersects any obstacle
			for(Path2D obstacle: obstacles)
			{
				if(obstacle.contains(samplePoint))
				{
					return true;
				}
			}
			
			//Check if sample point goes outside the main bounds
			if(!containingWalls.contains(samplePoint))
			{
				return true;
			}
		}
		
		//Edge sample points didn't intersect with any obstacles
		return false;
	}
	
	public Point2D[] getHullPoints()
	{
		return convexHullVertices;
	}
	
	public Point2D[] getGrownPoints() 
	{
		return this.grownVertices;
	}
	
	public Point2D[] getVertices()
	{
		return vertices.clone();
	}
	
	/**
	 * Convert an array of Pointd2D
	 * @param points
	 * @return
	 */
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
}
