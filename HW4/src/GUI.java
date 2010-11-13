import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class GUI extends JPanel implements ActionListener
{	
	/* Boolean values that determine what to display. */
	boolean StartPoint, GoalPoint, Obstacles, Convex, VisibilityGraph, OptimalPath;
	
	/* Double values that determine where to display the above. */
	double StartX, StartY, GoalX, GoalY;
	
	/* ArrayLists of vertices of objects to be displayed. */
	ArrayList<double[][]> Vertices = new ArrayList<double[][]>();
	ArrayList<double[][]> ConvexHull = new ArrayList<double[][]>();
	
	/* Swing components used in the GUI. */
	public JFrame Frame;
	public JRadioButton ObstaclesButton, ConvexButton, VisibilityButton, PathButton;
	public ButtonGroup Buttons;
	
	/* Factor by which to scale the data. */
	public final int SCALE_FACTOR = 50;
	
	public GUI(
			double StartX, 
			double StartY, 
			double GoalX, 
			double GoalY, 
			ArrayList<double[][]> Vertices, 
			ArrayList<double[][]> ConvexHull)
	{
		// Initialize values for the GUI
		StartPoint = false;
		GoalPoint = false;
		Obstacles = false;
		Convex = false;
		VisibilityGraph = false;
		OptimalPath = false;
		
		// this.Vertices = Vertices;
		// this.ConvexHull = ConvexHull;
		
		// Initialize Swing components
		Frame = new JFrame();
		
		ObstaclesButton = new JRadioButton("Start/Goal, Obstacles");
		ObstaclesButton.setActionCommand("Obstacles");
		ObstaclesButton.addActionListener(this);
		ConvexButton = new JRadioButton("Convex Hulls");
		ConvexButton.setActionCommand("Convex");
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
		Buttons.add(ConvexButton);
		Buttons.add(VisibilityButton);
		Buttons.add(PathButton);
		
		// Add components to the panel
		this.setPreferredSize(new Dimension(800, 600));
		this.add(ObstaclesButton);
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
		this.AdjustPoint(StartX, this.StartY, "Start");
		this.AdjustPoint(GoalX, this.GoalY, "Goal");
		
		for (int i = 0; i < Vertices.size(); i++)
		{
			this.AdjustObstacles(Vertices.get(i), "Original");
		}
		
		for (int i = 0; i < ConvexHull.size(); i++)
		{
			this.AdjustObstacles(ConvexHull.get(i), "Convex");
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Draw start point, goal point, and obstacles
		if (e.getActionCommand().equals("Obstacles"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			StartPoint = true;
			GoalPoint = true;
			
			// Draw the obstacles
			Obstacles = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, and grown obstacles
		if (e.getActionCommand().equals("Convex"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			StartPoint = true;
			GoalPoint = true;
			
			// Draw the obstacles and convex hulls, excluding the boundary of the environment
			Obstacles = true;
			Convex = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, grown obstacles, and visibility graph
		if (e.getActionCommand().equals("Visibility"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			StartPoint = true;
			GoalPoint = true;
			
			// Draw the convex hulls, excluding the boundary of the environment 
			Convex = true;
			
			// Draw the visibility graph
			VisibilityGraph = true;
			
			this.repaint();
		}
		
		// Draw start point, goal point, grown obstacles, and shortest path
		if (e.getActionCommand().equals("Path"))
		{
			// Tell the GUI to display nothing
			this.Clear();
			
			// Draw the start and goal points on the panel
			StartPoint = true;
			GoalPoint = true;
			
			// Draw the grown obstacles, excluding the boundary of the environment
			Convex = true;
			
			// Draw the optimal path
			// VisibilityGraph = true;
			OptimalPath = true;
			
			this.repaint();
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		// Draw start point
		if (StartPoint)
		{
			// Draw a black x to represent the starting point
			g2.setColor(Color.BLACK);
			g2.draw(new Line2D.Double(StartX - 2, StartY - 2, StartX + 2, StartY + 2));
			g2.draw(new Line2D.Double(StartX + 2, StartY - 2, StartX - 2, StartY + 2));
		}
		
		// Draw goal point
		if (GoalPoint)
		{
			// Draw a green + to represent the goal point
			g2.setColor(Color.GREEN);
			g2.draw(new Line2D.Double(GoalX - 2, GoalY, GoalX + 2, GoalY));
			g2.draw(new Line2D.Double(GoalX, GoalY - 2, GoalX, GoalY + 2));
		}
		
		// Draw original obstacles
		if (Obstacles)
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
		if (Convex)
		{
			g2.setColor(Color.RED);
			
			// Connect all of the vertices of all the obstacles
			for (int i = 0; i < ConvexHull.size(); i++)
			{
				for (int j = 0; j < ConvexHull.get(i).length - 1; j++)
				{
					// Connect vertex j to vertex j + 1
					g2.draw(new Line2D.Double(
							ConvexHull.get(i)[j][0], 
							ConvexHull.get(i)[j][1], 
							ConvexHull.get(i)[j + 1][0], 
							ConvexHull.get(i)[j + 1][1]));
				}
				
				// Connect the final vertex to the first
				g2.draw(new Line2D.Double(
						ConvexHull.get(i)[0][0], 
						ConvexHull.get(i)[0][1], 
						ConvexHull.get(i)[ConvexHull.get(i).length - 1][0], 
						ConvexHull.get(i)[ConvexHull.get(i).length - 1][1]));
			}
		}
		
		// Draw the visibility graph
		if (VisibilityGraph)
		{
			g2.setColor(Color.CYAN);
		}
		
		// Draw the optimal path
		if (OptimalPath)
		{
			g2.setColor(Color.YELLOW);
		}
	}

	public void AdjustPoint(double X, double Y, String Choice) 
	{
		if (Choice.equals("Start"))
		{
			// Adjust for proper Swing display
			StartX = X * SCALE_FACTOR + this.getWidth() / 4;
			StartY = Y * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
		else
		{
			// Adjust for proper Swing display
			GoalX = X * SCALE_FACTOR + this.getWidth() / 4;
			GoalY = Y * SCALE_FACTOR * -1 + this.getHeight() / 2;
		}
	}
	
	public void AdjustObstacles(double[][] Vertices, String Choice)
	{
		double FinalVertices[][] = new double[Vertices.length][2];
		
		// Draw original obstacles
		if (Choice.equals("Original"))
		{
			for (int i = 0; i < Vertices.length; i++)
			{
				// Adjust x and y values for swing
				FinalVertices[i][0] = Vertices[i][0] * SCALE_FACTOR + this.getWidth() / 4;
				FinalVertices[i][1] = Vertices[i][1] * SCALE_FACTOR * -1 + this.getHeight() / 2;
			}
			
			// Add the array to the list of original certices
			this.Vertices.add(FinalVertices);
		}
		// Draw grown obstacles/convex hull
		else
		{
			for (int i = 0; i < Vertices.length; i++)
			{
				// Adjust x and y values for swing
				FinalVertices[i][0] = Vertices[i][0] * SCALE_FACTOR + this.getWidth() / 4;
				FinalVertices[i][1] = Vertices[i][1] * SCALE_FACTOR * -1 + this.getHeight() / 2;
			}
			
			// Add the array to the list of convex hull vertices
			this.ConvexHull.add(FinalVertices);
		}
	}

	public void DrawGraph() 
	{
			
	}

	public void DrawPath() 
	{

	}
	
	public void Clear()
	{
		StartPoint = false;
		GoalPoint = false;
		Obstacles = false;
		Convex = false;
		VisibilityGraph = false;
		OptimalPath = false;
		
		this.repaint();
	}
}
