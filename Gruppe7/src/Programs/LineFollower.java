package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

/**
 * line follower program
 * 
 * @author Dominik Muth
 * 
 */
public class LineFollower implements Program {

	protected static final int LINE_VALUE = 35;
	private static final int MOVING_SPEED = 600;
	private static final int ROTATING_SPEED = 300;
	private static final int MANIPULATION_SPEED = -200;
	private static final int MANIPULATION_TIME = 1000;
	LightSensor light;
	TrackSuspension track;
	private boolean running;
	private SensorArm sensorArm;
	private UltrasoundSensor usSensor;
	private LightSweeper lightSweeper;
	private int deltaSpeed;
	protected boolean lineFinished;
	private long setSpeedBackAt;

	/**
	 * Constructs a line follower
	 * 
	 * @param portOfLightSensor
	 *            SensorPort of the light sensor
	 * @param portOfUsSensor
	 *            SensorPort of the ultrasonic sensor
	 */
	public LineFollower(SensorPort portOfLightSensor, SensorPort portOfUsSensor) {
		init(portOfLightSensor, portOfUsSensor);
		deltaSpeed = 0;
	}

	/**
	 * Constructs a line follower including driving speed alteration
	 * 
	 * @param portOfLightSensor
	 *            SensorPort of the light sensor
	 * @param portOfUsSensor
	 *            SensorPort of the ultrasonic sensor
	 * @param deltaSpeed
	 *            alteration of driving speed (negative = slower, postive =
	 *            faster)
	 */
	public LineFollower(SensorPort portOfLightSensor,
			SensorPort portOfUsSensor, int deltaSpeed) {
		init(portOfLightSensor, portOfUsSensor);
		this.deltaSpeed = deltaSpeed;
	}

	private void init(SensorPort portOfLightSensor, SensorPort portOfUsSensor) {
		setSpeedBackAt = -1;
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
		lineFinished = false;

		findLineStart();

		new Thread(lightSweeper).start();
		track.setSpeed(MOVING_SPEED + deltaSpeed);

		while (running && !lineFinished) {
			// check if robot is on the line
			if (lightSweeper.isLine()) {
				if (!track.motorsMoving())
					track.forward();

				if (setSpeedBackAt > 0
						&& setSpeedBackAt < System.currentTimeMillis())
					track.equalSpeed();
			} else { // search line if robot is off course
				track.stop();
				lightSweeper.setMoving(false);
				// search track with the sensor arm
				if (searchTrack()) {
					lightSweeper.setMoving(true);
				} else { // if no line is found, check if there are walls left
							// and right (end of second level)
					if (checkWalls()) {
						sensorArm.turnToCenter();
						lineFinished = true;
					} else { // else do a fallback search
						fallBack();
					}

				}
				if (!lineFinished) {
					track.setSpeed(MOVING_SPEED + deltaSpeed);
				}
			}
		}

		// drive straight to the barcode
		getToBarcode();

		running = false;
	}

	protected void fallBack() {
		// fallbackSearch (wall or Line)
		lineFinished = !fallbackSearch();
		if (!lineFinished)
			lightSweeper.setMoving(true);
	}

	protected void getToBarcode() {
		if (running) {
			track.setSpeed(1000);
			track.forward();
			while (!isLine()) {
				sleep(10);
			}
			track.stop();
		}
	}

	protected void findLineStart() {
		track.forward();
		track.setSpeed(MOVING_SPEED + deltaSpeed);
		sleep(1000);

		while (running && !isLine()) {
			sleep(10);
		}
	}

	@Override
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

		while (running && !found && angle <= 100) {
			if (checkRight(angle)) { // check right side
				sensorArm.turnToCenter();
				track.pivotAngleRight(angle);
				found = true;
			} else if (checkLeft(angle)) { // check left side
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
		track.setSpeed(MOVING_SPEED + deltaSpeed);
		return found;
	}

	/**
	 * checks whether the robot is on the line or not
	 * 
	 * @return is the robot on the line
	 */
	public boolean isLine() {
		return light.getLightValue() >= LINE_VALUE;
	}

	private boolean fallbackSearch() {
		boolean foundLine = false;
		track.setSpeed(ROTATING_SPEED);

		// checkLeft
		track.pivotAngleLeft(50);
		track.waitForMotors();
		if (checkLeft(90)) {
			foundLine = true;
			sensorArm.turnToCenter();
			track.pivotAngleLeft(90);
		}

		// checkRight
		if (!foundLine) {
			track.pivotAngleRight(100);
			track.waitForMotors();
			if (checkRight(90)) {
				foundLine = true;
				sensorArm.turnToCenter();
				track.pivotAngleRight(90);
			}

		}

		if (!foundLine) {
			track.pivotAngleLeft(50);
			track.waitForMotors();
		}

		while (foundLine && running && track.motorsMoving()) {
			if (isLine()) {
				track.stop();
			}
		}
		sensorArm.turnToCenter();

		track.setSpeed(MOVING_SPEED + deltaSpeed);
		return foundLine;
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

	/**
	 * gear left while driving
	 */
	public void gearLeft() {
		track.manipulateRight(MANIPULATION_SPEED);
		setSpeedBackAt = System.currentTimeMillis() + MANIPULATION_TIME;
	}

	/**
	 * gear right while driving
	 */
	public void gearRight() {
		track.manipulateLeft(MANIPULATION_SPEED);
		setSpeedBackAt = System.currentTimeMillis() + MANIPULATION_TIME;
	}

	private class LightSweeper implements Runnable {

		private SensorArm arm;
		private boolean moving;
		private boolean running;
		boolean moveLeft;
		private LineFollower follower;
		private boolean measurements[];
		private int head;

		public LightSweeper(SensorArm arm, LineFollower follower) {
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
						boolean isOnLine = follower.isLine();
						push(isOnLine);
						if (isOnLine)
							if (pos > 4) {
								follower.gearRight();
							} else if (pos < 4) {
								follower.gearLeft();
							}
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
