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
	final int LEFTPOSITION = 0;
	final int RIGHTPOSITION = 0;
	final int CENTERPOSITION = 0;
	FeatureDetector sensor;

	public UltrasoundArm(SensorPort portOfSensor) {
		motor = Motor.C;
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S3);
		sensor = new RangeFeatureDetector(us, MAX_DISTANCE, PERIOD);
	}

	public int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;

		return fdscan != null ? (int) fdscan.getRange() : MAX_DISTANCE;
	}

	public void turnToLeft() {
		motor.rotateTo(LEFTPOSITION);
	}

	public void TurnToRight() {
		motor.rotateTo(RIGHTPOSITION);
	}

	public void center() {
		motor.rotateTo(CENTERPOSITION);
	}
}
