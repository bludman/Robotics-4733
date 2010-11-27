import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Vector;

public class JBlobDetector {

	private static final int BACKGROUND_PIXEL  = 0;
	
	
	public JBlobDetector()
	{
	}



	// this function finds all the blobs
	// connectivity can be 4 or 8: 4 means only check up, down, left, and right
	// 8 means check all the eight pixels around one 
	public Vector<JBlob> findBlobs(JImage mask, int connectivity)
	{
		Vector<JBlob> blobs = new Vector<JBlob>();

		int imageHeight = mask.getHeight();
		int imageWidth = mask.getWidth();
		
		for (int row = 0; row < imageHeight; ++row)
		{
			for (int col = 0; col < imageWidth; ++col)
			{
				if(isMarked(mask.getPixel(col, row)))
				{
					JBlob b = growBlob(blobs,mask, col, row, connectivity);
					if(b.getNumPoints()>1)
						blobs.add(b);
					else
						System.out.println("Not adding tiny blob");
				}
			}
		}
		
		return blobs;
	}
//	
//	private Vector<JBlob> findBlobs4(JImage mask)
//	{
//		Vector<JBlob> blobs = new Vector<JBlob>();
//
//		return blobs;
//	}
//
//	private static String findSet(Properties map, String key)
//	{
//		if(map==null)
//			throw new NullPointerException("Map is null");
//		
//		if(key==null)
//			return key;
//			//throw new NullPointerException("Key is null");
//		
//		if(map.getProperty(key)!= null && map.getProperty(key).equals(key))
//			return key;
//		else
//			return findSet(map, map.getProperty(key));
//	}
//	
//
//	/** Union two sets by pointing adding the s1 to s2 */
//	private static void unionSet(Properties map, String s1, String s2)
//	{
//		map.setProperty(findSet(map,s1), findSet(map,s2));
//		//set[findSet(s1, set)]=findSet(s2, set);
//	}
//	
//	private Vector<JBlob> findBlobs8(JImage mask)
//	{
//		Vector<JBlob> blobs = new Vector<JBlob>();
//		
//		int imageHeight = mask.getHeight();
//		int imageWidth = mask.getWidth();
//		int[][] imageData = new int[imageHeight][imageWidth];
//		int label=INITIAL_LABEL;
//		Properties labelEquivalents = new Properties();
//		
//		//load the picture into the labeling data structure
//		for (int row = 0; row < imageHeight; ++row)
//		{
//			for (int col = 0; col < imageWidth; ++col)
//			{
//				imageData[row][col]=mask.getPixel(col, row)[0];
//			}
//		}
//		
//		//First pass of labeling
//		for (int row = 0; row < imageHeight; ++row)
//		{
//			for (int col = 0; col < imageWidth; ++col)
//			{
//				int D= ((row-1>=0) && (col-1>=0))? imageData[row-1][col-1] : BACKGROUND_PIXEL;
//				int B= (row-1>=0) ? imageData[row-1][col] : BACKGROUND_PIXEL;
//				int C= (col-1>=0) ? imageData[row][col-1] : BACKGROUND_PIXEL;
//			
//				if(imageData[row][col]==BACKGROUND_PIXEL)
//					;//do nothing, A is background
//				else if (D!=BACKGROUND_PIXEL) //D is labeled
//					imageData[row][col]=D; //label A as D
//				else if (B==BACKGROUND_PIXEL && C==BACKGROUND_PIXEL) //B and C are bg (as is D)
//				{
//					imageData[row][col]=++label;
//					labelEquivalents.setProperty(Integer.toString(label), Integer.toString(label));
//					//labelEquivalents[label]=label; //change to "create set"
//				}
//				else if (B!=BACKGROUND_PIXEL && C==BACKGROUND_PIXEL) //B is labeled
//					imageData[row][col]=B;
//				else if (B==BACKGROUND_PIXEL && C!=BACKGROUND_PIXEL) //C is labeled
//					imageData[row][col]=C;
//				else if (B!=BACKGROUND_PIXEL && C!=BACKGROUND_PIXEL)
//				{
//					if (B==C)
//					{
//						imageData[row][col]=B; //set A to B=C
//					}
//					else
//					{
//						
//						imageData[row][col]=B; //set A to B
//						unionSet(labelEquivalents,Integer.toString(C),Integer.toString(B));			 // record that C=B 				
//					}
//				}				
//			}
//		}
//		
//		/* Scan the image and relabel equivalent regions */
//		for (int row = 0; row < imageHeight; ++row)
//		{
//			for (int col = 0; col < imageWidth; ++col)
//			{
//				imageData[row][col]=Integer.parseInt(findSet(labelEquivalents,Integer.toString(imageData[row][col])));
//			}
//		}
//
//
//		/* MINIMIZE COLORS IN USE- closes gaps between labels so label number are sequential*/
//		int newLabel=0;
//		for(int i=0;i<=label;i++)
//		{
//			if(labelEquivalents.getProperty(Integer.toString(i)).equals(Integer.toString(i)))
//			{
//				labelEquivalents.setProperty(Integer.toString(i), Integer.toString(newLabel++));
//			}
//			blobs.add(new JBlob());
//		}
//		
//		for (int row = 0; row < imageHeight; ++row)
//		{
//			for (int col = 0; col < imageWidth; ++col)
//			{
//				JBlob b = blobs.get(Integer.parseInt(labelEquivalents.getProperty(Integer.toString(imageData[row][col]))));
//				b.addPoint(col, row);
//			}
//		}
//
//		
////		/* Rescan image and reset the labels with newly minimized labels */
////		for (int row = 0; row < imageHeight; ++row)
////		{
////			for (int col = 0; col < imageWidth; ++col)
////			{
////				
////				setPixel(&binaryImage, row, col,labelEquivalents.getProperty(Integer.toString(imageData[row][col]])));
////			}
////		}
//
////		setColors(&binaryImage, newLabel-1);
////
////		/* Write labeled image */
////		if (writeImage(&binaryImage, outputFilename)==0)
////	      printf("Labeled image written\n");
////		else
////			printf("Error writing labeled image\n");
//
//		
//		
//		
//		
//		
//		
//		
//
//		return blobs;
//	}
//
//	
//	private Vector<JBlob> findBlobs8(JImage mask)
//	{
//		Vector<JBlob> blobs = new Vector<JBlob>();
//
//		int imageHeight = mask.getHeight();
//		int imageWidth = mask.getWidth();
//		
//		for (int row = 0; row < imageHeight; ++row)
//		{
//			for (int col = 0; col < imageWidth; ++col)
//			{
//				if(isMarked(mask.getPixel(col, row)))
//				{
//					JBlob b = growBlob(mask, col, row, 8);
//					blobs.add(b);
//				}
//			}
//		}
//		
//		return blobs;
//		
//	}
	
	
	private static boolean isMarked(int[] pixel) 
	{
		return pixel[0]!=BACKGROUND_PIXEL;
	}



	// given a pixel location which is considered as a seed of a blob,
	// this function grows the region from this seed pixel according
	// to the connectivity type
	private static JBlob growBlob(Collection<JBlob> blobs, JImage binaryImage, int startX, int startY, int connectivity)
	{
		//System.out.println("Growing");
		JBlob blob = new JBlob();

		Queue<JPoint2D> q = new LinkedList<JPoint2D>();
		JPoint2D p = new JPoint2D(startX, startY);
		q.add(p);
		
		while(!q.isEmpty())
		{
			JPoint2D m = q.remove();
			if(isMarked(binaryImage.getPixel(m.getX(), m.getY())) && !isContainedInABlob(blobs, m))
			{
				blob.addPoint(m);
				q.addAll(getNeighbors(new Rectangle(binaryImage.getWidth(), binaryImage.getHeight()),m,connectivity));
			}
			
			
		}
		
		return blob;
	}



	private static boolean isContainedInABlob(Collection<JBlob> blobs, JPoint2D m) {

		for(JBlob blob : blobs)
		{
			if(blob.contains(m))
				return true;
		}
		return false;
	}



	private static Collection<? extends JPoint2D> getNeighbors(Rectangle bounds, JPoint2D m,
			int connectivity) {

		ArrayList<JPoint2D> neighbors = new ArrayList<JPoint2D>();
		ArrayList<JPoint2D> finalNeighbors = new ArrayList<JPoint2D>();
		if(connectivity==4)
		{
			//neighbors.add(m.north());
			neighbors.add(m.south());
			//neighbors.add(m.east());
			//neighbors.add(m.west());
		}
		else if(connectivity==8)
		{
		
		}
		else 
			throw new IllegalArgumentException("Invalid connectivity value");
		
		for(JPoint2D p: neighbors)
			if(validNeighbor(bounds,p))
				finalNeighbors.add(p);
				
		return finalNeighbors;
	}



	private static boolean validNeighbor(Rectangle bounds, JPoint2D p) {
		return bounds.contains(p.getX(), p.getY());
	}

}
