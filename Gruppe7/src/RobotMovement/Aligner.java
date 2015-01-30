package RobotMovement;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class Aligner {
	private LightSensor light;
	private int threshold;
	private boolean frontIsBrighter;
	private TrackSuspension track;
	private SensorArm arm;

	public Aligner(SensorPort portOfLight, int threshold,
			boolean frontIsBrighter) {
		arm = new SensorArm();
		track = new TrackSuspension();
		light = new LightSensor(portOfLight);
		this.threshold = threshold;
		this.frontIsBrighter = frontIsBrighter;
	}

	public void align() {
		track.setSpeed(300);
		arm.setSpeed(150);

		getOnFrontLine();

		int angleL = getLeftAngle();
		int angleR = getRightAngle();

	}

	private int getRightAngle() {
		arm.turnArmRight(90, true);
		return 0;
	}

	private int getLeftAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	private void getOnFrontLine() {
		while (!isFrontLine()) {
			track.forward(1);
		}
	}

	private boolean isFrontLine() {
		return frontIsBrighter == (light.getLightValue() > threshold);
	}

	private boolean isBackLine() {
		return !isFrontLine();
	}

	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
