package Test;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;

public class LineFolower implements Runnable {

	private static final int LINE_VALUE = 35;
	private static final int MOVING_SPEED = 500;
	private static final int ROTATING_SPEED = 250;
	LightSensor light;
	TrackSuspension track;
	private boolean running;
	private SensorArm sensorArm;
	private LightSweeper lightSweeper;

	public LineFolower(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);

		track = new TrackSuspension();
		track.setSpeed(MOVING_SPEED);
		sensorArm = new SensorArm();
		sensorArm.setSpeed(200);
		lightSweeper = new LightSweeper(sensorArm, this);
	}

	@Override
	public void run() {
		running = true;

		new Thread(lightSweeper).start();

		while (running) {
			/*
			 * // testing: new Thread(lightSweeper).start(); if
			 * (lightSweeper.isLine()) { // if (isLine()) { track.forward(); }
			 * else { lightSweeper.setMoving(false); track.stop(); if
			 * (searchTrack()) { lightSweeper.setMoving(true); } } // sleep(10);
			 */
		}
	}

	public void halt() {
		if (lightSweeper != null) {
			lightSweeper.halt();
		}
		running = false;
	}

	private boolean searchTrack() {
		boolean found = false;
		track.setSpeed(ROTATING_SPEED);
		int angle = 20;

		while (!found && angle <= 100) {
			if (checkRight(angle)) {
				sensorArm.turnToCenter();
				track.pivotAngleRight(angle);
				found = true;
			} else if (checkLeft(angle)) {
				sensorArm.turnToCenter();
				track.pivotAngleLeft(angle);
				found = true;
			}
			angle += 20;
		}

		while (running && track.motorsMoving()) {
			if (isLine()) {
				track.stop();
			}
		}
		track.setSpeed(MOVING_SPEED);
		return found;
	}

	public boolean isLine() {
		return light.getLightValue() >= LINE_VALUE;
	}

	/**
	 * Adjusts Robots angle if there is a small drift
	 * 
	 * @param deltaAngle
	 *            angle of abnormality
	 */
	public void adjustRobot(int deltaAngle) {
		if (deltaAngle < 0)
			track.turnLeft();
		else
			track.turnRight();
		sleep(5);
		track.forward();
	}

	private boolean checkLeft(int angle) {
		boolean found = false;
		sensorArm.turnToPosition(angle, true);
		while (sensorArm.getArmPosition() <= 0)
			;
		while (running && !found && sensorArm.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		return found;
	}

	private boolean checkRight(int angle) {
		boolean found = false;
		sensorArm.turnToPosition(-angle, true);
		while (sensorArm.getArmPosition() >= 0)
			;
		while (running && !found && sensorArm.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		return found;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class LightSweeper implements Runnable {

		private SensorArm arm;
		private boolean isLine;
		private boolean moving;
		private boolean running;
		boolean moveLeft;
		private LineFolower follower;

		public LightSweeper(SensorArm arm, LineFolower follower) {
			this.follower = follower;
			this.arm = arm;
			isLine = true;
			moving = true;
			moveLeft = true;
		}

		@Override
		public void run() {
			arm.turnToPosition(5);
			running = true;
			while (running) {
				while (running && moving) {
					if (moveLeft) {
						arm.turnToPosition(5, true);
					} else {
						arm.turnToPosition(-5, true);
					}
					int min = 5;
					int max = -5;
					while (running && moving && arm.isMoving()) {
						int pos = arm.getArmPosition();
						if (follower.isLine()) {
							min = Math.min(min, pos);
							max = Math.max(max, pos);
						}
					}
					int delta = max - min / 2;
					isLine = max >= min;
					if (isLine) {
						if (Math.abs(delta) >= 3)
							follower.adjustRobot(delta);
					}
					moveLeft = !moveLeft;
				}
			}
		}

		public void setMoving(boolean shouldMove) {
			if (shouldMove) {
				isLine = true;
				moveLeft = true;
				arm.turnToPosition(5);
			}
			moving = shouldMove;
		}

		public void halt() {
			running = false;
		}

		public boolean isLine() {
			return isLine;
		}
	}

}
