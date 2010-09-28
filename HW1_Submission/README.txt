README
Computational Aspects of Robotics - COMS4733
HW 1 - Robot 5
Mike Hernandez and Benjamin Ludman [bsl2106]

MIKE: I left the SensorData.java file in since there are some small dependencies on it 
and since it doesn't hurt to have it even if Hao doesn't look at it. I started filling in the below.



Included files:
CommandSet.java
CommandSetImpl.java
ControlGUI.java
Driver.java
OpenCommPort.java
README
	This file
Robot.java
	Main abstraction of a robot that should have a clean interface to work with from the GUI.
	Can be extended in future projects easily by chaining together basic commands.
SensorData.java
	A class for holding and parsing the sensor data. 
	Note that in our current implementation we removed most dependencies on this class in favor
	of hardcoding the sensor request data. Though this class is more robust, since the sensor data
	was unpredictable, it was easier to debug. For future use of the abstractions we made in this
	project we hope to get this class working completely.