import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;


public class Driver
{
	/* Contains the vertices of all obstacles and environment. */
	public static ArrayList<double[][]> Vertices;
	
	public static ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	public static Obstacle wall;
	
	/* Contains the grown vertices of all obstacles. */
	public static ArrayList<double[][]> GrownVertices;
	public static ArrayList<double[][]> grownVertices = new ArrayList<double[][]>();
	
	/* Contains the convex hull vertices of the grown obstacles. */
	public static ArrayList<double[][]> ConvexHull;
	public static ArrayList<double[][]> convexHulls = new ArrayList<double[][]>();
	
	/* Various coordinates and values necessary for proper execution of the program. */
	public static double StartX, StartY, GoalX, GoalY;
	public static int NumVertices, NumObstacles;
	
	/* Constant values used for computing the grown vertices and convex hull. */
	public static final double RADIUS = .0825;
	public static final double THETA = Math.PI / 4;
	
	/* GUI on which all data will be displayed. */
	public static GUI Display;
	
	public static void main(String args[])
	{	
		// Require correct number of arguments
		// if (args.length != 3)
		// {
		//	System.out.println("Usage: java Driver.java obstacle_textfile start_goal_textfile");
		//	return;
		// }
		
		Vertices = new ArrayList<double[][]>();
		GrownVertices = new ArrayList<double[][]>();
		ConvexHull = new ArrayList<double[][]>();
		
		// Retrieve the start and goal coordinates
		double returned[] = GetStartAndGoalCoordinates("hw3_start_goal.txt");
		StartX = returned[0];
		StartY = returned[1];
		
		GoalX = returned[2];
		GoalY = returned[3];
		
		System.out.println("RETURNED: "+Arrays.toString(returned));
		
		// Retrieve the vertices of all obstacles, store in ArrayList Vertices
		GetVertices("hw3_world_obstacles.txt");
		
		// Grow the obstacles, excluding the boundary of the environment
		GrowObstacles(Vertices);
		
		// Find the convex hull of each obstacle
		FindConvexHull(GrownVertices);
		
		// Calculate the visibility graph
		CalculateGraph();
		
		// Find the shortest path
		FindPath();
		
		
		
		for(Obstacle o:obstacles)
		{
			if(o!=null)
			{
				double[][] points=Obstacle.pointsToDouble(o.getGrownPoints());
				System.out.println(Arrays.toString(points));
				grownVertices.add(points);
				
			}
		}
		
		//obstacles.set(0, null);
		for(Obstacle o:obstacles)
		{
			if(o!=null)
			{
				double[][] points=Obstacle.pointsToDouble(o.getHullPoints());
				//System.out.println("Printed array: "+Arrays.toString(points));
				
				for(int i=0;i<points.length;i++)
				{
						System.out.print("("+points[i][0]+","+points[i][1]+") ");
				}
				
				convexHulls.add(points);
				
				System.out.println("\nadding an obstacle to convex hull\n***\n");
			}
		}

		ArrayList<PathFinder.Edge> visibilityGraph= PathFinder.findVisibilityGraph(new Point2D.Double(StartX, StartY), new Point2D.Double(GoalX, GoalY), obstacles,wall);
		ArrayList<Point2D> shortestPath = PathFinder.findShortestPath(new Point2D.Double(StartX, StartY), new Point2D.Double(GoalX, GoalY), visibilityGraph);
		
		//SAMPLE DATA
//		shortestPath = new ArrayList<Point2D>();
//		shortestPath.add(new Point2D.Double(StartX, StartY));
//		//shortestPath.add(new Point2D.Double(0,0));
//		//shortestPath.add(new Point2D.Double(1,1));
//		//shortestPath.add(new Point2D.Double(-1,2));
//		//shortestPath.add(new Point2D.Double(10,2));
//		shortestPath.add(new Point2D.Double(GoalX, GoalY));
		
		System.out.println("StartY: "+StartY);
		
		Display = new GUI(
				StartX,
				StartY,
				GoalX,
				GoalY,
				Vertices,
				grownVertices,
				//GrownVertices,
				convexHulls,
				//ConvexHull,
				visibilityGraph,
				shortestPath
				);
	}
	
	public static double[] GetStartAndGoalCoordinates(String FileName)
	{
		// For reading data in from the text file
		BufferedReader in;
		String ReadData = "";
		int SpaceLocation;
		
		// Values to be returned
		double StartX = 0, StartY = 0, GoalX = 0, GoalY = 0;
		
		try
		{
			in = new BufferedReader(new FileReader(new File(FileName)));
			ReadData = in.readLine();
			
			// Parse out x and y coordinates of the start position
			SpaceLocation = ReadData.indexOf(" ");
			StartX = Double.parseDouble(ReadData.substring(0, SpaceLocation));
			StartY = Double.parseDouble(ReadData.substring(SpaceLocation + 1));
			
			ReadData = in.readLine();
			
			// Parse out x and y coordinates of the goal position
			SpaceLocation = ReadData.indexOf(" ");
			GoalX = Double.parseDouble(ReadData.substring(0, SpaceLocation));
			GoalY = Double.parseDouble(ReadData.substring(SpaceLocation + 1));
		}
		catch (FileNotFoundException fe)
		{
			System.out.println("Error: file not found.");
			fe.printStackTrace();
		}
		catch (IOException io)
		{
			System.out.println("Error: I/O exception.");
			io.printStackTrace();
		}
		
		return new double[] {StartX, StartY, GoalX, GoalY};
	}
	
	public static void GetVertices(String FileName)
	{
		// For reading data in from the text file
		BufferedReader in;
		String ReadData = "";
		int SpaceLocation;
		
		// Currently manipulated values
		double[][] CurrentVertices;
		
		try
		{
			in = new BufferedReader(new FileReader(new File(FileName)));
			ReadData = in.readLine();
			
			// Read the number of obstacles
			NumObstacles = Integer.parseInt(ReadData);
			
			for (int i = 0; i < NumObstacles; i++)
			{
				ReadData = in.readLine();
			
				// For each obstacle, read the number of vertices
				NumVertices = Integer.parseInt(ReadData);
				CurrentVertices = new double[NumVertices][2];
				
				for (int j = 0; j < NumVertices; j++)
				{
					ReadData = in.readLine();
					
					// Parse out x and y coordinates of the vertex
					SpaceLocation = ReadData.indexOf(" ");
					
					CurrentVertices[j][0] = Double.parseDouble(ReadData.substring(0, SpaceLocation));
					CurrentVertices[j][1] = Double.parseDouble(ReadData.substring(SpaceLocation + 1));
				}
				
				Vertices.add(CurrentVertices);
				if(i==0)
					wall= new Obstacle(CurrentVertices);
				else
					obstacles.add(new Obstacle(CurrentVertices));
			}
		}
		catch (FileNotFoundException fe)
		{
			System.out.println("Error: file not found.");
			fe.printStackTrace();
		}
		catch (IOException io)
		{
			System.out.println("Error: I/O exception.");
			io.printStackTrace();
		}
	}
		
	public static void GrowObstacles(ArrayList<double[][]> Vertices)
	{
		// Newly created vertices
		double AllVertices[][];
		
		// Keeps track of position to add new vertices
		int VertexCounter;
		int obstacleCount=1;
		
		// For every obstacle
		for(double[][] Vertex : Vertices)
		{
			// Exclude the environment boundary
			if (!Vertices.get(0).equals(Vertex))
			{
				// Create a new array that will hold all of the newly created vertices
				AllVertices = new double[Vertex.length * 8 + Vertex.length][2];
				VertexCounter = 0;
				
				// For every vertex in that obstacle
				for (int i = 0; i < Vertex.length; i++)
				{	
					// Add the current vertex
					AllVertices[VertexCounter++] = Vertex[i];
					
					// Point 1: x, y - r
					AllVertices[VertexCounter][0] = Vertex[i][0];
					AllVertices[VertexCounter++][1] = Vertex[i][1] - RADIUS;
					
					// Point 2: x + r * sin(theta), y - r * cos(theta)
					AllVertices[VertexCounter][0] = Vertex[i][0] + RADIUS * Math.sin(THETA);
					AllVertices[VertexCounter++][1] = Vertex[i][1] - RADIUS * Math.cos(THETA);
					
					// Point 3: x + r, y
					AllVertices[VertexCounter][0] = Vertex[i][0] + RADIUS;
					AllVertices[VertexCounter++][1] = Vertex[i][1];
					
					// Point 4: x + r * sin(theta), y + r * cos(theta)
					AllVertices[VertexCounter][0] = Vertex[i][0] + RADIUS * Math.sin(THETA);
					AllVertices[VertexCounter++][1] = Vertex[i][1] + RADIUS * Math.cos(THETA);
					
					// Point 5: x, y + r
					AllVertices[VertexCounter][0] = Vertex[i][0];
					AllVertices[VertexCounter++][1] = Vertex[i][1] + RADIUS;
					
					// Point 6: x - r * sin(theta), y + r * scosin(theta)
					AllVertices[VertexCounter][0] = Vertex[i][0] - RADIUS * Math.sin(THETA);
					AllVertices[VertexCounter++][1] = Vertex[i][1] + RADIUS * Math.cos(THETA);
					
					// Point 7: x - r, y
					AllVertices[VertexCounter][0] = Vertex[i][0] - RADIUS;
					AllVertices[VertexCounter++][1] = Vertex[i][1];
					
					// Point 8: x - r * sin(theta), y - r * cos(theta)
					AllVertices[VertexCounter][0] = Vertex[i][0] - RADIUS * Math.sin(THETA);
					AllVertices[VertexCounter++][1] = Vertex[i][1] - RADIUS * Math.cos(THETA);
				}
				obstacles.get(obstacleCount-1).setGrownVertices(AllVertices);
				obstacleCount++;
				GrownVertices.add(AllVertices);
			}
		}
	}
	
	private static void FindConvexHull(ArrayList<double[][]> GrownVertices)
	{
		// Contains the current P_0
		double[] RightmostLowest = new double[2];
		int RLPosition = 0;
		
		// Stack for finding the convex hull
		Stack<double[]> ConvexStack = new Stack<double[]>();
		
		// For each set of grown vertices
		for (double[][] Vertex : GrownVertices)
		{
			// Initialize to negative/positive infinity to avoid accidentally selecting a point 0, 0
			RightmostLowest = new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};;
			RLPosition = 0;
			
			// Find the rightmost, lowest point
			for (int i = 0; i < Vertex.length; i++)
			{
				// Retain the point with the lowest y value
				if (Vertex[i][1] < RightmostLowest[1])
				{
					RightmostLowest = Vertex[i];
					RLPosition = i;
				}
				// If the y values match, pick the point with the greatest x value
				else if (Vertex[i][1] == RightmostLowest[1])
				{
					if (Vertex[i][0] > RightmostLowest[0])
					{
						RightmostLowest = Vertex[i];
						RLPosition = i;
					}
				}
			}
			
			// Put the point at the beginning of the array
			double[] Temp = Vertex[0];
			Vertex[0] = RightmostLowest;
			Vertex[RLPosition] = Temp;
			
			// Rightmost, lowest point found. Sort the rest of the points
			for (int i = 1; i < Vertex.length; i++)
			{
				for (int j = i; j < Vertex.length; j++)
				{
					// Points sorted from right (beginning of array) to left (end of array)
					if (!OnLeft(RightmostLowest, Vertex[i], Vertex[j]))
					{
						Temp = Vertex[j];
						Vertex[j] = Vertex[i];
						Vertex[i] = Temp;
					}
				}
			}
			
			// Push starting values onto the stack
			ConvexStack.push(Vertex[0]);
			ConvexStack.push(Vertex[Vertex.length - 1]);
			
			// Points sorted, find convex hull
			for (int i = 1; i < Vertex.length; i += 0)
			{
				Temp = ConvexStack.pop();
				if (OnLeft(Temp, ConvexStack.peek(), Vertex[i]))
				{
					// Push current point onto the stack if it is strictly left of the line
					// formed by the two points on top of the stack
					ConvexStack.push(Temp);
					ConvexStack.push(Vertex[i]);
					i++;
				}
			}
		
			double Hull[][] = new double[ConvexStack.size()][2];
			
			// Store information that is in the stack
			for (int i = 0; i < ConvexStack.size(); i++)
			{	
				Hull[i] = ConvexStack.pop();
			}
			
			// Add the information to the ConvexHull ArrayList
			ConvexHull.add(Hull);	
		}
	}
	
	private static boolean OnLeft(double[] Point0, double[] Point1, double[] Point2)
	{
		return (Point1[0] - Point0[0]) * (Point2[1] - Point0[1]) 
		> (Point2[0] - Point0[0]) * (Point1[1] - Point0[1]);
	}
	
	private static void CalculateGraph() 
	{	
	}
	
	private static void FindPath() 
	{		
	}
}
