package RobotMovement;

import lejos.nxt.LightSensor;
import Sensors.Light;

/**
 * Barcode reader class
 * 
 * @author Dominik Muth
 * 
 */
public class BarcodeReader {

	private static final int LINE_VALUE = 35;
	private static final int AVG_NOLINE_TIME = 250;
	LightSensor light;
	SensorArm arm;
	TrackSuspension track;
	long lastLine;
	long lastNoLine;

	/**
	 * 
	 * @param portOfLightSensor
	 *            SensorPort of light sensor
	 */
	public BarcodeReader() {
		light = Light.getInstanceOf();
		arm = SensorArm.getInstance();
		track = TrackSuspension.getInstance();
	}

	/**
	 * reads the barcode and stops at the end of it
	 * 
	 * @return value of the barcode (number of lines)
	 */
	public int readBarcode() {

		track.setSpeed(500);
		int code = 0;
		boolean lineValue = false;

		arm.turnToCenter();
		// drive back to first line
		track.backward();
		while (!isLine()) {
			sleep(10);
		}
		track.forward();
		lastLine = System.currentTimeMillis();
		lastNoLine = System.currentTimeMillis();
		while (track.motorsMoving()) {
			if (System.currentTimeMillis() - lastLine > 2 * AVG_NOLINE_TIME)
				track.stop();

			if (System.currentTimeMillis() - lastNoLine > 2 * AVG_NOLINE_TIME)
				track.stop();

			if (lineValue != isLine()) {
				lineValue = !lineValue;
				if (lineValue) {
					lastLine = System.currentTimeMillis();
					code++;
				} else {
					lastNoLine = System.currentTimeMillis();
				}
				sleep(100);
			}

		}
		track.backward();
		sleep(AVG_NOLINE_TIME);
		track.stop();

		return code;
	}

	private boolean isLine() {
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
