
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
}
