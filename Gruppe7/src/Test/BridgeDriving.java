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
	private static final int MOVING_SPEED = 500;
	private static final int ROTATING_SPEED = 200;
	private static final int NOGROUNDSOUND = 30;
	private static final int NOGROUND = 25;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSensor light;
	private boolean running;
	private Compass compass;
	public BridgeDriving() {
		track = new TrackSuspension();
		arm = new SensorArm();
		light = new LightSensor(SensorPort.S4);
		compass = new Compass();
		arm.setSpeed(300);
		track.setSpeed(200);
	}
	@Override
	public void run() {
		running = true;
		while(running){
			arm.tilt();
			track.forward();
			if(light.getLightValue() <= NOGROUND) {
				arm.stop();
				track.stop();
				int position = arm.getArmPosition();
				if(position <= 0) {
					track.backward(20);
					track.pivotAngleLeft(30);
					track.waitForMotors();
				} else {
					track.backward(20);
					track.pivotAngleRight(30);
					track.waitForMotors();
				}
			}
			
			/*
			Situation current = checkForGround();
			switch(current) {
			case OK:
				track.forward(30);
			break;
			case ABYSSLEFT:
				track.backward(30);
				track.pivotAngleRight(60);
				compass.turnRight(60);
			break;
			case ABYSSRIGHT:
				track.backward(30);
				track.pivotAngleLeft(60);
				compass.turnLeft(60);
			break;
			case ABYSSINFRONT:
				track.backward(90);
				moveForward();
			break;
			case ABYSSALLAROUND:
				track.backward(180);
				moveForward();
			break;
			case ABYSSLEFTINFRONT:
				track.backward(90);
				track.pivotAngleRight(60);
				compass.turnRight(60);
			break;
			case ABYSSRIGHINFRONT:
				track.backward(90);
				track.pivotAngleLeft(60);
				compass.turnRight(60);
			break;
			}
			RConsole.println("Aktuelle Richtung: " + compass.getDirection());
			track.waitForMotors(); */
		}
	}
	
	public void halt() {
		running = false;
	}

	private Situation checkForGround() {
		int[] measures = new int[3];
		
		RConsole.println("Links: " + measures[0] + "  Rechts: "+ measures[2]);
		//Test LEFT
		if(abyssCenter(measures)) {
			if(abyssLeft(measures)) {
				if(abyssRight(measures)) {
					return Situation.ABYSSALLAROUND;
				} 
				else {
					return Situation.ABYSSLEFTINFRONT;
				}
			} else if(abyssRight(measures)) {
				return Situation.ABYSSRIGHINFRONT;
			}
		}
		if(abyssLeft(measures)){
			return Situation.ABYSSLEFT;
		}
		if(abyssRight(measures)) {
			return Situation.ABYSSRIGHT;
		}
		return Situation.OK;
	}
	
	private boolean abyssLeft(int[] measures) {
		return measures[Directions.LEFT.ordinal()] >= NOGROUNDSOUND;
	}
	
	private boolean abyssCenter(int[] measures) {
		return light.getLightValue() <= NOGROUND;
	}
	
	private boolean abyssRight(int[] measures) {
		return measures[Directions.RIGHT.ordinal()] >= NOGROUNDSOUND;
	}
	public enum Situation {
		OK, ABYSSLEFT, ABYSSRIGHT, ABYSSINFRONT, ABYSSLEFTINFRONT, ABYSSRIGHINFRONT, ABYSSALLAROUND
	}
	
	
	
	private void moveForward() {
		int currentDirection = compass.getDirection();
		if((currentDirection > 0 && currentDirection < 180)) {
			int deviation = 180 - currentDirection;
			track.pivotAngleLeft(deviation);
			compass.turnLeft(deviation);
		} else if(currentDirection < 360 && currentDirection > 180) {
			int deviation = Math.abs(180 - currentDirection);
			track.pivotAngleRight(deviation);
		} 
		if(currentDirection == 0) {
			track.pivotAngleLeft(180);
			compass.turnLeft(180);
		}
	}
}
