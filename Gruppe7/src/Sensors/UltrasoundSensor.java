package Sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class UltrasoundSensor {

	public static int MAX_DISTANCE = 255;
	private static int PERIOD = 50;
	private static int WALL_VALUE = 30;
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

	public boolean isWall() {
		return getMeasurment() < WALL_VALUE;
	}
}