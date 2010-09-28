/**
 * Provides communication between the user and the iRobot Create. Influenced heavily by the sample program with a similar name.
 * 
 * @author Mike Hernandez, Benjamin Ludman
 */

import javax.comm.*;
import java.util.*;
import java.io.*;

public class OpenCommPort 
{
	// Constructs necessary for interaction with the iRobot Create.
	SerialPort port = null;
	OutputStream os = null;
	InputStream is = null;
	
	// Set baud rate
	public static final int BAUD = 57600;
	
	// Set COM port name
	String wantedPortName = "COM7";
	
	public int setUpBam()
	{
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId = null;
		
		while (portIdentifiers.hasMoreElements())
		{
			CommPortIdentifier pid = (CommPortIdentifier)portIdentifiers.nextElement();
			
			if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL && pid.getName().equals(wantedPortName))
			{
				portId = pid;
				System.out.println("Found port: " + pid.getName());
				break;
			}
		}
			
		try
		{
			port = (SerialPort)portId.open(
					"Driver", 
					10000);
		}
		catch(PortInUseException e)
		{
			System.err.println("Port already in use: " + e);
			System.exit(1);
		}
		
		try
		{
			port.setSerialPortParams(
					57600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		}
		catch (UnsupportedCommOperationException e)
		{
			System.err.println("Failed to set up port: " + e);
		}
		System.out.println("Port should be open now");
		return 0;
	}

	public void write(byte[] data)
	{
		try
		{
			os = port.getOutputStream();
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e);
		}
		
		try
		{
			os.write(data);
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e);
		}
	}
	
	public void read(byte[] data)
	{
		try
		{
			is = port.getInputStream();
		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e);
		}
		
		try
		{
			is.read(data);
		}
		catch(IOException e)
		{
			System.err.println("IOException: " + e);
		}
	}
}
