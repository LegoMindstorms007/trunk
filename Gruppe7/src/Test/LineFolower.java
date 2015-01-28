package Test;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import RobotMovement.TrackSuspension;

public class LineFolower implements Runnable {

	private static final int LINE_VALUE = 35;
	private static final int MOVING_SPEED = 500;
	private static final int ROTATING_SPEED = 250;
	LightSensor light;
	TrackSuspension track;
	private boolean running;

	public LineFolower(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);

		track = new TrackSuspension();
		track.setSpeed(MOVING_SPEED);
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
			sleep(5);
		}
	}

	public void halt() {
		running = false;
	}

	private boolean searchTrack() {
		boolean found = false;
		track.setSpeed(ROTATING_SPEED);

		Motor.C.setSpeed(200);
		int angle = 20;

		while (!found && angle <= 100) {
			if (checkRight(angle)) {
				Motor.C.rotateTo(0);
				track.pivotAngleRight(angle);
				found = true;
			} else if (checkLeft(angle)) {
				Motor.C.rotateTo(0);
				track.pivotAngleLeft(angle);
				found = true;
			}
			angle += 20;
		}

		Motor.C.rotateTo(0);

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
		Motor.C.rotateTo(angle, true);
		while (Motor.C.getPosition() <= 0)
			;
		while (running && !found && Motor.C.isMoving()) {
			if (isLine()) {
				found = true;
			}
		}
		return found;
	}

	private boolean checkRight(int angle) {
		boolean found = false;
		Motor.C.rotateTo(-angle, true);
		while (Motor.C.getPosition() >= 0)
			;
		while (running && !found && Motor.C.isMoving()) {
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
