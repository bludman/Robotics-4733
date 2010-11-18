import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;


public class GUI extends JPanel implements ActionListener
{	
	/* Boolean values that determine what to display. */
	boolean showStartPoint, showGoalPoint, showObstacles, showGrownObstacles, showConvex, showVisibilityGraph, showOptimalPath;
	
	/* Double values that determine where to display the above. */
	double StartX, StartY, GoalX, GoalY;
	
	/* ArrayLists of vertices of objects to be displayed. */
	ArrayList<double[][]> Vertices = new ArrayList<double[][]>();
	ArrayList<double[][]> ConvexHull = new ArrayList<double[][]>();
	ArrayList<double[][]> grownObstacles = new ArrayList<double[][]>();
	ArrayList<double[][]> visibilityGraph = new ArrayList<double[][]>();
	double[][] shortestPath;
	
	
	/* Swing components used in the GUI. */
	public JFrame Frame;
	public JRadioButton ObstaclesButton, grownObstaclesButton, ConvexButton, VisibilityButton, PathButton;
	public ButtonGroup Buttons;
	
	/* Factor by which to scale the data. */
	public final int SCALE_FACTOR = 60;
	
	public GUI(
			double StartX, 
			double StartY, 
			double GoalX, 
			double GoalY, 
			ArrayList<double[][]> Vertices,
			ArrayList<double[][]> grownObstacles,
			ArrayList<double[][]> ConvexHull,
			ArrayList<PathFinder.Edge> visibilityGraph,
			ArrayList<Point2D> shortestPath
			)
	{
		// Initialize values for the GUI
		showStartPoint = false;
		showGoalPoint = false;
		showObstacles = false;
		showConvex = false;
		showVisibilityGraph = false;
		showOptimalPath = false;
		
		// this.Vertices = Vertices;
		// this.ConvexHull = ConvexHull;
		
		// Initialize Swing components
		Frame = new JFrame();
		
		ObstaclesButton = new JRadioButton("Start/Goal, showObstacles");
		ObstaclesButton.setActionCommand("showObstacles");
		ObstaclesButton.addActionListener(this);
		grownObstaclesButton = new JRadioButton("Grown Obstacles");
		grownObstaclesButton.setActionCommand("showGrownObstacles");
		grownObstaclesButton.addActionListener(this);
		ConvexButton = new JRadioButton("showConvex Hulls");
		ConvexButton.setActionCommand("showConvex");
		ConvexButton.addActionListener(this);
		VisibilityButton = new JRadioButton("Visibility Graph");
		VisibilityButton.setActionCommand("Visibility");
		VisibilityButton.addActionListener(this);
		PathButton = new JRadioButton("Shortest Path");
		PathButton.setActionCommand("Path");
		PathButton.addActionListener(this);
		
		// Set up button group
		Buttons = new ButtonGroup();
		Buttons.add(ObstaclesButton);
		Buttons.add(grownObstaclesButton);
		Buttons.add(ConvexButton);
		Buttons.add(VisibilityButton);
		Buttons.add(PathButton);
		
		// Add components to the panel
		this.setPreferredSize(new Dimension(800, 600));
		this.add(ObstaclesButton);
		this.add(grownObstaclesButton);
		this.add(ConvexButton);
		this.add(VisibilityButton);
		this.add(PathButton);
		
		// Set up the frame
		Frame.add(this);
		this.setOpaque(true);
		Frame.setContentPane(this);
		Frame.setSize(new Dimension(800, 600));
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.pack();
		Frame.setVisible(true);
			
		// Scale and save the data as necessary
		this.AdjustPoint(StartX, StartY, "Start");
		this.AdjustPoint(GoalX, GoalY, "Goal");
		
		for (int i = 0; i < Vertices.size(); i++)
		{
			this.AdjustObstacles(Vertices.get(i), "Original");
		}
		
		for (int i = 0; i < ConvexHull.size(); i++)
		{
			this.AdjustObstacles(ConvexHull.get(i), "showConvex");
		}
		
		for (int i = 0; i < grownObstacles.size(); i++)
		{
			this.AdjustObstacles(grownObstacles.get(i), "Grown");
		}
		
		convertVisibilityGraph(visibilityGraph);
		System.out.println("Shortest path:" +shortestPath.toString());
		convertShortestPath(shortestPath);
	}
	
	private void convertShortestPath(ArrayList<Point2D> shortestPath) 
	{
		double[][] path = new double[shortestPath.size()][2];
		
		int i=0;
		for(Point2D p: shortestPath)
		{
			path[i++]=adjustPoint(p);
			
		}
		this.shortestPath=path;
		
		System.out.println("***Shortest path has "+this.shortestPath.length +" points ***");
		for( i=0;i<path.length;i++)
		{
			System.out.println(path[i][0]+","+path[i][1]);
		}
		
	}

	private void convertVisibilityGraph(ArrayList<PathFinder.Edge> graph) 
	{
		for(PathFinder.Edge e: graph)
		{
			this.visibilityGraph.add(AdjustObstacles(e.toArray()));
		}
		
		System.out.println("***Visibility graph has "+visibilityGraph.size() +" points ***");
	}

	public void actionPerformed(ActionEvent e)
	{
		// Draw start point, goal point, and obstacles
		if (e.getActionCommand().equals("showObstacles"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			showStartPoint = true;
			showGoalPoint = true;
			
			// Draw the obstacles
			showObstacles = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, original obstacles and grown obstacles
		if (e.getActionCommand().equals("showGrownObstacles"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			showStartPoint = true;
			showGoalPoint = true;
			
			// Draw the obstacles
			showObstacles = true;
			
			// Draw the grown obstacles
			showGrownObstacles = true;
			
			
			this.repaint();
		}
		
		// Draw start point, goal point, and grown obstacles
		if (e.getActionCommand().equals("showConvex"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			showStartPoint = true;
			showGoalPoint = true;
			
			// Draw the obstacles and convex hulls, excluding the boundary of the environment
			showObstacles = true;
			showConvex = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, grown obstacles, and visibility graph
		if (e.getActionCommand().equals("Visibility"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			showStartPoint = true;
			showGoalPoint = true;
			showObstacles = true;
			
			
			// Draw the convex hulls, excluding the boundary of the environment 
			//showConvex = true;
			
			// Draw the visibility graph
			showVisibilityGraph = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, grown obstacles, and shortest path
		if (e.getActionCommand().equals("Path"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			showStartPoint = true;
			showGoalPoint = true;
			
			showObstacles = true;
			
			// Draw the grown obstacles, excluding the boundary of the environment
			showConvex = true;
			
			// Draw the optimal path
			// showVisibilityGraph = true;
			showOptimalPath = true;
			
			this.repaint();
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		

		// Draw start point
		if (showStartPoint)
		{
			// Draw a black x to represent the starting point
			drawMarker(g2, StartX, StartY, Color.BLACK);
//			g2.setColor(Color.BLACK);
//			g2.draw(new Line2D.Double(StartX - 2, StartY - 2, StartX + 2, StartY + 2));
//			g2.draw(new Line2D.Double(StartX + 2, StartY - 2, StartX - 2, StartY + 2));
		}
		
		// Draw goal point
		if (showGoalPoint)
		{
			// Draw a green + to represent the goal point
			drawMarker(g2, GoalX, GoalY, Color.GREEN);
		
		}
		
		// Draw original obstacles
		if (showObstacles)
		{
			g2.setColor(Color.BLUE);
		
			// Connect all of the vertices of all the obstacles
			for (int i = 0; i < Vertices.size(); i++)
			{
				for (int j = 0; j < Vertices.get(i).length - 1; j++)
				{
					// Connect vertex j to vertex j + 1
					g2.draw(new Line2D.Double(
							Vertices.get(i)[j][0], 
							Vertices.get(i)[j][1], 
							Vertices.get(i)[j + 1][0], 
							Vertices.get(i)[j + 1][1]));
				}
				
				// Connect the final vertex to the first
				g2.draw(new Line2D.Double(
						Vertices.get(i)[0][0], 
						Vertices.get(i)[0][1], 
						Vertices.get(i)[Vertices.get(i).length - 1][0], 
						Vertices.get(i)[Vertices.get(i).length - 1][1]));
			}
		}
		
		// Draw grown obstacles
		if (showGrownObstacles)
		{
			g2.setColor(Color.PINK);
		
			// Connect all of the vertices of all the obstacles
			for (int i = 0; i < grownObstacles.size(); i++)
			{
				for (int j = 0; j < grownObstacles.get(i).length - 1; j++)
				{
					// Connect vertex j to vertex j + 1
					g2.draw(new Line2D.Double(
							grownObstacles.get(i)[j][0], 
							grownObstacles.get(i)[j][1], 
							grownObstacles.get(i)[j + 1][0], 
							grownObstacles.get(i)[j + 1][1]));
				}
				
				// Connect the final vertex to the first
				g2.draw(new Line2D.Double(
						grownObstacles.get(i)[0][0], 
						grownObstacles.get(i)[0][1], 
						grownObstacles.get(i)[Vertices.get(i).length - 1][0], 
						grownObstacles.get(i)[Vertices.get(i).length - 1][1]));
			}
		}
		
		// Draw grown obstacles
		if (showConvex)
		{
			g2.setColor(Color.GREEN);
			
			// Connect all of the vertices of all the obstacles
			for (int i = 0; i < ConvexHull.size(); i++)
			{
				//System.out.println("i:"+i);
				for (int j = 0; j < ConvexHull.get(i).length - 1; j++)
				{
					//System.out.println("j:"+j);
					// Connect vertex j to vertex j + 1
					g2.draw(new Line2D.Double(
							ConvexHull.get(i)[j][0], 
							ConvexHull.get(i)[j][1], 
							ConvexHull.get(i)[j + 1][0], 
							ConvexHull.get(i)[j + 1][1]));
				}
				
				// Connect the final vertex to the first
				if(ConvexHull.get(i)!=null && ConvexHull.get(i).length >0)
				g2.draw(new Line2D.Double(
						ConvexHull.get(i)[0][0], 
						ConvexHull.get(i)[0][1], 
						ConvexHull.get(i)[ConvexHull.get(i).length - 1][0], 
						ConvexHull.get(i)[ConvexHull.get(i).length - 1][1]));
			}
		}
		
		// Draw the visibility graph
		if (showVisibilityGraph)
		{
			drawVisibilityGraph(g2);
		}
		
		
		// Draw the optimal path
		if (showOptimalPath)
		{
			g2.setColor(Color.CYAN);
			for(int i=1;i< shortestPath.length;i++)
			{
				
				g2.setColor(Color.BLUE);
				g2.fillOval(
						(int)shortestPath[i-1][0]-2, 
						(int)shortestPath[i-1][1]-2, 4,4);
				
				g2.setColor(Color.CYAN);
				g2.draw(new Line2D.Double(
						shortestPath[i-1][0], 
						shortestPath[i-1][1], 
						shortestPath[i][0], 
						shortestPath[i][1]));
			}
		}
	}

	public double[] adjustPoint(Point2D point)
	{
		double[] p = new double[2];
		p[0]= point.getX()* SCALE_FACTOR + this.getWidth() / 3;
		p[1]= point.getY()* SCALE_FACTOR *-1 + this.getHeight() / 2;
		return p;
	}
	
	
	public void AdjustPoint(double X, double Y, String Choice) 
	{
		if (Choice.equals("Start"))
		{
			// Adjust for proper Swing display
			StartX = X * SCALE_FACTOR + this.getWidth() / 3;
			StartY = Y * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
		else
		{
			// Adjust for proper Swing display
			GoalX = X * SCALE_FACTOR + this.getWidth() / 3;
			GoalY = Y * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
	}
	
	public double[][] AdjustObstacles(double[][] vertices)
	{
		double adjustedVertices[][] = new double[vertices.length][2];
		
		
		for (int i = 0; i < vertices.length; i++)
		{
			// Adjust x and y values for swing
			adjustedVertices[i][0] = vertices[i][0] * SCALE_FACTOR + this.getWidth() / 3;
			adjustedVertices[i][1] = vertices[i][1] * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
		return adjustedVertices;
	}

	
	public void AdjustObstacles(double[][] vertices, String choice)
	{
		double adjustedVertices[][] = new double[vertices.length][2];
		
		
		for (int i = 0; i < vertices.length; i++)
		{
			// Adjust x and y values for swing
			adjustedVertices[i][0] = vertices[i][0] * SCALE_FACTOR + this.getWidth() / 3;
			adjustedVertices[i][1] = vertices[i][1] * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
			
		// Draw original obstacles
		if (choice.equals("Original"))
		{
			// Add the array to the list of original vertices
			this.Vertices.add(adjustedVertices);
		}
		// Draw grown obstacles/convex hull
		else if (choice.equals("showConvex"))
		{
			// Add the array to the list of convex hull vertices
			this.ConvexHull.add(adjustedVertices);
		}
		else if (choice.equals("Grown"))
		{
			// Add the array to the list of convex hull vertices
			this.grownObstacles.add(adjustedVertices);
		}
	}

	
	public void drawMarker(Graphics2D g2,double x, double y, Color c)
	{
		//System.out.println("Marker at: "+x+","+y);
		g2.setColor(c);
		int SIZE=5;
		g2.draw(new Line2D.Double(x - SIZE, y, x + SIZE, y));
		g2.draw(new Line2D.Double(x, y - SIZE, x, y + SIZE));
	}
	
	/**
	 * Draw the visibility graph
	 * @param g2
	 */
	public void drawVisibilityGraph(Graphics2D g2) 
	{
		g2.setColor(Color.RED);
		
		// Connect all of the vertices of all the obstacles
		for (int i = 0; i < visibilityGraph.size() -1; i++)
		{
			//System.out.println("Drawing edge: "+Arrays.toString(visibilityGraph.get(i)[0])+" -> "+Arrays.toString(visibilityGraph.get(i)[1]));
				// Connect vertex j to vertex j + 1
				g2.draw(new Line2D.Double(
						visibilityGraph.get(i)[0][0], 
						visibilityGraph.get(i)[0][1], 
						visibilityGraph.get(i)[1][0], 
						visibilityGraph.get(i)[1][1]));

		}
	}

	public void DrawPath() 
	{

	}
	
	public void Clear()
	{
		showStartPoint = false;
		showGoalPoint = false;
		showObstacles = false;
		showGrownObstacles = false;
		showConvex = false;
		showVisibilityGraph = false;
		showOptimalPath = false;
		
		this.repaint();
	}
}
