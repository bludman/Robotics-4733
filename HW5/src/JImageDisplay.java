import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class JImageDisplay extends JPanel {
	
	//current display image
	BufferedImage image;
	
	//current selected rectangle
	Rectangle rect;
	Dimension size = new Dimension();
	
	//flag to indicate whether there is a new selection since last query of isSelectionUpdated
	boolean selectionUpdated;
	
	JMouseListener mouseListener;
	JKeyListener keyListener;

	public JImageDisplay() {
		//initialize two listeners
		mouseListener = new JMouseListener(this);
		keyListener = new JKeyListener(this);
		
		//add mouse listener
		addMouseListener(mouseListener);
	    addMouseMotionListener(mouseListener);
	    
	    //add key listener to the JPanel
	    setFocusable(true);
	    addKeyListener(keyListener);
	    rect = new Rectangle();
	    selectionUpdated = false;
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

}

