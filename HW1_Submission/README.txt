README
Computational Aspects of Robotics - COMS4733
HW 1 - Robot 5
Mike Hernandez [mph2121] and Benjamin Ludman [bsl2106]

NOTE: Set your com port in OpenCommPort.java

Fun Secret Features:
	+ and - keys will change the velocity by 50 
		(only goes into effect when you send another drive command)
	if "Mode" button has focus you can use the arrow keys to drive the robot freestyle
		space bar will sends stop, and will reset to safe mode if wheels drop when doing a wheely

Included files:
CommandSet.java
    Interface for the various low-level commands of the iRobot Create. This interface can easily be
    extended to allow any subset (or, if the user prefers, superset) of operations allowable by the
    iRobot Create.
CommandSetImpl.java
    Implementation of all of the commands in the interface above. This sends the appropriate bytes
    to the iRobot Create and passes the results along to Robot.java.
ControlGUI.java
    Creates, and allows interaction with, the GUI for the program. Generates various buttons,
    checkboxes, radio buttons, sliders, and action events to allow control of the robot. There
    are a few things to take note regarding our implementation:
        1.) Clicking "Forward", "Backward", "Left", or "Right" will cause the iRobot Create to
        move/turn indefinitely, as long as the appropriate values to the right are set to
        default.
        2.) Changing any of these default values (Turn Degree, Move Distance, Move Velocity) will
        override the default values, and provides simple execution of commands like "Go Distance",
        "Turn Degrees", and "Move Velocity". Specifying a distance, followed by "Forward" or
        "Backward", will move the robot forward or backward by the value entered in the text box.
        In keeping with the listed requirements of the assignment, entering a value for Turn Degree
        will cause the program to disregard whether the "Right" or "Left" button has been clicked.
        If the user chooses a value of "-90", any "Turn" button will cause the iRobot Create to
        rotate 90 degrees in the clockwise direction. This is the intended functionality.
Driver.java
    Driver class for this program. Instantiates the GUI, generates it, and displays it.
OpenCommPort.java
    Borrowed heavily from the same project. This file creates the necessary input/output streams
    for interaction with the iRobot Create. In addition to this, the read and write methods have
    been included for data transfer. Within this file, one can change the necessary COM port.
README.txt
	This file
Robot.java
	Main abstraction of a robot that should have a clean interface to work with from the GUI.
	Can be extended in future projects easily by chaining together basic commands. Containts 
    methods for interacting with the robot at a basic level, while allowing the user to create
    more complex behaviors by chaining these operations together.
SensorData.java
	A class for holding and parsing the sensor data. 
	Note that in our current implementation we removed most dependencies on this class in favor
	of hardcoding the sensor request data. Though this class is more robust, since the sensor data
	was unpredictable, it was easier to debug. For future use of the abstractions we made in this
	project we hope to get this class working completely.