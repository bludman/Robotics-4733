import java.util.ArrayDeque;
import java.util.Vector;

public class JBlobDetector {

	public JBlobDetector()
	{
	}



	// this function finds all the blobs
	// connectivity can be 4 or 8: 4 means only check up, down, left, and right
	// 8 means check all the eight pixels around one 
	public Vector<JBlob> findBlobs(JImage mask, int connectivity)
	{
		if(connectivity==4)
			return findBlobs4(mask);
		else if(connectivity==8)
			return findBlobs8(mask);
		else 
			throw new IllegalArgumentException("Invalid connectivity value");
	}
	
	private Vector<JBlob> findBlobs4(JImage mask)
	{
		Vector<JBlob> blobs = new Vector<JBlob>();

		return blobs;
	}
	
	private Vector<JBlob> findBlobs8(JImage mask)
	{
		Vector<JBlob> blobs = new Vector<JBlob>();

		return blobs;
	}

	// given a pixel location which is considered as a seed of a blob,
	// this function grows the region from this seed pixel according
	// to the connectivity type
	public JBlob growBlob(JImage mask, int startX, int startY, int connectivity)
	{
		JBlob blob = new JBlob();

		return blob;
	}

}
