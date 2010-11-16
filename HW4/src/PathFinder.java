import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class PathFinder {
	
	
	public static class Edge
	{
		Point2D a,b;
		public Edge(Point2D a, Point2D b) {
			this.a = a;
			this.b = b;
		}
		
		public double[][] getAsArray()
		{
			double[][] edge = new double[][]{{a.getX(),a.getY()},{b.getX(),b.getY()}};
			//System.out.println("Vis: "+Arrays.toString(edge[0])+" -> "+Arrays.toString(edge[1]));
			return new double[][]{{a.getX(),a.getY()},{b.getX(),b.getY()}};
		}
	}
	/**
	 * Non destructive function that finds the visibility graph of a graph
	 * @return
	 */
	public static ArrayList<Edge> findVisibilityGraph(Point2D start, Point2D goal, ArrayList<Obstacle> obstacles)
	{
		ArrayList<Point2D> nodes = new ArrayList<Point2D>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		ArrayList<Path2D> polygons = new ArrayList<Path2D>();
		ArrayList<Path2D> walls = new ArrayList<Path2D>();
		
		
		
		//Add all the nodes from the obstacles to the nodes list
		nodes.add(start);
		nodes.add(goal);
		if(obstacles!=null)
		for(Obstacle o : obstacles)
		{
			if(o!=null)
				nodes.addAll(Arrays.asList(o.getHullPoints()));
		}
		
		walls.add(obstacles.remove(0).toPath());
		
		//Create the polygon list
		if(obstacles!=null)
		for(Obstacle o : obstacles)
		{
			if(o!=null)
				polygons.add(o.toPath());
		}
		
		
		
		
		
		//Create all edges that don't intersect
		for(int i=0;i<nodes.size();i++)
		{
			for(int j=i;j<nodes.size();j++)
			{
				Edge edge = new Edge(nodes.get(i), nodes.get(j));
				if(!Obstacle.intersects(polygons,walls,edge))
					edges.add(edge);
			}
		}

		return edges;
		
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

}
