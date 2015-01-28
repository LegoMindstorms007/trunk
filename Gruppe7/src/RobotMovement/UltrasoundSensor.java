package RobotMovement;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class UltrasoundSensor {

	private static int MAX_DISTANCE = 50;
	private static int PERIOD = 50;
	FeatureDetector sensor;

	public UltrasoundSensor(SensorPort portOfSensor) {
		UltrasonicSensor us = new UltrasonicSensor(portOfSensor);
		sensor = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
	}

	public int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;

		return fdscan != null ? (int) fdscan.getRange() : MAX_DISTANCE;
	}
}
