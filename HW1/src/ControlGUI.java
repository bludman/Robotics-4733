/**
 * 
 * @author Mike Hernandez, Benjamin Ludman
 *
 */

import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class ControlGUI extends JPanel implements ActionListener, KeyListener
{
	private static final long serialVersionUID = 4428024130270567883L;
	protected Robot robot = new Robot();
	protected JButton Forward, Backward, Stop, TurnLeft, TurnRight, OpenPort, Mode, ReadSensors, Square;
	protected JTextField TurnDegreeBox, MoveDistanceBox, MoveVelocityBox;
	protected JTextArea message; // Note: Can we force this to display standard error/standard out?
	protected JRadioButton MoveVelocityDefault, MoveVelocitySpecify, MoveDistanceDefault, MoveDistanceSpecify, TurnDegreeDefault, TurnDegreeSpecify;
	protected ButtonGroup MoveVelocityGroup, MoveDistanceGroup, TurnDegreeGroup;
	protected JCheckBox BUMP_RIGHT, BUMP_LEFT, WHEEL_DROP_RIGHT, WHEEL_DROP_LEFT, WHEEL_DROP_CASTER, WALL, CLIFF_LEFT, CLIFF_FRONT_LEFT, CLIFF_FRONT_RIGHT, CLIFF_RIGHT, VIRTUAL_WALL;
	protected JSlider WALL_SIGNAL_Slider;
	protected JLabel WALL_SIGNAL_Label;
	protected JPanel Controls, Options[], AllOptions, SensorsBoolean, SensorsNumerical, Other;
	protected boolean loggingEnabled=true;
	
	protected final static int defaultVelocity= 100;
	protected int moveVelocityValue = defaultVelocity;
	protected int moveDistanceValue = 0;
	protected int turnDegreeValue = 0;
	
	public ControlGUI()
	{
		// Buttons classified under Controls
		OpenPort = new JButton("Open");
		OpenPort.setActionCommand("Open");
				
		Mode = new JButton("Mode");
		Mode.setActionCommand("Mode");
		Mode.addKeyListener(this);
		
		Forward = new JButton("Forward");
		Forward.setActionCommand("Forward");
		
		Backward = new JButton("Backward");
		Backward.setActionCommand("Backward");
		
		Stop = new JButton("Stop");
		Stop.setActionCommand("Stop");
		
		TurnLeft = new JButton("Left");
		TurnLeft.setActionCommand("Turn Left");
		
		TurnRight = new JButton("Right");
		TurnRight.setActionCommand("Turn Right");
		
		ReadSensors = new JButton("Read");
		ReadSensors.setActionCommand("Read");
		
		Square = new JButton("Square");
		Square.setActionCommand("Square");
		
		// Adding the Controls to the appropriate panel, empty labels take up space in the GridLayout
		Controls = new JPanel(new GridLayout(3, 3, 20, 20));
		Controls.add(new Label());
		Controls.add(Forward);
		Controls.add(new Label());
		Controls.add(TurnLeft);
		Controls.add(Stop);
		Controls.add(TurnRight);
		Controls.add(new Label());
		Controls.add(Backward);
		
		// Radio buttons classified under Options, and associated actions
		MoveVelocityDefault = new JRadioButton("Default");
		MoveVelocityDefault.setActionCommand("Default Velocity");
		MoveVelocitySpecify = new JRadioButton("Specified");
		MoveVelocitySpecify.setActionCommand("Specify Velocity");
		
		MoveDistanceDefault = new JRadioButton("Default");
		MoveDistanceDefault.setActionCommand("Default Distance");
		MoveDistanceSpecify = new JRadioButton("Specified");
		MoveDistanceSpecify.setActionCommand("Specify Distance");
		
		TurnDegreeDefault = new JRadioButton("Default");
		TurnDegreeDefault.setActionCommand("Default Degree");
		TurnDegreeSpecify = new JRadioButton("Specified");
		TurnDegreeSpecify.setActionCommand("Specify Degree");
		
		// Set up radio button groups
		MoveVelocityGroup = new ButtonGroup();
		MoveVelocityGroup.add(MoveVelocityDefault);
		MoveVelocityGroup.add(MoveVelocitySpecify);
		
		MoveDistanceGroup = new ButtonGroup();
		MoveDistanceGroup.add(MoveDistanceDefault);
		MoveDistanceGroup.add(MoveDistanceSpecify);
		
		TurnDegreeGroup = new ButtonGroup();
		TurnDegreeGroup.add(TurnDegreeDefault);
		TurnDegreeGroup.add(TurnDegreeSpecify);
		
		// Text boxes classified under Options
		MoveVelocityBox = new JTextField(5);
			MoveVelocityBox.setText(Integer.toString(moveVelocityValue));
		MoveDistanceBox = new JTextField(5);
			MoveDistanceBox.setText(Integer.toString(moveDistanceValue));
		TurnDegreeBox = new JTextField(5);
			TurnDegreeBox.setText(Integer.toString(turnDegreeValue));
		
		// Adding the Options to the appropriate panel
		Options = new JPanel[3];
		
		Options[0] = new JPanel(new GridLayout(2, 2));
		Options[0].setBorder(BorderFactory.createTitledBorder("Move Velocity"));
		Options[0].add(MoveVelocityDefault);
		Options[0].add(new JLabel());
		Options[0].add(MoveVelocitySpecify);
		Options[0].add(MoveVelocityBox);

		Options[1] = new JPanel(new GridLayout(2, 2));
		Options[1].setBorder(BorderFactory.createTitledBorder("Move Distance"));
		Options[1].add(MoveDistanceDefault);
		Options[1].add(new JLabel());
		Options[1].add(MoveDistanceSpecify);
		Options[1].add(MoveDistanceBox);
		
		Options[2] = new JPanel(new GridLayout(2, 2));
		Options[2].setBorder(BorderFactory.createTitledBorder("Turn Degrees"));
		Options[2].add(TurnDegreeDefault);
		Options[2].add(new JLabel());
		Options[2].add(TurnDegreeSpecify);
		Options[2].add(TurnDegreeBox);
		
		// Setting Default radio buttons as selected initially
		MoveVelocityDefault.setSelected(true);
		MoveDistanceDefault.setSelected(true);
		TurnDegreeDefault.setSelected(true);
		
		// Setting Specify boxes as disabled initially
		MoveVelocityBox.setEnabled(false);
		MoveDistanceBox.setEnabled(false);
		TurnDegreeBox.setEnabled(false);
		
		message = new JTextArea(5, 25);
		
		// Adding ActionListeners to all necessary elements
		OpenPort.addActionListener(this);
		Mode.addActionListener(this);
		Forward.addActionListener(this);
		Backward.addActionListener(this);
		TurnLeft.addActionListener(this);
		TurnRight.addActionListener(this);
		Stop.addActionListener(this);
		Square.addActionListener(this);
		MoveVelocityDefault.addActionListener(this);
		MoveVelocitySpecify.addActionListener(this);
		MoveDistanceDefault.addActionListener(this);
		MoveDistanceSpecify.addActionListener(this);
		TurnDegreeDefault.addActionListener(this);
		TurnDegreeSpecify.addActionListener(this);
		ReadSensors.addActionListener(this);
		
		// Adding the Other buttons to the appropriate panel
		Other = new JPanel(new FlowLayout());
		Other.setPreferredSize(new Dimension(400, 400));
		Other.add(OpenPort);
		Other.add(Mode);
		Other.add(ReadSensors);
		Other.add(Square);
		Other.add(message);
		
		// Set up checkboxes for boolean sensor data
		BUMP_RIGHT = new JCheckBox("BUMP_RIGHT");
		BUMP_LEFT = new JCheckBox("BUMP_LEFT");
		WHEEL_DROP_RIGHT = new JCheckBox("WHEEL_DROP_RIGHT");
		WHEEL_DROP_LEFT = new JCheckBox("WHEEL_DROP_LEFT");
		WHEEL_DROP_CASTER = new JCheckBox("WHEEL_DROP_CASTER");
		WALL = new JCheckBox("WALL"); 
		CLIFF_LEFT = new JCheckBox("CLIFF_LEFT");
		CLIFF_FRONT_LEFT = new JCheckBox("CLIFF_FRONT_LEFT");
		CLIFF_FRONT_RIGHT = new JCheckBox("CLIFF_FRONT_RIGHT");
		CLIFF_RIGHT = new JCheckBox("CLIFF_RIGHT");
		VIRTUAL_WALL = new JCheckBox("VIRTUAL_WALL");
		
		// Add checkboxes to the panel
		SensorsBoolean = new JPanel(new GridLayout(6, 2));
		SensorsBoolean.add(BUMP_LEFT);
		SensorsBoolean.add(BUMP_RIGHT);
		SensorsBoolean.add(WHEEL_DROP_LEFT);
		SensorsBoolean.add(WHEEL_DROP_RIGHT);
		SensorsBoolean.add(WHEEL_DROP_CASTER);
		SensorsBoolean.add(WALL);
		SensorsBoolean.add(CLIFF_FRONT_LEFT);
		SensorsBoolean.add(CLIFF_FRONT_RIGHT);
		SensorsBoolean.add(CLIFF_LEFT);
		SensorsBoolean.add(CLIFF_RIGHT);
		SensorsBoolean.add(VIRTUAL_WALL);
		
		// Set up sliders and labels for numerical sensor data
		WALL_SIGNAL_Slider = new JSlider(0, 4095, 0);
		WALL_SIGNAL_Label = new JLabel("WALL_SIGNAL");
		
		// Add sliders and labels to the panel
		SensorsNumerical = new JPanel(new GridLayout(2, 1));
		SensorsNumerical.add(WALL_SIGNAL_Slider);
		SensorsNumerical.add(WALL_SIGNAL_Label);
		
		// Adding the panels to the GUI
		AllOptions = new JPanel(new GridLayout(3, 1));
		AllOptions.add(Options[0]);
		AllOptions.add(Options[1]);
		AllOptions.add(Options[2]);
		
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(600, 500));
		this.add(Controls);
		this.add(AllOptions);
		this.add(SensorsBoolean);
		this.add(SensorsNumerical);
		this.add(Other);
		
		
	}
	
	public void generateGUI()
	{
		// Create the frame and set various options
		JFrame frame = new JFrame("ControlGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ControlGUI newGUI = new ControlGUI();
		newGUI.setOpaque(true);
		frame.setContentPane(newGUI);
		frame.setSize(new Dimension(600, 500));
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// Open
		if (e.getActionCommand().equals("Open"))
		{
			boolean setupStatus=robot.setup();
			log("Setting up robot: " + setupStatus);
			
			if(setupStatus)
			{
				robot.start();
				log("Open interface started");
				OpenPort.setEnabled(false);
			}
			
		}
		
		// Default velocity selected
		if (e.getActionCommand().equals("Default Velocity"))
		{
			MoveVelocityBox.setEnabled(false);
			//moveVelocityValue = defaultVelocity;
		}
		
		// Specified velocity selected
		if (e.getActionCommand().equals("Specify Velocity"))
		{
			MoveVelocityBox.setEnabled(true);
		}
		
		// Default distance selected
		if (e.getActionCommand().equals("Default Distance"))
		{
			MoveDistanceBox.setEnabled(false);
		}
		
		// Specified distance selected
		if (e.getActionCommand().equals("Specify Distance"))
		{
			MoveDistanceBox.setEnabled(true);
		}
		
		// Default turn degrees selected
		if (e.getActionCommand().equals("Default Degree"))
		{
			TurnDegreeBox.setEnabled(false);
		}
		
		// Specified turn degrees selected
		if (e.getActionCommand().equals("Specify Degree"))
		{
			TurnDegreeBox.setEnabled(true);
		}
		
		// Turn Left
		if (e.getActionCommand().equals("Turn Left"))
		{
			updateVelocity();
			if (TurnDegreeBox.isEnabled())
			{
				turnDegreeValue = Integer.parseInt(TurnDegreeBox.getText());
				robot.turnDegrees(moveVelocityValue, turnDegreeValue);
			}
			else
			{
				robot.turnInPlace(Robot.DIRECTION.LEFT, moveVelocityValue);
			}
		}
		
		// Turn Right
		if (e.getActionCommand().equals("Turn Right"))
		{
			updateVelocity();
			if (TurnDegreeBox.isEnabled())
			{
				turnDegreeValue = Integer.parseInt(TurnDegreeBox.getText());
				robot.turnDegrees(moveVelocityValue, turnDegreeValue);
			}
			else
			{
				robot.turnInPlace(Robot.DIRECTION.RIGHT, moveVelocityValue);
			}
		}
		
		// Drive forward
		if (e.getActionCommand().equals("Forward"))
		{
			
			updateVelocity();
			if (MoveDistanceBox.isEnabled())
			{
				moveDistanceValue = Integer.parseInt(MoveDistanceBox.getText());
				robot.driveDistance(moveVelocityValue, moveDistanceValue);
				
			}
			else
			{
				robot.driveDirect(moveVelocityValue, moveVelocityValue);
			}
		}
		
		// Drive backwards
		if (e.getActionCommand().equals("Backward"))
		{
			updateVelocity();
			if (MoveDistanceBox.isEnabled())
			{
				moveDistanceValue = Integer.parseInt(MoveDistanceBox.getText());
				robot.driveDistance(-moveVelocityValue, -moveDistanceValue);
				
			}
			else
			{
				robot.driveDirect(-moveVelocityValue, -moveVelocityValue);
			}
		}
		
		// Stop
		if (e.getActionCommand().equals("Stop"))
		{
			robot.stop();
		}
		
		// Read Sensors
		if (e.getActionCommand().equals("Read"))
		{
			log("Reading sensor data");
			clearCheckboxes();
			
			byte[] data = new byte[1];
			data = robot.readBumpsAndWheelDrops();
			log(String.valueOf(Arrays.toString(data)));
			
			WHEEL_DROP_CASTER.setSelected(getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.WHEEL_DROP_CASTER, data[0]));
			WHEEL_DROP_LEFT.setSelected(getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.WHEEL_DROP_LEFT, data[0]));
			WHEEL_DROP_RIGHT.setSelected(getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.WHEEL_DROP_RIGHT, data[0]));
			BUMP_LEFT.setSelected(getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_LEFT, data[0]));
			BUMP_RIGHT.setSelected(getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS.BUMP_RIGHT, data[0]));
		
			
			data = new byte[6];
			data = robot.readWallsAndCliffs();
			log(String.valueOf(Arrays.toString(data)));
			
			WALL.setSelected(byteToBool(data[0]));
			CLIFF_LEFT.setSelected(byteToBool(data[1]));
			CLIFF_FRONT_LEFT.setSelected(byteToBool(data[2]));
			CLIFF_FRONT_RIGHT.setSelected(byteToBool(data[3]));
			CLIFF_RIGHT.setSelected(byteToBool(data[4]));
			VIRTUAL_WALL.setSelected(byteToBool(data[5]));
			
			
			data = new byte[2];
			data = robot.readWallSignal();
			int convertedData=(int)(data[0] << 8) + data[1];
			WALL_SIGNAL_Slider.setValue(convertedData);
			WALL_SIGNAL_Label.setText("WALL_SIGNAL: "+convertedData);
			log(Arrays.toString(data)+"convertedData: "+convertedData);
		}
		
		// Square
		if (e.getActionCommand().equals("Square"))
		{
			if (MoveVelocityBox.isEnabled())
			{
				moveVelocityValue = Integer.parseInt(MoveVelocityBox.getText());
			}
			robot.driveSquare(moveVelocityValue, 400);
		}
		
		// Safe Mode
		if (e.getActionCommand().equals("Mode"))
		{
			log("Initiating safe mode");
			robot.safe();
		}
	}

	/**
	 * Log a message for the user
	 * @param messageText
	 */
	private void log(String messageText) 
	{
		if(!loggingEnabled)
			return;
		
		message.setText(messageText+"\n"+message.getText());
		System.out.println(messageText);
	}

	/**
	 * Clear the sensor display checkboxes
	 */
	private void clearCheckboxes() {
		BUMP_RIGHT.setSelected(false);
		BUMP_LEFT.setSelected(false);
		WHEEL_DROP_RIGHT.setSelected(false);
		WHEEL_DROP_LEFT.setSelected(false);
		WHEEL_DROP_CASTER.setSelected(false);
		WALL.setSelected(false);
		CLIFF_LEFT.setSelected(false);
		CLIFF_FRONT_LEFT.setSelected(false);
		CLIFF_FRONT_RIGHT.setSelected(false);
		CLIFF_RIGHT.setSelected(false);
		VIRTUAL_WALL.setSelected(false);
	}
	
	/**
	 * Get the boolean value of a specific sensor in the BUMPS_AND_WHEEL_DROPS sensor packet 
	 * @param sensor the sensor to read
	 * @param rawData the raw data from which to interpret the value
	 * @return
	 */
	public static boolean getBumpOrWheelDropStatus(Robot.BUMPS_AND_WHEEL_DROPS sensor, byte rawData){
		/*
		 * Get the raw value of the BUMPS_AND_WHEEL_DROPS sensor and then parse the
		 * specific sensor boolean value using a bitwise AND to get the desired bit
		 */
		return (rawData & (1 << sensor.ordinal())) == 0 ? false : true;
	}
	
	/** Convert a byte to a boolean where x==0->false, otherwise true */
	private static boolean byteToBool(byte x)
	{
		return x == 0 ? false : true;
	}
	
	/**
	 * Update the velocity
	 */
	private void updateVelocity()
	{
		if (MoveVelocityBox.isEnabled())
			moveVelocityValue = Integer.parseInt(MoveVelocityBox.getText());
		else
			moveVelocityValue = defaultVelocity;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_UP)
			actionPerformed(new ActionEvent(this,1,"Forward"));
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
			actionPerformed(new ActionEvent(this,1,"Backward"));
		else if (e.getKeyCode()==KeyEvent.VK_LEFT)
			actionPerformed(new ActionEvent(this,1,"Turn Left"));	
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
			actionPerformed(new ActionEvent(this,1,"Turn Right"));
		else if (e.getKeyCode()==KeyEvent.VK_SPACE)
			actionPerformed(new ActionEvent(this,1,"Stop"));
		else if (e.getKeyCode()==KeyEvent.VK_EQUALS){
			MoveVelocityBox.setText(Integer.toString(Integer.parseInt(MoveVelocityBox.getText())+50));
			actionPerformed(new ActionEvent(this,1,"Specify Velocity"));
		}
		else if (e.getKeyCode()==KeyEvent.VK_MINUS){
			MoveVelocityBox.setText(Integer.toString(Integer.parseInt(MoveVelocityBox.getText())-50));
			actionPerformed(new ActionEvent(this,1,"Specify Velocity"));
		}
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_UP)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_DOWN)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_LEFT)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_SPACE)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_EQUALS)
			robot.stop();
		else if (e.getKeyCode()==KeyEvent.VK_MINUS)
			robot.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
