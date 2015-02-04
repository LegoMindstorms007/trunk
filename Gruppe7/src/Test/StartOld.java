package Test;

import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class StartOld implements Runnable {
	private RangeFeatureDetector sensor;
	private static final int MAXDISTANCE = 80;
	private static final int LINE = 35;
	private static final int DISTANCETOWALL = 25;
	private static final int MAXSPEED = 2000;
	private static final int CAUTIOSSPEED = 100;
	private static final int ARMSPEED = 600;
	private int cautios;
	private int stop;
	private int border;
	private Turns currentTurn;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSweeper sweeper;
	private boolean running;
	public StartOld() {
		arm = new SensorArm();
		track = TrackSuspension.getInstance();
		running = true;
		sweeper = new LightSweeper(SensorPort.S4);
		track.setSpeed(MAXSPEED);
		currentTurn = Turns.FIRSTLEFT;
		cautios = 20;
		stop = 15;
		border = 30;

	}
	@Override
	public void run() {
		new Thread(sweeper).start();
		arm.setSpeed(ARMSPEED);
	 while(running) {
		 if(!track.motorsMoving()) {
			 track.forward();
		 }
		 int distance = getMeasurment();
		 RConsole.println("Distance: " +distance);
		 int position = arm.getArmPosition();
		 if(distance < border) {
			 if(position < 0) {
				track.setSpeedLeft(CAUTIOSSPEED + 200);
				sleep(100);
			 } else {
				track.setSpeedRight(CAUTIOSSPEED + 200);
				sleep(100);
			 }
		 }
		 if(distance < cautios) {
			 track.setSpeed(CAUTIOSSPEED);
		 } else {
			 track.setSpeed(MAXSPEED);
		 }
		 if(distance < stop) {
			track.stop();
			track.setSpeed(MAXSPEED);
			track.backward(150);
			track.setSpeed(CAUTIOSSPEED);
			position =  findDirectionWithMaxDistance();
			if(position < 0) {
				track.pivotAngleRight(-position);
				track.waitForMotors();
			} else if(position >0) {
				track.pivotAngleLeft(position);	
				track.waitForMotors();
			}
		 }
	 }
	 sweeper.halt();
	}
	public void halt() {
		running = false;
	}
		private static void sleep(int millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	public int findDirectionWithMaxDistance() {
		sweeper.stopSweeping();
		arm.setSpeed(200);
		int measurement = 0;
		int maxposition = 0;
		Turns nextTurn = Turns.FIRSTLEFT;
		switch(currentTurn) {
			case FIRSTLEFT:
				arm.turnToPosition(SensorArm.MAXLEFT);
				measurement = getMeasurment();
				maxposition = SensorArm.MAXLEFT;
				arm.turnToPosition(SensorArm.MAXRIGHT, true);
				nextTurn = Turns.SECONDLEFT;
			break;
			case SECONDLEFT:
				arm.turnToPosition(SensorArm.MAXLEFT);
				measurement = getMeasurment();
				maxposition = SensorArm.MAXLEFT;
				arm.turnToPosition(SensorArm.MAXRIGHT, true);
				nextTurn = Turns.RIGHT;
			break;
			case RIGHT:
				arm.turnToPosition(SensorArm.MAXRIGHT);
				measurement = getMeasurment();
				maxposition = SensorArm.MAXRIGHT;
				arm.turnToPosition(SensorArm.MAXLEFT, true);
				nextTurn =  Turns.FINISH;
			break;
			case FINISH:
				arm.turnToCenter();
				measurement = getMeasurment();
				maxposition = SensorArm.CENTER;
				arm.turnToPosition(SensorArm.MAXRIGHT);
				arm.turnToPosition(SensorArm.MAXLEFT, true);
				border = 10;
				cautios = 8;
				border = 6;
				nextTurn = Turns.FINISH;
			break;
		}
		if(!(measurement == UltrasoundSensor.MAX_DISTANCE)) {
		while(arm.isMoving()) {
			int newmeasurement = getMeasurment();
			int position = arm.getArmPosition();
			if(newmeasurement > measurement) {
				measurement = newmeasurement;
				maxposition = position;
				RConsole.println("Position: " + maxposition);
				RConsole.println("Value: " + measurement);
			}
			measurement = Math.max(measurement, getMeasurment());
		} 
		} else {
			currentTurn = nextTurn;
		}
		
		arm.setSpeed(ARMSPEED);
		sweeper.startSweeping();
		return maxposition;
	}
	
	private enum Turns {
		FIRSTLEFT, SECONDLEFT, RIGHT, FINISH;
	}
	
	public int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;

		return fdscan != null ? (int) fdscan.getRange() : MAXDISTANCE;
	}
}
