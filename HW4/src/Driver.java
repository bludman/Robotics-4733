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
		 //Require correct number of arguments
		 if (args.length != 2)
		 {
			System.out.println("Usage: java Driver.java obstacle_textfile start_goal_textfile");
			return;
		 }
		
		Vertices = new ArrayList<double[][]>();
		GrownVertices = new ArrayList<double[][]>();
		
		// Retrieve the start and goal coordinates
		double returned[] = GetStartAndGoalCoordinates(args[1]);
		StartX = returned[0];
		StartY = returned[1];
		
		GoalX = returned[2];
		GoalY = returned[3];
		
		// Retrieve the vertices of all obstacles, store in ArrayList Vertices
		GetVertices(args[0]);
		
		// Grow the obstacles, excluding the boundary of the environment
		//TODO: Refactor this to the obstacle class
		GrowObstacles(Vertices);
		
		for(Obstacle o:obstacles)
		{
			if(o!=null)
			{
				double[][] points=Obstacle.pointsToDouble(o.getGrownPoints());
				grownVertices.add(points);
			}
		}
		
		for(Obstacle o:obstacles)
		{
			if(o!=null)
			{
				double[][] points=Obstacle.pointsToDouble(o.getHullPoints());
				convexHulls.add(points);
			}
		}

		// Calculate the visibility graph
		ArrayList<PathFinder.Edge> visibilityGraph= PathFinder.findVisibilityGraph(new Point2D.Double(StartX, StartY), new Point2D.Double(GoalX, GoalY), obstacles,wall);
		
		// Calculate the shortest path
		ArrayList<Point2D> shortestPath = PathFinder.findShortestPath(new Point2D.Double(StartX, StartY), new Point2D.Double(GoalX, GoalY), visibilityGraph);

		Display = new GUI(
				StartX,
				StartY,
				GoalX,
				GoalY,
				Vertices,
				grownVertices,
				convexHulls,
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
	
}
