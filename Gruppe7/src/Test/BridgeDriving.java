package Test;

import java.util.ArrayList;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import RobotMovement.Compass;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundArm;
import RobotMovement.UltrasoundArm.Directions;

public class BridgeDriving implements Runnable {
	private static final int MOVING_SPEED = 225;
	private static final int SWEEPING_SPEED = 400;
	private static final int ROTATINGSPEED = 500;
	private static final int NOGROUND = 25;
	private static final int BLACKGROUND = 26;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSensor light;
	private boolean running;
	private LightSweeper sweeper;
	Last last;
	public BridgeDriving() {
		track = new TrackSuspension();
		arm = new SensorArm();
		light = new LightSensor(SensorPort.S4);
		arm.setSpeed(SWEEPING_SPEED);
		track.setSpeed(MOVING_SPEED);
		 sweeper = new LightSweeper();
	}
	@Override
	public void run() {
		running = true;
		boolean foundBridge = false;
		track.setSpeed(1000);
		//Find the Bridge
		while(!foundBridge) {
			if(!track.motorsMoving()) {
				track.forward();
			}
			if(light.getLightValue() >= BLACKGROUND) {
				foundBridge = true;
				track.forward(150);
			}
			sleep(50);
		}
		//Start the sweeping to prevent a Downfall
		track.setSpeed(MOVING_SPEED);
		Thread sweeping = new Thread(sweeper);
		sweeping.start();
		last = null;
		while(running){
			if(!track.motorsMoving()) {
			track.forward();
			}
			if(light.getLightValue() <= NOGROUND) {
				int position = arm.getArmPosition();
				track.stop();
				track.setSpeed(ROTATINGSPEED);
				if(position < 0) {
					turnLeft(20);
				//Problem if found Cliff direcetly in Front
				} else if(position == 0) {
					if(last == null) {
						turnLeft(10);
					} else if(last == Last.RIGHT) {
						turnRight(10);
					} else {
						turnLeft(10);
					}
				} else {
					turnRight(20);
				}
				track.setSpeed(MOVING_SPEED);
			}
		}
		sweeper.halt();
	}
	
	public void halt() {
		running = false;
	}
	public enum Last {
		LEFT, RIGHT;
	}
	
	private void turnLeft(int angle) {
		track.backward(10);
		track.pivotAngleLeft(angle);
		track.waitForMotors();
		last = Last.LEFT;
	}
	
	private void turnRight(int angle) {
		track.backward(10);
		track.pivotAngleRight(angle);
		track.waitForMotors();
		last = Last.RIGHT;
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
