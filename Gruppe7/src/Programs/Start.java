package Programs;

import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;
import Sensors.BumpSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;

public class Start implements Program {
	protected boolean running;
	UltrasoundSensor us;
	TrackSuspension tracks;
	protected boolean bumped;
	protected Bumper bump;
	SensorArm arm;
	private LightSensor light;
	private boolean linefound;
	final static int MOVINGSPEED = 2000;
	final static int TURNINGSPEED = 550;
	protected final static int backward = 110;
	private final static int NEAREST = 17;
	private final static int TONEAREST = 8;
	private final static int FAREST = 20;
	private final static int TURNRIGHT = 60;
	public Start(SensorPort lightPort, SensorPort ultraSoundPort) {
		 us = new UltrasoundSensor(ultraSoundPort);
		 tracks = new TrackSuspension();
		 running = true;
		 bumped = false;
		 arm = new SensorArm();
		 bump = new Bumper();
		 light = new LightSensor(lightPort);
		 new Thread(bump).start();
		 linefound = false;
	}
	@Override
	public void run() {
		
		tracks.setSpeed(MOVINGSPEED);
		findLeftWall();
		driveAlongLeftWall();
	}

	@Override
	public void halt() {
		running = false;
		
	}

	private void findLeftWall() {
		arm.turnToPosition(SensorArm.MAXLEFT);
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
	
	protected void findRightWall() {
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

	protected void driveAlongLeftWall() {
		int distance = 0;
		while(running) {
			if(bumped  && !linefound) {
				tracks.stop();
				tracks.backward(backward);
				tracks.pivotAngleRight(95);
				tracks.waitForMotors();
			}
		    if(!tracks.motorsMoving() && !bumped  && !linefound) {
		    	tracks.forward();
		    }
		  distance =  us.getMeasurment();
		  if(distance >= TURNRIGHT && !linefound) {
			  hitWallTurnLeft();
		  }
		  if(distance < NEAREST && !bumped  && !linefound) {
			  tracks.setSpeedRight(TURNINGSPEED);
			  while(distance < NEAREST && !bumped  && !linefound){
				  distance = us.getMeasurment();
				  sleep(10);
			  }
		  if(distance <= (NEAREST -  TONEAREST)&& !bumped  && !linefound) {
				  tracks.setSpeedLeft(MOVINGSPEED);
				  tracks.setSpeedRight(TURNINGSPEED - 150);
				  while(distance < (NEAREST -  TONEAREST) && !bumped  && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }
			  tracks.setSpeed(MOVINGSPEED);
		  } 
		  if(distance > FAREST && !bumped  && !linefound) {
			  tracks.setSpeedLeft(TURNINGSPEED);
			  while(distance > FAREST && distance < (FAREST + 10) && !bumped  && !linefound){
				  distance = us.getMeasurment(); 
				  sleep(10);
			  }
			  if(distance >= TURNRIGHT  && !linefound) {
				  hitWallTurnLeft();
			  }
			  if(distance > (FAREST  + 10) && (distance < TURNRIGHT) && !bumped  && !linefound) {
				  tracks.setSpeedLeft(MOVINGSPEED);
				  tracks.setSpeedRight(TURNINGSPEED - 100);
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
	private void hitWallTurnLeft() {
		tracks.setSpeed(MOVINGSPEED - 1000);
		tracks.stop();
		tracks.forward(120);
		tracks.pivotAngleLeft(90);
		tracks.waitForMotors();
		while(!bumped && running) {
			if(!tracks.motorsMoving()) {
			tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.pivotAngleLeft(90);
		tracks.waitForMotors();
		arm.turnToCenter();
		arm.turnToPosition(SensorArm.MAXRIGHT - 15);
		driveAlongRightWall();
		
	}
	protected void driveAlongRightWall() {
		int distance = 0;
		while(running) {
			if(bumped  && !linefound) {
				tracks.stop();
				tracks.backward(backward);
				tracks.pivotAngleLeft(95);
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
			  tracks.setSpeedLeft(TURNINGSPEED);
			  while(distance < NEAREST && !bumped  && !linefound){
				  distance = us.getMeasurment();
				  sleep(10);
			  }
		  if(distance <= (NEAREST -  TONEAREST)&& !bumped  && !linefound) {
				  tracks.setSpeedLeft(MOVINGSPEED);
				  tracks.setSpeedRight(TURNINGSPEED - 150);
				  while(distance < (NEAREST -  TONEAREST) && !bumped  && !linefound) {
					  distance = us.getMeasurment(); 
					  sleep(10);
				  }
				  tracks.setSpeed(MOVINGSPEED);
			  }
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
				  tracks.setSpeedLeft(TURNINGSPEED - 100);
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
	
	protected void bumped() {
		bumped = true;
	}
	
	protected void released() {
		bumped = false;
	}
	
	private void linefound(){
		linefound = true;
		halt();
	}
	
	protected void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void hitWallTurnRight() {
		while(running && !bumped && !linefound) {
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		if(!linefound) {
		tracks.backward(backward - 30);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
		arm.turnToCenter();
		}
		while(!linefound) {
			if( light.getLightValue() <= 35) {
				if(!tracks.motorsMoving()) {
					tracks.forward();	
				}
			} else {
				tracks.stop();
				linefound();
				halt();
			}
		}
	}
	
	protected class Bumper implements Program {
		BumpSensor bumper;
		protected boolean running;
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
