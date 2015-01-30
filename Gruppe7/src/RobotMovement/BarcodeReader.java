package RobotMovement;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class BarcodeReader {

	private static final int LINE_VALUE = 35;
	private static final int AVG_NOLINE_TIME = 250;
	LightSensor light;
	SensorArm arm;
	TrackSuspension track;
	long lastLine;

	public BarcodeReader(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);
		arm = new SensorArm();
		track = new TrackSuspension();
	}

	public int readBarcode() {

		track.setSpeed(500);
		int code = 0;
		boolean lineValue = false;

		arm.turnToCenter();
		track.backward();
		while (!isLine()) {
			sleep(10);
		}
		track.forward();
		lastLine = System.currentTimeMillis();
		while (track.motorsMoving()) {
			if (System.currentTimeMillis() - lastLine > 2 * AVG_NOLINE_TIME)
				track.stop();

			if (lineValue != isLine()) {
				lineValue = !lineValue;
				if (lineValue) {
					lastLine = System.currentTimeMillis();
					code++;
				}
				sleep(100);
			}

		}

		return code;
	}

	public void alignOnBarcode() {
		track.backward();
		while (!isLine()) {
			sleep(10);
		}
		track.forward(50);
		track.stop();

		int angleLeft = -90;
		int angleRight = 90;

		arm.setSpeed(100);
		arm.turnToCenter();

		arm.turnToPosition(100, true);
		while (arm.isMoving()) {
			if (isLine()) {
				angleLeft = arm.getArmPosition();
				arm.stop();
			}
		}
		arm.turnToCenter();

		arm.turnToPosition(-100, true);
		while (arm.isMoving()) {
			if (isLine()) {
				angleRight = arm.getArmPosition();
				arm.stop();
			}
		}
		arm.turnToPosition(0, true);

		int angleDiv = (angleLeft - angleRight) / 2;
		if (angleDiv < 0)
			track.pivotAngleRight(-angleDiv);
		else
			track.pivotAngleLeft(angleDiv);
		track.forward(50);
	}

	public boolean isLine() {
		return light.getLightValue() >= LINE_VALUE;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
