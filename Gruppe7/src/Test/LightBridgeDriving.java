package Test;

import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LightBridgeDriving  implements Runnable {
	
	private boolean running;
	
	private TrackSuspension track;
	private LightSensor light;
	private final int NOGROUND = 25;
	private SensorArm arm;
	public LightBridgeDriving() {
		running = true;
		light = new LightSensor(SensorPort.S4);
		track = new TrackSuspension();
		arm = new SensorArm();
		arm.setSpeed(100);
	}
	@Override
	public void run() {
		Situation found = searchCLiff();
		if(found == Situation.CLIFFLEFT) {
			arm.turnToPosition(SensorArm.MAXLEFT);
			driveRightFromCliff();
		} else {
			arm.turnToPosition(SensorArm.MAXRIGHT);
			driveLeftFromCliff();
		}
		
	}
	public void halt() {
		running = false;
	}
	
	private Situation searchCLiff() {
		boolean clifffound = false;
		while(!clifffound) {
			track.forward(30);
			arm.turnToPosition(SensorArm.MAXLEFT);
			sleep(100);
			if(light.getLightValue() <= NOGROUND) {
				while(light.getLightValue() <=NOGROUND) {
					track.turnLeftBackward();
				}
				track.stop();
				return Situation.CLIFFLEFT;
			}
			sleep(100);
			arm.turnToPosition(SensorArm.MAXRIGHT);
			sleep(100);
			if(light.getLightValue() <= NOGROUND) {
				while(light.getLightValue() <=NOGROUND) {
					track.turnRightBackward();
				}
				track.stop();
				return Situation.CLIFFRIGHT;
			}
			arm.turnToCenter();
			sleep(100);
		}
		return Situation.SearchCLIFF;
	}
	
	private void driveLeftFromCliff() {
			while(running) {
				arm.turnToPosition(-45);
				if(light.getLightValue() >= NOGROUND) {
					arm.turnToPosition(SensorArm.MAXRIGHT);
					track.forward(50);
					if(light.getLightValue() >= NOGROUND) {
						track.turnRight(10);
						track.waitForMotors();
					}
				} else { 
					arm.turnToPosition(SensorArm.MAXRIGHT);
					track.turnLeft(10);
					track.waitForMotors();
				}
			}
	}
	private void driveRightFromCliff() {
		while(running) {
			arm.turnToPosition(45);
			if(light.getLightValue() >= NOGROUND) {
				arm.turnToPosition(SensorArm.MAXLEFT);
				track.forward(50);
				if(light.getLightValue() >= NOGROUND) {
					//track.turn(10);
					track.waitForMotors();
				}
			} else { 
				arm.turnToPosition(SensorArm.MAXLEFT);
				track.turnRight(10);
				track.waitForMotors();
			}

		}
	}
	private enum Situation {
		SearchCLIFF, CLIFFLEFT, CLIFFRIGHT;
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
