package Sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import Test.Demo;

/**
 * class representing the front bump sensor
 * 
 * @author Dominik Muth
 * 
 */
public class BumpSensor {

	private TouchSensor leftSensor;
	private TouchSensor rightSensor;
	private static BumpSensor instance;

	/**
	 * 
	 * @param leftSensorPort
	 *            port of left bump sensor
	 * @param rightSensorPort
	 *            port of right bump sensor
	 */
	private BumpSensor(SensorPort leftSensorPort, SensorPort rightSensorPort) {
		leftSensor = new TouchSensor(leftSensorPort);
		rightSensor = new TouchSensor(rightSensorPort);
	}

	public static BumpSensor getInstanceOf() {
		if (instance == null) {
			instance = new BumpSensor(Demo.BUMP_LEFT, Demo.BUMP_RIGHT);
		}
		return instance;
	}

	/**
	 * 
	 * @return if bump sensor on the right is pressed
	 */
	public boolean touchedRight() {
		return rightSensor.isPressed();
	}

	/**
	 * 
	 * @return if bump sensor on the left is pressed
	 */
	public boolean touchedLeft() {
		return leftSensor.isPressed();
	}

	/**
	 * 
	 * @return if both sensors are pressed
	 */
	public boolean touchedFront() {
		return touchedLeft() && touchedRight();
	}

	/**
	 * 
	 * @return true if any sensor is bumped
	 */
	public boolean touchedAny() {
		return touchedLeft() || touchedRight();
	}
}
