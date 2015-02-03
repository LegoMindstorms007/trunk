package RobotMovement;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LineAligner {

	private static final int LINE_VALUE = 35;
	private static final int ARM_SPEED = 100;
	private LightSensor light;
	private SensorArm arm;
	private TrackSuspension track;

	public LineAligner(SensorPort lightPort) {
		light = new LightSensor(lightPort);
		arm = new SensorArm();
		track = new TrackSuspension();
	}

	public void align() {
		arm.turnArmLeft(90, true);
		track.backward(50);
		while (arm.isMoving()) {
			sleep(25);
		}
		arm.setSpeed(ARM_SPEED);
		int min = 90;
		int max = -90;

		arm.turnToPosition(-90, true);

		while (arm.isMoving()) {
			if (isLine()) {
				int angle = arm.getArmPosition();
				min = Math.min(min, angle);
				max = Math.max(max, angle);
			}
		}

		int delta = (max + min) / 2;

		if (delta < 0) {
			track.pivotAngleLeft(-delta);
		} else
			track.pivotAngleRight(delta);

		track.waitForMotors();
		track.forward(50);
		arm.turnToCenter();

	}

	private boolean isLine() {
		return light.getLightValue() > LINE_VALUE;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
