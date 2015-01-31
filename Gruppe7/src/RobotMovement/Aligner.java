package RobotMovement;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * class containing an algorithm for aligning the robot on a line
 * 
 * @author Dominik Muth
 * 
 */
public class Aligner {
	private LightSensor light;
	private int threshold;
	private boolean frontIsBrighter;
	private TrackSuspension track;
	private SensorArm arm;

	/**
	 * constructs a new Aligner
	 * 
	 * @param portOfLight
	 *            SensorPort of light sensor
	 * @param threshold
	 *            light limit to distinguish between front and back part
	 * @param frontIsBrighter
	 *            whether the front is brighter then the back or not
	 */
	public Aligner(SensorPort portOfLight, int threshold,
			boolean frontIsBrighter) {
		init(threshold, frontIsBrighter);
		light = new LightSensor(portOfLight);
	}

	/**
	 * constructs a new Aligner
	 * 
	 * @param portOfLight
	 *            SensorPort of light sensor
	 * @param threshold
	 *            light limit to distinguish between front and back part
	 * @param frontIsBrighter
	 *            whether the front is brighter then the back or not
	 * @param useFlashlight
	 *            whether the flashlight of the light sensor should be used or
	 *            not
	 */
	public Aligner(SensorPort portOfLight, int threshold,
			boolean frontIsBrighter, boolean useFlashlight) {
		init(threshold, frontIsBrighter);
		light = new LightSensor(portOfLight, useFlashlight);
	}

	private void init(int threshold, boolean frontIsBrighter) {
		arm = new SensorArm();
		track = new TrackSuspension();
		this.threshold = threshold;
		this.frontIsBrighter = frontIsBrighter;
	}

	/**
	 * start aligning
	 */
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

		}

		arm.turnToCenter();
		track.waitForMotors();
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

		}

		track.waitForMotors();
		arm.turnToCenter();
		return angle;
	}

	private void getOnFrontLine() {
		if (isFrontLine()) {
			track.backward();
			while (isFrontLine()) {
				sleep(10);
			}
			track.stop();
		}

		track.forward();
		while (isBackLine()) {
			sleep(10);
		}
		track.stop();
		track.forward(25);
	}

	private boolean isFrontLine() {
		return frontIsBrighter == (light.getLightValue() > threshold);
	}

	private boolean isBackLine() {
		return !isFrontLine();
	}

	/**
	 * sleep method
	 * 
	 * @param milliseconds
	 *            time to sleep in milliseconds
	 */
	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
