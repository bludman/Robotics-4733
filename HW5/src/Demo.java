
import java.net.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JFrame;
import java.util.Iterator;
import java.util.Vector;

public class Demo {

	public static void main(String[] args) throws Exception {

		//connect to the camera and pull an image out of it
		//URL camera = new URL("http://192.168.1.2/img/snapshot.cgi?size=2&quality=3");
		URL camera = new URL("http://mikeben.myipcamera.com/img/snapshot.cgi?size=2&quality=3");

		//setup the JFrame for display
		JImageDisplay original = new JImageDisplay();
		JFrame f_original = new JFrame();
		f_original.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_original.add(original);
		f_original.setResizable(false);
		f_original.setVisible(true);

		JImageDisplay mask = new JImageDisplay();
		JFrame f_mask = new JFrame();
		f_mask.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f_mask.add(mask);
		f_mask.setResizable(false);
		f_mask.setVisible(true);

		//create a reader to read the image from the input stream
		Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
		ImageReader reader = (ImageReader)readers.next();

		while(true)
		{
			URLConnection yc = camera.openConnection();
			InputStream input = new BufferedInputStream(yc.getInputStream());
			int inputLength = yc.getContentLength();
			//			System.out.println(inputLength);

			ImageInputStream iis = ImageIO.createImageInputStream(input);
			reader.setInput(iis, true);

			//setup decompression parameters
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			Rectangle rect = new Rectangle(0, 0, reader.getWidth(imageIndex), reader.getHeight(imageIndex)); 
			param.setSourceRegion(rect);

			//really does decompress the image
			BufferedImage bi = reader.read(imageIndex, param);

			//setup the window size according to the image
			f_original.setSize(bi.getWidth(), bi.getHeight());
			f_mask.setSize(bi.getWidth(), bi.getHeight());
			
			//get a JImage out of the decompressed image
			JImage ji = new JImage(bi);
			//			ji.writeOut("c:\\joriginal.jpg");
			//			System.out.println("written");

			//specify the range of RGB channels
			JImage jmask = new JImage(bi);
			int lower[] = {240, 240, 240};
			int upper[] = {255, 255, 255};
			jmask = JImageProcessing.threshold(ji, lower, upper);
			//			jmask.writeOut("c:\\jmask.jpg");

			//blob detection
			Vector<JBlob> jbs = new Vector<JBlob>();
			JBlobDetector jbd = new JBlobDetector();
			jbs = jbd.findBlobs(jmask, 4);

			//update the image in each display window
			original.updateImage(ji.getBufferedImage());
			mask.updateImage(jmask.getBufferedImage());
			
			//check if there is any update for original image
			//and output the selected rectangle if there is an update
			if(original.isSelectionUpdated())
			{
				System.out.println(original.getSelectedRectangle());
			}
			System.out.println("Updating ");
		}
	}
}
