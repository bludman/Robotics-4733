import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;


public class JMouseListener extends MouseInputAdapter {
	Rectangle currentRect;
	JImageDisplay display;
	
	public JMouseListener(JImageDisplay jid)
	{
		display = jid;
	}
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		currentRect = new Rectangle(x, y, 0, 0);
		updateSize(e);
	}

	public void mouseDragged(MouseEvent e) {
		updateSize(e);
	}

	public void mouseReleased(MouseEvent e) {
		updateSize(e);
		display.setSelectionUpdated();
	}

	void updateSize(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		currentRect.setSize(x - currentRect.x,
				y - currentRect.y);
		display.setSelectedRectangle(currentRect);
	}

}
