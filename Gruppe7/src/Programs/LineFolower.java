package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundSensor;

public class LineFolower implements Program {

	private static final int LINE_VALUE = 35;
	private static final int MOVING_SPEED = 600;
	private static final int ROTATING_SPEED = 300;
	LightSensor light;
	TrackSuspension track;
	private boolean running;
	private SensorArm sensorArm;
	private UltrasoundSensor usSensor;
	private LightSweeper lightSweeper;

	public LineFolower(SensorPort portOfLightSensor, SensorPort portOfUsSensor) {
		light = new LightSensor(portOfLightSensor);
		usSensor = new UltrasoundSensor(portOfUsSensor);

		track = new TrackSuspension();
		sensorArm = new SensorArm();
		sensorArm.setSpeed(250);
		lightSweeper = new LightSweeper(sensorArm, this);
	}

	@Override
	public void run() {
		running = true;
		boolean lineFinished = false;

		new Thread(lightSweeper).start();

		while (running && !lineFinished) {
			// testing:
			if (lightSweeper.isLine()) {
				track.setSpeed(MOVING_SPEED);
				// if (isLine()) {
				track.forward();
			} else {
				track.stop();
				lightSweeper.setMoving(false);
				if (searchTrack()) {
					lightSweeper.setMoving(true);
				} else {
					if (checkWalls()) {
						sensorArm.turnToCenter();
						lineFinished = true;
					} else {
						// Fallback-search
					}

				}
			}
			// sleep(10);
		}

		if (running) {
			track.setSpeed(1000);
			track.forward();
			while (!isLine()) {
				sleep(10);
			}
			track.stop();
		}
		running = false;
	}

	public void halt() {
		if (lightSweeper != null) {
			lightSweeper.halt();
		}
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private boolean searchTrack() {
		boolean found = false;
		track.setSpeed(ROTATING_SPEED);
		int angle = 50;

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
			angle += 40;
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
		sleep(50);
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
		while (running && sensorArm.getArmPosition() >= 0)
			;
		while (running && !found && sensorArm.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		return found;
	}

	private boolean checkWalls() {
		boolean wallLeft = false;
		boolean wallRight = false;
		sensorArm.turnToPosition(100, true);
		while (running && sensorArm.getArmPosition() < 0) {
			sleep(10);
		}
		while (running && sensorArm.isMoving() && !wallLeft) {
			if (usSensor.isWall()) {
				wallLeft = true;
			}
		}
		sensorArm.turnToPosition(-100, true);
		while (running && sensorArm.getArmPosition() > 0) {
			sleep(10);
		}
		while (running && sensorArm.isMoving() && !wallRight) {
			if (usSensor.isWall()) {
				wallRight = true;
			}
		}

		return wallLeft && wallRight;

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
		private boolean moving;
		private boolean running;
		boolean moveLeft;
		private LineFolower follower;
		private boolean measurements[];
		private int head;

		public LightSweeper(SensorArm arm, LineFolower follower) {
			measurements = new boolean[130];
			this.follower = follower;
			this.arm = arm;
			moving = true;
			moveLeft = true;
			head = 0;
			push(follower.isLine());
		}

		@Override
		public void run() {
			running = true;
			while (running) {
				while (running && isLine()) {
					if (moveLeft) {
						arm.turnToPosition(8, true);
					} else {
						arm.turnToPosition(-8, true);
					}
					while (running && moving && arm.isMoving()) {
						int pos = arm.getArmPosition();
						push(follower.isLine());
					}
					// int delta = max - min / 2;
					// isLine = max >= min;
					moveLeft = !moveLeft;
				}
				sleep(50);
			}
		}

		public void setMoving(boolean shouldMove) {
			if (shouldMove) {
				push(follower.isLine());
			}
			moving = shouldMove;
		}

		public void halt() {
			running = false;
		}

		public boolean isLine() {
			for (boolean value : measurements)
				if (value)
					return true;

			return false;
		}

		private void push(boolean value) {
			measurements[head++] = value; // insert into Buffer and move head
			head %= measurements.length; // move head to start of array if index
											// is out of bounds
		}
	}

}
