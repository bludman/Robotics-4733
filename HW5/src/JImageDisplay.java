import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class JImageDisplay extends JPanel {
	
	private static final long serialVersionUID = 1L;

	//current display image
	private BufferedImage image;
	
	//current selected rectangle
	private Rectangle rect;
	private Dimension size = new Dimension();
	
	//current tracked blob
	private ArrayList<Rectangle> blobRectangles;
	
	//tracked points
	public JPoint2D redPoint;
	public JPoint2D bluePoint;
	private static final int RADIUS = 5;
	
	//flag to indicate whether there is a new selection since last query of isSelectionUpdated
	private boolean selectionUpdated;
	
	private JMouseListener mouseListener;
	private JKeyListener keyListener;

	public JImageDisplay(Robot robot) {
		//initialize two listeners
		mouseListener = new JMouseListener(this);
		keyListener = new JKeyListener(this, robot);
		
		//add mouse listener
		addMouseListener(mouseListener);
	    addMouseMotionListener(mouseListener);
	    
	    //add key listener to the JPanel
	    setFocusable(true);
	    addKeyListener(keyListener);
	    rect = new Rectangle();
	    selectionUpdated = false;
	    
	    blobRectangles = new ArrayList<Rectangle>();
	}
	
	public void updateImage(BufferedImage image)
	{
		this.image = image;
		size.setSize(image.getWidth(), image.getHeight());
		this.updateUI();
	}

	/**
	 * Drawing an image can allow for more
	 * flexibility in processing/editing.
	 */
	protected void paintComponent(Graphics g) {
		// Center image in this component.
		int x = (getWidth() - size.width)/2;
		int y = (getHeight() - size.height)/2;
		g.drawImage(image, x, y, this);
		
		// draw a rectangle created by mouse dragging
//		System.out.print(rect.getX());
//		System.out.print(" ");
//		System.out.println(rect.getY());
		g.setColor(Color.red);
		g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		g.setColor(Color.BLUE);
		for(Rectangle blobRectangle: blobRectangles)
			g.drawRect((int)blobRectangle.getX(), (int)blobRectangle.getY(), (int)blobRectangle.getWidth(), (int)blobRectangle.getHeight());
		
		
		if(redPoint!=null)
		{
			g.setColor(Color.red);
			g.fillOval(redPoint.getX()-RADIUS, redPoint.getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
		if(bluePoint!=null)
		{
			g.setColor(Color.blue);
			g.fillOval(bluePoint.getX()-RADIUS, bluePoint.getY()-RADIUS, RADIUS*2, RADIUS*2);
		}
	}

	public Dimension getPreferredSize() { return size; }
	
	public void setSelectedRectangle(Rectangle r)
	{
		rect = new Rectangle(r);
	}
	
	public Rectangle getSelectedRectangle()
	{
		return rect;
	}
	
	public boolean isSelectionUpdated()
	{
		boolean ret = selectionUpdated;
		selectionUpdated = false;
		return ret;
	}
	
	public void setSelectionUpdated()
	{
		selectionUpdated = true;
	}

	public void setBlobs(Vector<JBlob> jbs) {
		System.out.println("Adding "+jbs.size()+" blobs");
		this.blobRectangles = new ArrayList<Rectangle>();

		
		Rectangle biggestRectangle = new Rectangle(0, 0);
		int maxPoints = 0, totalPoints = 0;
		
		for(JBlob blob : jbs)
		{
			totalPoints = blob.getNumPoints();
			if(totalPoints > maxPoints && 
					blob.getBoundingBox().getHeight() *blob.getBoundingBox().getWidth() < this.getHeight()*this.getWidth())
			{
				biggestRectangle= blob.getBoundingBox();
				maxPoints = blob.getNumPoints();
			}
			// System.out.println(blob.getBoundingBox());
		}
		
		blobRectangles.add(biggestRectangle);
		
//		for(JBlob blob : jbs)
//			blobRectangles.add(blob.getBoundingBox());
				
	}

}

