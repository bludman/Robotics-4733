import java.awt.Rectangle;
import java.util.Arrays;


public class JImageProcessing {
	static public JImage threshold(JImage in, int lower[], int upper[])
	{
		int width = in.getWidth();
		int height = in.getHeight();
		int numChannels = in.getNumChannels();
		int stride = in.getStride();

		// The return binary thresholded image
		JImage out = new JImage(width, height, 1);

		int offset;
		int pixel[] = new int [numChannels];
		int outPixel[] = new int [1];
		for (int y = 0; y < height; y++)
		{
			offset = y*stride;
			for (int x = 0; x < width; x++, offset++)
			{
				pixel = in.getPixel(x,y);

				boolean isTrue = true;
				for (int c = 0; c < numChannels; c++)
				{
					isTrue = isTrue && ((pixel[c]>=lower[c]) && (pixel[c]<=upper[c]));
				}

				if (isTrue) outPixel[0] = 255;
				else outPixel[0] = 0;
				out.setPixel(x,y,outPixel);
			}
			//		System.out.println(y);
		}

		return out;
	}
	
	/**
	 * Find the range of colors from a rectangle in an image
	 * @param in
	 * @param rect
	 * @return
	 */
	static public int[][] findRangeByExtrema(JImage in, Rectangle rect)
	{
		int numChannels = in.getNumChannels();
		int pixel[] = new int [numChannels];
		int[] low = new int[numChannels];
		int[] high = new int[numChannels];
		
		for(int chanel = 0;chanel<numChannels;chanel++)
		{
			low[chanel]= Integer.MAX_VALUE;
			high[chanel] = Integer.MIN_VALUE;
		}
		
		int minX = (int) rect.getMinX();
		int maxX = (int) rect.getMaxX();
		int minY = (int) rect.getMinY();
		int maxY = (int) rect.getMaxY();
		
		for(int x=minX;x<maxX;x++)
		{
			for(int y=minY;y<maxY;y++)
			{
				pixel = in.getPixel(x,y);
				System.out.println(Arrays.toString(pixel));
				for(int chanel = 0;chanel<numChannels;chanel++)
				{
					if(pixel[chanel]<low[chanel]){
						System.out.println("Replacing "+low[chanel]+" with "+pixel[chanel]);
						low[chanel]= pixel[chanel];
					}
					
					if(pixel[chanel]>high[chanel])
						high[chanel]= pixel[chanel];
				}
			}
		}
		return new int[][]{low,high};
	}
}
