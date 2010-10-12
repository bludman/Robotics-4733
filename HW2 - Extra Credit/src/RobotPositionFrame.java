import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class RobotPositionFrame //extends JFrame {
{

	protected RobotPositionPanel positionPanel;
	protected JFrame frame;
	
	public RobotPositionFrame() 
	{
		
		
	}
	
	public void generateGUI()
	{
		positionPanel= new RobotPositionPanel();
		
		// Create the frame and set various options
		JFrame frame = new JFrame("Robot Position Frame");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		frame.setSize(new Dimension(600, 500));
		
	
		
		
		frame = new JFrame("Robot Postition");
		frame.setPreferredSize(new Dimension(800,800));
		
		// set up the panel we will draw in
		positionPanel.setPreferredSize(new Dimension(1010, 700));
		positionPanel.setBackground(Color.white);
		frame.getContentPane().add(positionPanel, BorderLayout.CENTER);

		positionPanel.addPoint(100, 0,180);
		positionPanel.addPoint(-100, -100,90);
		positionPanel.addPoint(50, -200, 180);
		
		frame.pack();
		frame.setVisible(true);
		
		
	}

}
