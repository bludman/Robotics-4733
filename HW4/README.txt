README
Computational Aspects of Robotics - COMS4733
HW 4 - Robot 5
Mike Hernandez [mph2121] and Benjamin Ludman [bsl2106]

NOTE: Set your COM port in OpenCommPort.java


Steps to initiate contact with iRobot Create:
1.) Execute the program (initializes DijkstraRobot and connects to the robot)
2.) Click on "Shortest Path" (puts it into safe mode)
	* NOTE: Please wait a few second until connection is established
	* Console will give you a message
3.) Click "Drive" (will send the appropriate commands to the iRobot Create)

Included files:

CommandSet.java
    Interface for the various low-level commands of the iRobot Create. This interface can easily be
    extended to allow any subset (or, if the user prefers, superset) of operations allowable by the
    iRobot Create.

CommandSetImpl.java
    Implementation of all of the commands in the interface above. This sends the appropriate bytes
    to the iRobot Create and passes the results along to Robot.java.

DijkstraRobot.java
    Initializes iRobot Create and contains a method for following a path based on a set of Point2D objects
    in an ArrayList. This method gets the angle between two points using a Math.atan2() calculation, and gets the distance between the two points using a built-in distance method for Point2D. The robot is then told to turn the necessary angle, drive the necessary distance, and return to an angle of zero (original configuration).

Driver.java
    Parses out information from text files, and stores them as start/goal points and arrays of
    vertices. Growing obstacles is done by adding eight points in the shape of an octagon to each
    vertex of an obstacle. Trigonometric functions were used to determine the correct x and y values
    for these new points. The convex hull was generated using the methods described in class.

GUI.java
    Initializes data structures necessary for displaying necessary data, creates and organizes "Drive"
    button and radio buttons, and adds actions to those elements. All obstacles are painted one vertex
    at a time, values are scaled appropriately (defined in this class), and an effort was made to 
    distinguish between different elements of the program.

Obstacle.java
	Keeps track of the shape of obstacles, a grown obstacle and an obstacle's convex hull.
	Can check if an Edge between two points intersects a set of obstacles by sampling the 
	edge at regular intervals.

OpenCommPort.java
    Borrowed heavily from the original project. This file creates the necessary input/output streams
    for interaction with the iRobot Create. In addition to this, the read and write methods have
    been included for data transfer. Within this file, one can change the necessary COM port.

PathFinder.java
	Used for finding the visibility graph given start and goal points, a list of obstacles, 
	the containing wall. Used for finding the shortest path between a start and goal points
	given a visibility graph.

README.txt
    This file

Robot.java
    Main abstraction of a robot that should have a clean interface to work with from the GUI.
    Can be extended in future projects easily by chaining together basic commands. Containts
    methods for interacting with the robot at a basic level, while allowing the user to create
    more complex behaviors by chaining these operations together.
