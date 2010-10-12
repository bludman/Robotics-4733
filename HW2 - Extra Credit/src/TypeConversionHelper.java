/**
 * 
 */

/**
 * Helper class to convert from one type to another.
 * @author Ben
 *
 */
public class TypeConversionHelper {
	
	
	public static int byteToInt(byte[] data) 
	{
		int distance = (int)((data[0] << 8) & 0x00FF) + (int)(data[1] & 0x00FF);
		return distance;
	}

}
