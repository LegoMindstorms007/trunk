package Test;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.TrackSuspension;

public class LineFolower implements Runnable {

	private static final int LINE_VALUE = 35;
	private static final int MOVING_SPEED = 500;
	private static final int ROTATING_SPEED = 200;
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
			if (light.getLightValue() >= LINE_VALUE) {
				track.forward();
			} else {
				track.stop();
				searchTrack();
			}
		}
	}

	public void halt() {
		running = false;
	}

	private void searchTrack() {
		track.setSpeed(ROTATING_SPEED);
		int angle = 20;
		int i = 0;
		boolean found = false;
		while (running && !found) {
			if (i == 1) {
				track.pivotAngleLeft(angle);
			} else {
				track.pivotAngleRight(angle);
			}

			while (track.motorsMoving()) {
				if (isLine()) {
					track.stop();
					found = true;
				}
				sleep(5);
			}

			found = isLine();
			i++;
			i %= 2;
			angle += 20;
		}
		track.setSpeed(MOVING_SPEED);
	}

	private boolean isLine() {
		return light.getLightValue() >= LINE_VALUE;
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
