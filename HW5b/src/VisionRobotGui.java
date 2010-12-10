import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFrame;

/**
 * Constructs the necessary containers for the images. Used to display several aspects of the BlobTrackingRobot.
 */
public class VisionRobotGui 
{
	
	private JImageDisplay original;
	private JFrame f_original;
	private JImageDisplay mask;
	private JFrame f_mask;
	private URL camera;
	private int[][] range;
	private BlobTracker tracker;
	private DoorFindingRobot doorRobot;
	
	
	private static final int LOW = 0;
	private static final int HIGH = 1;
	private static final int MAX_NUMBER_OF_CHANELS = 3;
	private static final int MAX_NUMBER_OF_RANGE_VALUES = 2; //LOW, HIGH
	 
	
	
	public VisionRobotGui() throws MalformedURLException 
	{
		
		//connect to the camera and pull an image out of it
		camera = new URL("http://192.168.1.8/img/snapshot.cgi?size=2&quality=3");

		//setup the JFrame for display
		doorRobot = new DoorFindingRobot();
		
		
		f_original = new JFrame();
		f_original.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_original.add(original);
		f_original.setResizable(false);
		f_original.setVisible(true);

		mask = new JImageDisplay(doorRobot);	
		f_mask = new JFrame();
		f_mask.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_mask.add(mask);
		f_mask.setResizable(false);
		f_mask.setVisible(true);
		f_mask.setLocation(350,0);

		range = new int[MAX_NUMBER_OF_RANGE_VALUES][MAX_NUMBER_OF_CHANELS];	
	}

	
	public void start() throws IOException
	{
		System.out.println("Starting");
		//create a reader to read the image from the input stream
		Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpeg");
		ImageReader reader = (ImageReader)readers.next();
		tracker = new BlobTracker(original.getWidth());
		
		while(true)
		{
			BufferedImage bi = getNextFrame(reader);
			processFrame(bi);
		}
	}
	
	
	
	
	private void processFrame(BufferedImage bi) {
		//setup the window size according to the image
		f_original.setSize(bi.getWidth(), bi.getHeight());
		f_mask.setSize(bi.getWidth(), bi.getHeight());
		
		doorRobot.doMove(bi, original, mask);
		
	}


	private BufferedImage getNextFrame(ImageReader reader) throws IOException 
	{
		URLConnection yc = camera.openConnection();
		InputStream input = new BufferedInputStream(yc.getInputStream());
		ImageInputStream iis = ImageIO.createImageInputStream(input);
		reader.setInput(iis, true);
		

		//setup decompression parameters
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		Rectangle rect = new Rectangle(0, 0, reader.getWidth(imageIndex), reader.getHeight(imageIndex)); 
		param.setSourceRegion(rect);

		//really does decompress the image
		BufferedImage bi = reader.read(imageIndex, param);
		return bi;
	}
	
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Initializing GUI");
		VisionRobotGui robotGui = new VisionRobotGui();
		robotGui.start();
	}

}
