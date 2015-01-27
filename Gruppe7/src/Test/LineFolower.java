package Test;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.TrackSuspension;

public class LineFolower implements Runnable {

	private static final int LINE_VALUE = 35;
	LightSensor light;
	TrackSuspension track;
	private boolean running;

	public LineFolower(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);

		track = new TrackSuspension();
		track.setSpeed(500);
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
		track.setSpeed(1000);
		int angle = 5;
		int i = 0;
		boolean found = false;
		while (!found) {
			if (i == 0) {
				if (angle < 10) {
					track.turnLeft(angle);
				} else {
					track.pivotAngleLeft(angle);
				}
			} else {
				if (angle < 10) {
					track.turnRight(angle);
				} else {
					track.pivotAngleRight(angle);
				}
			}
			while (track.motorsMoving()) {
				if (isLine()) {
					track.stop();
					found = true;
				}
				sleep(10);
			}
			found = isLine();
			i++;
			i %= 2;
			angle += 5;
		}
		track.setSpeed(100);
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
