package Test;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.TrackSuspension;

public class LineFolower implements Runnable {

	private static final int LINE_VALUE = 35;
	LightSensor light;
	TrackSuspension track;

	public LineFolower(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);

		track = new TrackSuspension();
	}

	@Override
	public void run() {

		while (true) {
			if (light.getLightValue() >= LINE_VALUE) {
				track.forward();
			} else {
				track.stop();
				searchTrack();
			}
		}
	}

	private void searchTrack() {
		int angle = 5;
		int i = 0;
		while (light.getLightValue() < LINE_VALUE) {
			if (i == 0) {
				track.pivotAngleLeft(angle);
			} else {
				track.pivotAngleRight(angle);
			}
			i++;
			i %= 2;
			angle += 5;
			track.waitForMotors();
		}
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
