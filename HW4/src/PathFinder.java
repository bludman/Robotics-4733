import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Contains static methods for finding a visibility graph and a shortest path traversal of a visibility graph
 * @author Benjamin Ludman, Mike Hernandez
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
		if(obstacles!=null)
			for(Obstacle o : obstacles)
			{
				if(o!=null)
					nodes.addAll(Arrays.asList(o.getHullPoints()));
			}
		
		if(wallObstacle!=null)
			nodes.addAll(Arrays.asList(wallObstacle.getHullPoints()));
		
		Path2D wall = wallObstacle.toPath();
		
		//Create the list of paths that we check intersection against
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
		
		//Add back any edges along the perimeter of obstacles
		if(obstacles!=null)
			for(Obstacle o: obstacles)
			{
				edges.addAll(o.getPerimeterAsEdges());
			}
		
		//Add back any edges along the perimeter of the wall
		if(wallObstacle!=null)
			edges.addAll(wallObstacle.getPerimeterAsEdges());
		
		return edges;
	}
	
	/**
	 * Find the shortest path from the start point to goal point navigating the obstacles
	 * @param start
	 * @param goal
	 * @param edges define the path the robot is allowed tro traverse
	 * @return
	 */
	public static ArrayList<Point2D> findShortestPath(Point2D start, Point2D goal, ArrayList<Edge> edges)
	{
		HashSet<Point2D> visited = new HashSet<Point2D>();
		final HashMap<Point2D,Double> distanceToStart = new HashMap<Point2D, Double>();
		HashMap<Point2D,Point2D> updatedBy = new HashMap<Point2D, Point2D>();
		HashMap<Point2D, ArrayList<Point2D>> adjacency = buildAdjacencyList(edges);
		ArrayList<Point2D> shortestPath = new ArrayList<Point2D>();
		
		//Set all initial distances from start to infinity
		for(Point2D p: adjacency.keySet())
		{
			distanceToStart.put(p, Double.POSITIVE_INFINITY);
		}
		
		//Initialize the distance to the vertices visible from the start point
		if( adjacency.get(start)!=null) //make sure the start point is connected to something
			for(Point2D p: adjacency.get(start))
			{
				distanceToStart.put(p, p.distance(start));
				updatedBy.put(p, start);
			}
		
		//Set the distance of the start vertext to itself
		distanceToStart.put(start,0.0);
		
		Comparator<Point2D> distanceToStartComparator = new Comparator<Point2D>() {
			@Override
			public int compare(Point2D a, Point2D b) {
				//return distances.get(a).compareTo(distanceToStart.get(b));
				return distanceToStart.get(a).compareTo(distanceToStart.get(b));
			}
		};
		
		PriorityQueue<Point2D> unvisited = new PriorityQueue<Point2D>(adjacency.keySet().size(), distanceToStartComparator);
		unvisited.addAll(distanceToStart.keySet());
	
		//While Vertices in G remain UNVISITED
		while(!unvisited.isEmpty())
		{
			//Find closest Vertex V that is UNVISITED
			Point2D v =unvisited.poll();
			
			//Mark V as VISITED
			visited.add(v);
			
			//For each UNVISITED vertex W visible from V
			ArrayList<Point2D> visiblePointsFromV = adjacency.get(v);
			for(Point2D w : visiblePointsFromV)
			{
				if(!visited.contains(w))
				{
					//If (DIST(S,V) + DIST(V,W)) < DIST(S,W)
					if((distanceToStart.get(v)+v.distance(w) )< distanceToStart.get(w))
					{
						//then DIST(S,W) = DIST(S,V) + DIST(V,W)
						distanceToStart.put(w, distanceToStart.get(v)+v.distance(w));
						updatedBy.put(w,v);
						
						//Percolate the heap
						unvisited.remove(w);
						unvisited.add(w);
					}
				}
			}
		}
		
		//Build the shortest path to the goal point
		Point2D temp = goal;
		while(!temp.equals(start))
		{
			shortestPath.add(temp);
			temp=updatedBy.get(temp);
		}
		shortestPath.add(start);
		Collections.reverse(shortestPath);
			
		return shortestPath;
	}

	/**
	 * Given a list of edges, create an undirected graph in the for an adjacency list
	 * @param edges
	 * @return
	 */
	private static HashMap<Point2D, ArrayList<Point2D>> buildAdjacencyList(ArrayList<Edge> edges) {
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
