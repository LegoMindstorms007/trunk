package Test;

import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Start implements Runnable {
	private UltrasoundSensor us;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSensor light;
	private static final int LINE = 35;
	public Start() {
		us = new UltrasoundSensor(SensorPort.S3);
		light = new LightSensor(SensorPort.S4);
		arm = new SensorArm();
		track = new TrackSuspension();
		track.setSpeed(1000);
	}
	@Override
	public void run() {
		while(us.getMeasurment() >  UltrasoundSensor.MAX_DISTANCE) {
			if(!track.motorsMoving()) {
				track.forward();
			}
		}
		track.stop();
		track.pivotAngleLeft(90);
		track.waitForMotors();
		while(us.getMeasurment() >  UltrasoundSensor.MAX_DISTANCE) {
			if(!track.motorsMoving()) {
				track.forward();
			}
		}
		track.stop();
		track.pivotAngleLeft(90);
		track.waitForMotors();
		while(us.getMeasurment() >  UltrasoundSensor.MAX_DISTANCE) {
			if(!track.motorsMoving()) {
				track.forward();
			}
		}
		track.stop();
		track.pivotAngleRight(90);
		track.waitForMotors();
		while(light.getLightValue() < LINE)  {
			if(!track.motorsMoving()) {
				track.forward();
			}
		}
		track.stop();
	}
	
}
