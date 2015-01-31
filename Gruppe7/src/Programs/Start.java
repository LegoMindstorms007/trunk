package Programs;

import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundSensor;
import Sensors.BumpSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;

public class Start implements Program {
	private boolean running;
	UltrasoundSensor us;
	TrackSuspension tracks;
	private boolean bumped;
	private Bumper bump;
	SensorArm arm;
	private LightSensor light;
	private boolean linefound;
	final static int MOVINGSPEED = 2000;
	final static int TURNINGSPEED = 500;
	private final static int backward = 110;
	private final static int NEAREST = 17;
	private final static int TONEAREST = 8;
	private final static int FAREST = 20;
	private final static int TURNRIGHT = 50;
	public Start(SensorPort lightPort, SensorPort ultraSoundPort) {
		 us = new UltrasoundSensor(ultraSoundPort);
		 tracks = new TrackSuspension();
		 running = true;
		 bumped = false;
		 arm = new SensorArm();
		 bump = new Bumper();
		 light = new LightSensor(SensorPort.S4);
		 new Thread(bump).start();
		 linefound = false;
	}
	@Override
	public void run() {
		
		tracks.setSpeed(MOVINGSPEED);
		findRightWall();
		driveAlongRightWall();
	}

	@Override
	public void halt() {
		running = false;
		
	}

	private void findLeftWall() {
		arm.turnToPosition(SensorArm.MAXLEFT - 15);
		tracks.pivotAngleLeft(90);
		tracks.waitForMotors();
		while(running && !bumped) {
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
	}
	
	private void findRightWall() {
		arm.turnToPosition(SensorArm.MAXRIGHT - 15);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
		while(running && !bumped) {
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.pivotAngleLeft(90);
		tracks.waitForMotors();
	}

	private void driveAlongLeftWall() {
		int distance = 0;
		while(running) {
			if(bumped && !linefound) {
				tracks.stop();
				tracks.backward(backward);
				tracks.pivotAngleRight(90);
				tracks.waitForMotors();
			}
		    if(!tracks.motorsMoving()) {
		    	tracks.forward();
		    }
		  distance =  us.getMeasurment();
		  if(distance < NEAREST && !bumped &&  !linefound) {
			  tracks.setSpeedRight(TURNINGSPEED);
			  while(distance < NEAREST && distance > (NEAREST - TONEAREST) && !bumped && !linefound){
				  distance = us.getMeasurment();
				  sleep(10);
			  }
			  if(distance <= (NEAREST -  TONEAREST) && !bumped && ! linefound) {
				  tracks.setSpeedRight(MOVINGSPEED);
				  tracks.setSpeedLeft(TURNINGSPEED);
				  while(distance < (NEAREST -  TONEAREST) && !bumped && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }
			  tracks.setSpeed(MOVINGSPEED);
		  } 
		  if(distance > FAREST && !bumped && !linefound) {
			  tracks.setSpeedLeft(TURNINGSPEED);
			  while(distance > FAREST && distance < (FAREST + 10) && !bumped  && !linefound){
				  distance = us.getMeasurment(); 
				  sleep(10);
			  }
			  if(distance > (FAREST + 10) && (distance < TURNRIGHT) && !bumped  && !linefound) {
				  tracks.setSpeedLeft(MOVINGSPEED);
				  tracks.setSpeedRight(TURNINGSPEED);
				  while(distance > FAREST && (distance < TURNRIGHT) && !bumped  && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }
			  tracks.setSpeed(MOVINGSPEED);
		  }
		}
		bump.halt();
	}
	private void driveAlongRightWall() {
		int distance = 0;
		while(running) {
			if(bumped  && !linefound) {
				tracks.stop();
				tracks.backward(backward);
				tracks.pivotAngleLeft(90);
				tracks.waitForMotors();
			}
		    if(!tracks.motorsMoving() && !bumped  && !linefound) {
		    	tracks.forward();
		    }
		  distance =  us.getMeasurment();
		  if(distance >= TURNRIGHT && !linefound) {
			  hitWallTurnRight();
		  }
		  if(distance < NEAREST && !bumped  && !linefound) {
			  tracks.setSpeedLeft(TURNINGSPEED - 50);
			  while(distance < NEAREST && !bumped  && !linefound){
				  distance = us.getMeasurment();
				  sleep(10);
			  }
		 /*  if(distance <= (NEAREST -  TONEAREST)&& !bumped  && !linefound) {
				  tracks.setSpeedLeft(MOVINGSPEED);
				  tracks.setSpeedRight(TURNINGSPEED);
				  while(distance < (NEAREST -  TONEAREST) && !bumped  && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }*/
			  tracks.setSpeed(MOVINGSPEED);
		  } 
		  if(distance > FAREST && !bumped  && !linefound) {
			  tracks.setSpeedRight(TURNINGSPEED);
			  while(distance > FAREST && distance < (FAREST + 10) && !bumped  && !linefound){
				  distance = us.getMeasurment(); 
				  sleep(10);
			  }
			  if(distance >= TURNRIGHT  && !linefound) {
				  hitWallTurnRight();
			  }
			  if(distance > (FAREST  + 10) && (distance < TURNRIGHT) && !bumped  && !linefound) {
				  tracks.setSpeedRight(MOVINGSPEED);
				  tracks.setSpeedLeft(TURNINGSPEED);
				  while(distance > FAREST && (distance < TURNRIGHT) && !bumped  && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }
			  tracks.setSpeed(MOVINGSPEED);
		  }
		}
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}
	
	private void bumped() {
		bumped = true;
	}
	
	private void released() {
		bumped = false;
	}
	
	private void linefound(){
		linefound = true;
		halt();
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void hitWallTurnRight() {
		while(running && !bumped && !linefound) {
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		if(!linefound) {
		tracks.stop();
		tracks.backward(backward - 30);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
		}
		while(!linefound) {
			if( light.getLightValue() <= 35) {
				if(!tracks.motorsMoving()) {
					tracks.forward();	
				}
			} else {
				linefound();
				halt();
			}
		}
	}
	
	private class Bumper implements Program {
		BumpSensor bumper;
		private boolean running;
		public Bumper() {
			 bumper = new BumpSensor(SensorPort.S1, SensorPort.S2);
			 running = true;
		}
		@Override
		public void run() {
			while(running) {
				if(light.getLightValue() >= 40) {
					linefound();
				}
				while(bumper.touchedFront()) {
					bumped();
					sleep(50);
				}
				released();
				sleep(50);
			}
			
		}

		@Override
		public void halt() {
			 running = false;
			
		}

		@Override
		public boolean isRunning() {
			return running;
		}
		
	}

}
