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

	public LineFolower(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);

		track = new TrackSuspension();
		track.setSpeed(MOVING_SPEED);
		sensorArm = new SensorArm();
		sensorArm.setSpeed(200);
	}

	@Override
	public void run() {
		running = true;

		while (running) {
			if (isLine()) {
				track.forward();
			} else {
				track.stop();
				searchTrack();
			}
			sleep(10);
		}
	}

	public void halt() {
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

	private boolean isLine() {
		return light.getLightValue() >= LINE_VALUE;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
