package RobotMovement;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class UltrasoundArm {

	private static int MAX_DISTANCE = 50;
	private static int PERIOD = 50;
	private NXTRegulatedMotor motor;
	final int LEFTPOSITION = -65;
	final int RIGHTPOSITION = 65;
	final int CENTERPOSITION = 0;
	FeatureDetector sensor;

	private int measurements[];
	private long times[];

	public UltrasoundArm(SensorPort portOfSensor) {
		motor = Motor.C;
		UltrasonicSensor us = new UltrasonicSensor(portOfSensor);
		sensor = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
		times = new long[3];
		measurements = new int[3];
	}

	public int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;

		return fdscan != null ? (int) fdscan.getRange() : MAX_DISTANCE;
	}

	public void turnToLeft() {
		motor.rotateTo(LEFTPOSITION);
		measurements[0] = getMeasurment();
		times[0] = System.currentTimeMillis();
	}

	public void TurnToRight() {
		motor.rotateTo(RIGHTPOSITION);
		measurements[2] = getMeasurment();
		times[2] = System.currentTimeMillis();
	}

	public void center() {
		motor.rotateTo(CENTERPOSITION);
		measurements[1] = getMeasurment();
		times[1] = System.currentTimeMillis();
	}

	public int getRightMeasurement() {
		return measurements[0];
	}

	public int getLeftMeasurement() {
		return measurements[2];
	}

	public int getCenterMeasurement() {
		return measurements[1];
	}

	public int getAgeLeft() {
		return (int) (System.currentTimeMillis() - times[0]);
	}

	public int getAgeRight() {
		return (int) (System.currentTimeMillis() - times[2]);
	}

	public int getAgeCenter() {
		return (int) (System.currentTimeMillis() - times[1]);
	}
}
