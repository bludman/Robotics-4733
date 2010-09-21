/**
 * 
 * @author Mike Hernandez, Benjamin Ludman
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlGUI extends JPanel implements ActionListener
{
	protected JButton Forward, Backward, TurnLeft, TurnRight, OpenPort, Mode;
	protected JTextArea message; // Note: Can we force this to display standard error/standard out?
	// TODO: Elements to display sensor information.
	
	public void generateGUI()
	{
		JFrame frame = new JFrame("ControlGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ControlGUI newGUI = new ControlGUI();
		newGUI.setOpaque(true);
		frame.setContentPane(newGUI);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Template for reaction to button presses. Also need to use Open.setActionCommand("Open") for each button.
		if (e.getActionCommand().equals("Open"))
		{
		}
	}
}
