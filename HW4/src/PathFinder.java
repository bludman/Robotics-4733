import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * @author Ben
 *
 */
public class PathFinder {
	private final static boolean PRUNE_INTERSECTIONS = true;
	
	public static class Edge
	{
		Point2D a,b;
		public Edge(Point2D a, Point2D b) {
			this.a = a;
			this.b = b;
		}
		
		public double[][] toArray()
		{
			double[][] edge = new double[][]{{a.getX(),a.getY()},{b.getX(),b.getY()}};
			//System.out.println("Vis: "+Arrays.toString(edge[0])+" -> "+Arrays.toString(edge[1]));
			return edge;
		}
		
		public String toString()
		{
			return "EDGE: "+a.getX()+","+a.getY()+"->"+b.getX()+","+b.getY();
		}
		
		public double getLength()
		{
			return a.distance(b);
		}
		
		public Point2D getA(){return a;}
		public Point2D getB(){return b;}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}
	}

	/**
	 * Non destructive function that finds the visibility graph of a connected graph
	 * @return
	 */
	public static ArrayList<Edge> findVisibilityGraph(Point2D start, Point2D goal, ArrayList<Obstacle> obstacles, Obstacle wallObstacle)
	{
		ArrayList<Point2D> nodes = new ArrayList<Point2D>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Path2D> polygons = new ArrayList<Path2D>();
		
		//Add all the nodes from the obstacles to the nodes list
		nodes.add(start);
		nodes.add(goal);
		System.out.println("***Goal node is: "+goal);
		System.out.println("***Start node is: "+start);
		if(obstacles!=null)
			for(Obstacle o : obstacles)
			{
				if(o!=null)
					nodes.addAll(Arrays.asList(o.getHullPoints()));
			}
		
		if(wallObstacle!=null)
			nodes.addAll(Arrays.asList(wallObstacle.getHullPoints()));
		
		Path2D wall = wallObstacle.toPath();
		
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
				if(!PRUNE_INTERSECTIONS || !Obstacle.intersects(polygons,wall,edge))
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
	public static ArrayList<Point2D> findShortestPath(Point2D start, Point2D goal, ArrayList<Point2D> vertices, ArrayList<Edge> edges)
	{
		HashSet<Point2D> visited = new HashSet<Point2D>();
		HashMap<Point2D,Double> distanceToStart = new HashMap<Point2D, Double>();
		
		class Node{
			private Point2D point;
			private double distance;
			public Point2D getPoint() {return this.point;}
		}
		PriorityQueue<Node> unvisited = new PriorityQueue<Node>();
		
		//HashMap<Point2D, Point2D> adjacency = new HashMap<Point2D, Point2D>();
		HashMap<Point2D, ArrayList<Point2D>> adjacency = buildGraph(edges);
		
		HashSet<Node> temp = new HashSet<Node>();
		//add all nodes from edges to hashset with default infinite distance
		//add adjacencies to the adjacency list
		//remove the source node from the hashset
		//move nodes from temp to the heap
		
		
		//While Vertices in G remain UNVISITED
		while(!unvisited.isEmpty())
		{
			//Find closest Vertex V that is UNVISITED
			Node v =unvisited.poll();
			//Mark V as VISITED
			visited.add(v.getPoint());
			
			//For each UNVISITED vertex W visible from V
			ArrayList<Point2D> visiblePointsFromV = adjacency.get(v.getPoint());
			for(Point2D w : visiblePointsFromV)
			{
				if(!visited.contains(v.getPoint()))
				{
					//If (DIST(S,V) + DIST(V,W)) < DIST(S,W)
					if(distanceToStart.get(v)+v.)
					
						//then DIST(S,W) = DIST(S,V) + DIST(V,W)
				}
			}
		}
		
		
		
		for(Edge edge: edges)
		{
			
		}
		
		return null;
		
	}

	private static HashMap<Point2D, ArrayList<Point2D>> buildGraph(ArrayList<Edge> edges) {
		HashMap<Point2D, ArrayList<Point2D>> adj= new HashMap<Point2D, ArrayList<Point2D>>();
		for(Edge e: edges)
		{
			Point2D a= e.getA();
			Point2D b= e.getB();
			if(!adj.containsKey(a))
				adj.put(a, new ArrayList<Point2D>());
			
			adj.get(a).add(b);
			
			if(!adj.containsKey(b))
				adj.put(b, new ArrayList<Point2D>());
			
			adj.get(b).add(a);
			
			
		}
		
		return adj;
		
	}

}
