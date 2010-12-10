import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JKeyListener extends KeyAdapter {
	private JImageDisplay display;
	private Robot robot;
	
	
	public JKeyListener(JImageDisplay jid, Robot r)
	{
		System.out.println("initialized");
		display = jid;
		robot= r;
	}
	
	public void keyPressed(KeyEvent e) {
		char pressedKey = e.getKeyChar();
		System.out.println(pressedKey);
		//this is only sample code, only for reference if you want to use
		if(pressedKey == 's') //stop the robot
		{
			System.out.println("stop the robot");
			robot.stop();
		}
		else if(pressedKey == 'c')
		{
			
			boolean setupStatus = robot.setup();
			if (setupStatus)
			{
				robot.start();
			}
		}
		else if(pressedKey == 'm')
		{
			robot.safe();
		}
		else if(pressedKey == 'w')
		{
			robot.driveDirect(100, 100);
		}
	
		//...... you can add more functions
	}
}
