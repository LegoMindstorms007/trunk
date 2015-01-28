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
	final int LEFTPOSITION = 60;
	final int RIGHTPOSITION = -60;
	final int CENTERPOSITION = 0;
	FeatureDetector sensor;

	private int measurements[];
	private long times[];

	public UltrasoundArm(SensorPort portOfSensor) {
		motor = Motor.C;
		UltrasonicSensor us = new UltrasonicSensor(portOfSensor);
		motor.setSpeed(90);
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
		sleep(100);
		measurements[Directions.LEFT.ordinal()] = getMeasurment();
		times[Directions.LEFT.ordinal()] = System.currentTimeMillis();
	}

	public void turnToRight() {
		motor.rotateTo(RIGHTPOSITION);
		sleep(100);
		measurements[Directions.RIGHT.ordinal()] = getMeasurment();
		times[Directions.RIGHT.ordinal()] = System.currentTimeMillis();
	}

	public void center() {
		motor.rotateTo(CENTERPOSITION);
		measurements[Directions.CENTER.ordinal()] = getMeasurment();
		times[Directions.CENTER.ordinal()] = System.currentTimeMillis();
	}

	public int getRightMeasurement() {
		return measurements[Directions.LEFT.ordinal()];
	}

	public int getLeftMeasurement() {
		return measurements[Directions.RIGHT.ordinal()];
	}

	public int getCenterMeasurement() {
		return measurements[Directions.CENTER.ordinal()];
	}

	public int getAgeLeft() {
		return (int) (System.currentTimeMillis() - times[Directions.LEFT
				.ordinal()]);
	}

	public int getAgeRight() {
		return (int) (System.currentTimeMillis() - times[Directions.RIGHT
				.ordinal()]);
	}

	public int getAgeCenter() {
		return (int) (System.currentTimeMillis() - times[Directions.CENTER
				.ordinal()]);
	}

	public int[] fullMeasureMent() {
		turnToLeft();
		turnToRight();
		return measurements;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public enum Directions {
		LEFT, CENTER, RIGHT;
	}
}
