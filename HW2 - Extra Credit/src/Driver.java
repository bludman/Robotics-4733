/**
 * Driver class for running the GUI for the iRobot Create.
 * 
 * @author Mike Hernandez
 * @author Benjamin Ludman
 *
 */
public class Driver 
{
	// Instance of the GUI with all of the controls.
	static ControlGUI gui = new ControlGUI();
	
	public static void main(String[] args)
	{
		// Generate the GUI and its components.
		gui.generateGUI();
	}
}
