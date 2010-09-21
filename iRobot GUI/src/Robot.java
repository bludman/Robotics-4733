/**
 * 
 */

/**
 * @author Ben
 *
 */
public class Robot {
	OpenCommPort ocp;
	CommandSet command;
	
	Robot()
	{
		ocp= new OpenCommPort();
		command = new CommandSetImpl();
	}

}
