

public interface CommandSet {

	public abstract byte[] Start();

	public abstract byte[] Baud(int baudCode);

	public abstract byte[] Control();

	public abstract byte[] Safe();

	public abstract byte[] Full();

	public abstract byte[] Spot();

	public abstract byte[] Cover();

	public abstract byte[] Demo(int demoMode);

	public abstract byte[] Drive(int velocityHigh, int velocityLow,
			int radiusHigh, int radiusLow);

	public abstract byte[] LowSideDrivers(int outputBits);

	public abstract byte[] LEDs(int LEDBits, int color, int intensity);

	public abstract byte[] Song();

	public abstract byte[] Play(int songNumber);

	public abstract byte[] CoverAndDock();

	public abstract byte[] PMWLowSideDrivers(int lsd2, int lsd1, int lsd0);

	public abstract byte[] DriveDirect(int rightHigh, int rightLow, int leftHigh,
			int leftLow);

	public abstract byte[] DigitalOutputs(int outputBits);

	public abstract byte[] Stream();

	public abstract byte[] QueryList();

	public abstract byte[] PauseResumeStream(int range);

	public abstract byte[] SendIR(int value);

	public abstract byte[] Script();

	public abstract byte[] PlayScript();

	public abstract byte[] StopScript();

	public abstract byte[] WaitTime(int time);

	public abstract byte[] WaitDistance(int highDistance, int lowDistance);

	public abstract byte[] WaitAngle(int highAngle, int lowAngle);

	public abstract byte[] WaitEvent(int eventID);

}