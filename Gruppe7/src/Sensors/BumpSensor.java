package Sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class BumpSensor {

	private TouchSensor leftSensor;
	private TouchSensor rightSensor;

	public BumpSensor(SensorPort leftSensorPort, SensorPort rightSensorPort) {
		leftSensor = new TouchSensor(leftSensorPort);
		rightSensor = new TouchSensor(rightSensorPort);
	}

	public boolean touchedRight() {
		return rightSensor.isPressed();
	}

	public boolean touchedLeft() {
		return leftSensor.isPressed();
	}

	public boolean touchedFront() {
		return touchedLeft() && touchedRight();
	}

	public boolean touchedAny() {
		return touchedLeft() || touchedRight();
	}
}
