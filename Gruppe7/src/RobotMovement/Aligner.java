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

		int deltaAngle = (angleL + angleR) / 2;

		if (deltaAngle < 0) {
			track.pivotAngleRight(-deltaAngle);
		} else {
			track.pivotAngleLeft(deltaAngle);
		}

		track.waitForMotors();

	}

	private int getRightAngle() {
		int angle = 0;
		boolean found = false;
		arm.turnToCenter();
		arm.turnArmRight(90, true);

		while (arm.isMoving() && !found) {
			if (isBackLine()) {
				found = true;
				angle = arm.getArmPosition();
			}
		}

		if (!found) {
			arm.turnToCenter();
			track.pivotAngleRight(90);
			track.waitForMotors();

			arm.turnToCenter();
			arm.turnArmRight(90, true);

			while (arm.isMoving() && !found) {
				if (isBackLine()) {
					found = true;
					angle = -90 + arm.getArmPosition();
				}
			}

			track.pivotAngleLeft(90);
			track.waitForMotors();

		}

		arm.turnToCenter();
		return angle;
	}

	private int getLeftAngle() {
		int angle = 0;
		boolean found = false;
		arm.turnToCenter();
		arm.turnArmLeft(90, true);

		while (arm.isMoving() && !found) {
			if (isBackLine()) {
				found = true;
				angle = arm.getArmPosition();
			}
		}

		if (!found) {
			arm.turnToCenter();
			track.pivotAngleLeft(90);
			track.waitForMotors();

			arm.turnToCenter();
			arm.turnArmLeft(90, true);

			while (arm.isMoving() && !found) {
				if (isBackLine()) {
					found = true;
					angle = 90 + arm.getArmPosition();
				}
			}

			track.pivotAngleRight(90);
			track.waitForMotors();

		}

		arm.turnToCenter();
		return angle;
	}

	private void getOnFrontLine() {
		track.forward();
		while (!isFrontLine()) {
			sleep(10);
		}
		track.stop();
		track.forward(50);
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
