package Test;

import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;

public class Start implements Runnable {
	private UltrasoundSensor us;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSensor light;
	private static final int LINE = 35;
	private static final int DISTANCETOWALL = 15;
	private boolean running;
	public Start() {
		us = new UltrasoundSensor(SensorPort.S3);
		light = new LightSensor(SensorPort.S4);
		arm = new SensorArm();
		track = new TrackSuspension();
		track.setSpeed(2000);
		running = true;
	}
	@Override
	public void run() {
	 while(running) {
		arm.turnToPosition(SensorArm.MAXLEFT);
		driveNextToLeftWall();
		track.stop();
		track.pivotAngleLeft(90);
		track.waitForMotors();
		arm.turnToCenter();
		while(us.getMeasurment() >=  DISTANCETOWALL) {
			if(!track.motorsMoving()) {
				track.forward();
			}
		}
		track.stop();
		track.pivotAngleLeft(90);
		track.waitForMotors();
		arm.turnToPosition(SensorArm.MAXRIGHT);
		driveNextToRightWall();
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
	public void halt() {
		running = false;
	}
	
	private void driveNextToLeftWall() {
		boolean leftWallFound = true;
		while(leftWallFound) {
			if(!track.motorsMoving()) {
				track.forward();
			}
			track.setSpeed(2000);
			int measure = us.getMeasurment();
			RConsole.println(String.valueOf(measure));
			if(measure < 20) {
				track.setSpeedRight(100);
			}
			if(measure > 25 && measure < UltrasoundSensor.MAX_DISTANCE) {
				track.setSpeedLeft(100);
			}
			if(measure == UltrasoundSensor.MAX_DISTANCE) {
				leftWallFound = false;
			}
			sleep(200);
		}
		}
		private void driveNextToRightWall() {
			boolean rightWallFound = true;
			while(!rightWallFound) {
			if(!track.motorsMoving()) {
				track.forward();
			}
			track.setSpeed(2000);
			int measure = us.getMeasurment();
			RConsole.println(String.valueOf(measure));
			if(measure < 20) {
				track.setSpeedLeft(100);
			}
			if(measure > 25 && measure < UltrasoundSensor.MAX_DISTANCE) {
				track.setSpeedRight(100);
			}
			if(measure == UltrasoundSensor.MAX_DISTANCE) {
				rightWallFound = false;
			}
			sleep(200);
		  }
		}
		private static void sleep(int millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}
