package Programs;

import lejos.nxt.Battery;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.Light;
import Sensors.UltrasoundSensor;

public class StartNeu implements Program {
	private static final int MOVINGSPEED = 2000;
	private static final int TURNINGSPEED = (int)  (20 * Battery.getVoltage());
	protected final static int backward = 150;
	private boolean running;
	private boolean bumped;
	private boolean linefound;
	private UltrasoundSensor us;
	private TrackSuspension tracks;
	private SensorArm arm;
	private LightSensor light;
	protected Bumper bump;

	public StartNeu() {
		running = true;
		us = UltrasoundSensor.getInstanceOf();
		light = Light.getInstanceOf();
		tracks = TrackSuspension.getInstance();
		arm = SensorArm.getInstance();
		bumped = false;
		linefound = false;
		bump = new Bumper();
		new Thread(bump).start();
	}

	@Override
	public void run() {
		tracks.setSpeed(MOVINGSPEED);
		findLeftWall();
		driveAlongLeftWall();
		findRightWall();
		driveAlongRightWall();
		while (!bumped && running) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward - 70);
		tracks.stop();
		tracks.pivotAngleRight(95);
		tracks.waitForMotors();
		arm.turnToCenter();
		adjustToWallLeft();
		arm.turnToCenter();
		while (!linefound && running) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
	}

	private void driveAlongRightWall() {
		boolean moving = true;
		tracks.setSpeed(MOVINGSPEED);
		while (running && moving) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
			sleep(50);
			Orientation orientation = CalculateAngle();
			LCD.clear();
			LCD.drawString(orientation.toString(), 0, 1);
			if (orientation == orientation.STOP) {
				moving = false;
				tracks.stop();
			}
			if (orientation == orientation.TONEAR) {
				tracks.stop();
				tracks.setSpeed(MOVINGSPEED);
				tracks.pivotAngleLeft(45);
				tracks.waitForMotors();
				tracks.forward(150);
				tracks.pivotAngleRight(50);
				tracks.waitForMotors();
				arm.turnToCenter();
				adjustToWallRight();
			}
			if (orientation == Orientation.TOLEFT) {
				tracks.setSpeedLeft(MOVINGSPEED);
				tracks.setSpeedRight(TURNINGSPEED);
			} else if (orientation == Orientation.TORIGHT) {
				tracks.setSpeedRight(MOVINGSPEED);
				tracks.setSpeedLeft(TURNINGSPEED);
			} else {
				tracks.setSpeed(MOVINGSPEED);
			}
		}

	}

	private void driveAlongLeftWall() {
		boolean moving = true;
		tracks.setSpeed(MOVINGSPEED);
		while (running && moving) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
			sleep(50);
			Orientation orientation = CalculateAngle();
			LCD.clear();
			LCD.drawString(orientation.toString(), 0, 1);
			if (orientation == orientation.STOP) {
				moving = false;
				tracks.stop();
			}
			if (orientation == orientation.TONEAR) {
				tracks.stop();
				tracks.setSpeed(MOVINGSPEED);
				tracks.pivotAngleRight(45);
				tracks.waitForMotors();
				tracks.forward(150);
				tracks.pivotAngleLeft(50);
				tracks.waitForMotors();
				arm.turnToCenter();
				adjustToWallLeft();
			}
			if (orientation == Orientation.TOLEFT) {
				tracks.setSpeedRight(MOVINGSPEED);
				tracks.setSpeedLeft(TURNINGSPEED);
			} else if (orientation == Orientation.TORIGHT) {
				tracks.setSpeedLeft(MOVINGSPEED);
				tracks.setSpeedRight(TURNINGSPEED);
			} else {
				tracks.setSpeed(MOVINGSPEED);
			}
		}

	}

	private void findLeftWall() {
		arm.turnToPosition(SensorArm.MAXRIGHT);
		tracks.pivotAngleLeft(95);
		tracks.waitForMotors();
		while (running && !bumped) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.pivotAngleRight(95);
		tracks.waitForMotors();
		arm.turnToCenter();
		adjustToWallLeft();
	}

	private void findRightWall() {
		tracks.forward(100);
		arm.turnToPosition(SensorArm.MAXRIGHT);
		tracks.pivotAngleLeft(95);
		tracks.waitForMotors();
		while (running && !bumped) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.stop();
		tracks.pivotAngleLeft(95);
		tracks.waitForMotors();
		arm.turnToCenter();
		adjustToWallRight();
	}

	@Override
	public void halt() {
		running = false;

	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private Orientation CalculateAngle() {
		int firstMeasure = us.getAverageMeasurement(5);
		sleep(50);
		int secondMeasure = us.getAverageMeasurement(5);
		int sum = firstMeasure + secondMeasure;
		if (firstMeasure + secondMeasure > 160) {
			return Orientation.STOP;
		}
		if (sum < 25) {
			return Orientation.TONEAR;
		}
		int position = firstMeasure - secondMeasure;
		if (position > 0) {
			return Orientation.TORIGHT;
		} else if (position < 0) {
			return Orientation.TOLEFT;
		} else {
			return Orientation.OK;
		}

	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected enum Orientation {
		TOLEFT, TORIGHT, OK, STOP, TONEAR;
	}

	protected class Bumper implements Program {
		BumpSensor bumper;
		protected boolean running;

		public Bumper() {
			bumper = BumpSensor.getInstanceOf();
			running = true;
		}

		@Override
		public void run() {
			while (running) {
				if (light.getLightValue() >= 40) {
					linefound();
				}
				while (bumper.touchedFront()) {
					bumped();
					sleep(50);
				}
				released();
				sleep(50);
			}

		}

		@Override
		public void halt() {
			running = false;

		}

		@Override
		public boolean isRunning() {
			return running;
		}

	}

	protected void bumped() {
		bumped = true;
	}

	protected void released() {
		bumped = false;
	}

	private void linefound() {
		linefound = true;
		halt();
	}

	private void adjustToWallRight() {
		int min = us.getMeasurment();
		int minPosition = 0;
		arm.turnToPosition(SensorArm.MAXRIGHT, true);
		while (arm.isMoving() && running && !linefound && !bumped) {
			int value = us.getMeasurment();
			int currentPosition = arm.getArmPosition();
			if (value < min) {
				min = value;
				minPosition = currentPosition;
			}
		}
		tracks.pivotAngleLeft((int) ((90 - Math.abs(minPosition) * 1.05)));
		tracks.waitForMotors();
	}

	private void adjustToWallLeft() {
		int min = us.getMeasurment();
		int minPosition = 0;
		arm.turnToPosition(SensorArm.MAXLEFT, true);
		while (arm.isMoving() && running && !linefound && !bumped) {
			int value = us.getMeasurment();
			int currentPosition = arm.getArmPosition();
			if (value < min) {
				min = value;
				minPosition = currentPosition;
			}
		}
		tracks.pivotAngleRight((int) ((90 - Math.abs(minPosition) * 1.05)));
		tracks.waitForMotors();
	}

}
