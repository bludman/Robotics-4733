import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * 
 */

/**
 * @author Ben
 *
 */
public class RobotPositionPanel extends JPanel {

	ArrayList<PositionAndOrientation> points= new ArrayList<PositionAndOrientation>();
	private final static int X_CENTER= 400;
	private final static int Y_CENTER= 400;
	
	
	public void addPoint(int x, int y, double angle)
	{
		points.add(new PositionAndOrientation(x, y,angle));
	}

	public void paintComponent(Graphics g1)
	{
		Graphics2D g= (Graphics2D)g1;
		
		//g.setColor(Color.WHITE);
		//g.fillRect(0, 0, getWidth(), getHeight());
		
		
		g.setColor(Color.GRAY);
		g.drawLine(0, Y_CENTER,getWidth(), Y_CENTER); //draw horizontal axis
		g.drawLine(X_CENTER, 0,X_CENTER, getHeight()); //draw vertical axis
		
		
		
		for(PositionAndOrientation p: points)
		{
			drawBot(g, p.getX(), p.getY(),p.getAngle());
		}
	}    
	
	private void drawBot(Graphics2D g, double x, double y, double angle)
	{
		int botRadius=10;
		g.setColor(Color.yellow);
		int botCenterX=X_CENTER+(int)x;
		int botCenterY=Y_CENTER-(int)y;
		g.fillOval(botCenterX-botRadius, botCenterY-botRadius, 2*botRadius, 2*botRadius);
		double x1,y1;

		int orientationMarker=(int) (botRadius*1.5);
		
		x1=orientationMarker*Math.cos(angle*Math.PI/180);
		y1=orientationMarker*Math.sin(angle*Math.PI/180);
		g.setColor(Color.blue);
		//System.out.println("x: "+x+" y: "+y+" x1: "+x1+" y1: "+y1);
		
		g.drawLine(botCenterX, botCenterY, botCenterX+(int)x1, botCenterY+(int)y1);
	}
	
	public RobotPositionPanel() {
		// TODO Auto-generated constructor stub
	}

	}
