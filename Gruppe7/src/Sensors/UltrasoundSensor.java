package Sensors;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.RangeFeatureDetector;
import Test.Demo;

public class UltrasoundSensor {

	public static int MAX_DISTANCE = 255;
	private static int PERIOD = 10;
	private static int WALL_VALUE = 20;
	FeatureDetector sensor;
	private static UltrasoundSensor instance;

	private UltrasoundSensor(SensorPort portOfSensor) {
		UltrasonicSensor us = new UltrasonicSensor(portOfSensor);
		sensor = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
	}

	public static UltrasoundSensor getInstanceOf() {
		if (instance == null) {
			instance = new UltrasoundSensor(Demo.ULTRA_SOUND);
		}
		return instance;
	}

	public int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;
		return fdscan != null ? (int) fdscan.getRange() : MAX_DISTANCE;
	}

	public int getAverageMeasurement(int numberMeasurements) {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;
		int[] scanData = new int[numberMeasurements];
		scanData[0] = fdscan != null ? (int) fdscan.getRange() : MAX_DISTANCE;
		for (int i = 1; i < numberMeasurements; i++) {
			scanData[i] = fdscan != null ? (int) fdscan.getRange()
					: MAX_DISTANCE;
		}
		int result = scanData[0];
		int max = scanData[0];
		int min = scanData[0];
		for (int i = 1; i < numberMeasurements; i++) {
			int current = scanData[i];
			if (current > max) {
				max = current;
			}
			if (current < min) {
				min = current;
			}
			result += current;
		}
		result -= max;
		result -= min;
		return (int) (result / (float) (numberMeasurements - 2));
	}

	public boolean isWall() {
		return getMeasurment() < WALL_VALUE;
	}
}
