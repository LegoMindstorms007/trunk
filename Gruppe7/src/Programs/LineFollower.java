package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.LineAligner;
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
	protected static final int MOVING_SPEED = 600;
	protected static final int ROTATING_SPEED = 350;
	protected static final int ARM_SPEED = 350;
	protected static final int BUFFERSIZE = 40;
	protected LightSensor light;
	protected TrackSuspension track;
	protected boolean running;
	protected SensorArm sensorArm;
	protected UltrasoundSensor usSensor;
	protected LightSweeper lightSweeper;
	protected int deltaSpeed;
	protected boolean lineFinished;
	protected boolean lastLeft = false;
	protected LineAligner lineAligner;

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
		light = new LightSensor(portOfLightSensor);
		usSensor = UltrasoundSensor.getInstanceOf();

		track = TrackSuspension.getInstance();
		sensorArm = new SensorArm();
		sensorArm.setSpeed(ARM_SPEED);
		lightSweeper = new LightSweeper(sensorArm, this, BUFFERSIZE);
		lineAligner = new LineAligner(portOfLightSensor);
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

			} else { // search line if robot is off course
				track.stop();
				lightSweeper.setMoving(false);
				// search track with the sensor arm
				if (searchTrack()) {
					lightSweeper.setMoving(true);
				} else {
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
			sleep(50);
		}

		if (running)
			alignOnEnd();

		// drive straight to the barcode
		if (running)
			getToBarcode();

		running = false;
	}

	protected void fallBack() {
		// fallbackSearch (wall or Line)
		lineFinished = !fallbackSearch();
		if (!lineFinished)
			lightSweeper.setMoving(true);
	}

	protected void alignOnEnd() {
		int dist = 75;
		track.backward(dist);
		if (!searchTrack()) {
			track.backward(25);
			dist += 25;
		}
		sensorArm.turnToCenter();
		track.forward(dist);
		sensorArm.waitForArm();
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
		sleep(2000);

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

	protected boolean searchTrack() {
		boolean found = false;
		track.setSpeed(ROTATING_SPEED);
		int angle = 50;

		// search small angle where track was last seen
		if (!lastLeft && lightSweeper.wasLastLeft() && checkLeft(25)) {
			track.pivotAngleLeft(15);
			found = true;
			lastLeft = true;
		} else if (lastLeft && !lightSweeper.wasLastLeft() && checkRight(25)) {
			track.pivotAngleRight(15);
			found = true;
			lastLeft = false;
		}

		while (running && !found && angle <= 90) {
			if (lastLeft && checkLeft(angle)) { // check left side
				track.pivotAngleLeft(angle);
				found = true;
				lastLeft = true;
			} else if (checkRight(angle)) { // check right side
				track.pivotAngleRight(angle);
				found = true;
				lastLeft = false;
			} else if (!lastLeft && checkLeft(angle)) { // check left side
				track.pivotAngleLeft(angle);
				found = true;
				lastLeft = true;
			}
			angle += 40;
		}
		if (found) {
			sensorArm.turnToCenter();
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

	protected boolean fallbackSearch() {
		boolean foundLine = false;
		track.setSpeed(ROTATING_SPEED);
		sensorArm.turnToPosition(20, true);
		track.backward(25);
		sensorArm.waitForArm();

		// checkLeft
		if (checkLeft(90)) {
			foundLine = true;
			track.pivotAngleLeft(90);
		}

		sensorArm.turnToPosition(-20, true);
		// checkRight
		if (!foundLine) {
			track.waitForMotors();
			if (checkRight(90)) {
				foundLine = true;
				track.pivotAngleRight(90);
			}

		}

		while (foundLine && running && track.motorsMoving()) {
			if (isLine()) {
				track.stop();
			}
		}
		sensorArm.turnToCenter(true);
		track.forward(25);
		sensorArm.waitForArm();

		track.setSpeed(MOVING_SPEED + deltaSpeed);
		return foundLine;
	}

	protected boolean checkLeft(int angle) {
		boolean found = false;
		sensorArm.turnToPosition(angle, true);
		while (sensorArm.getArmPosition() <= 0)
			;
		while (running && !found && sensorArm.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		sensorArm.stop();
		return found;
	}

	protected boolean checkRight(int angle) {
		boolean found = false;
		sensorArm.turnToPosition(-angle, true);
		while (running && sensorArm.getArmPosition() >= 0)
			;
		while (running && !found && sensorArm.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		sensorArm.stop();
		return found;
	}

	protected boolean checkWalls() {
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

	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected class LightSweeper implements Runnable {

		private SensorArm arm;
		private boolean moving;
		private boolean running;
		boolean moveLeft;
		private LineFollower follower;
		private boolean measurements[];
		private int head;
		private boolean lastLeft;

		public LightSweeper(SensorArm arm, LineFollower follower, int bufferSize) {
			measurements = new boolean[bufferSize];
			this.follower = follower;
			this.arm = arm;
			moving = true;
			moveLeft = true;
			head = 0;
			push(follower.isLine());
			lastLeft = true;
		}

		@Override
		public void run() {
			running = true;
			while (running) {
				while (running && moving && isLine()) {
					if (moveLeft) {
						arm.turnToPosition(9, true);
					} else {
						arm.turnToPosition(-9, true);
					}
					while (running && moving && arm.isMoving()) {
						boolean isLine = follower.isLine();
						int angle = arm.getArmPosition();
						push(isLine);
						if (isLine) {
							if (angle > 0)
								lastLeft = true;
							else
								lastLeft = false;
						}
						sleep(10);
					}
					moveLeft = !moveLeft;
				}
			}
		}

		public void setMoving(boolean shouldMove) {
			moving = shouldMove;
			if (shouldMove)
				push(true);
			else {
				sensorArm.stop();
				arm.turnToCenter();
			}
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

		public boolean wasLastLeft() {
			return lastLeft;
		}

		private void push(boolean value) {
			measurements[head++] = value; // insert into Buffer and move head
			head %= measurements.length; // move head to start of array if index
											// is out of bounds
		}
	}
}
