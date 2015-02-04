package Programs;

import RobotMovement.SensorArm;
import lejos.nxt.SensorPort;

public class StartBackwards  extends Start{

	public StartBackwards(SensorPort lightPort, SensorPort ultraSoundPort) {
		super(lightPort, ultraSoundPort);
		bump = new BumperWithOutLight();
	}
	
	protected void findRightWall() {
		arm.turnToPosition(SensorArm.MAXRIGHT - 15);
	}
	protected void hitWallTurnRight() {
		tracks.setSpeed(MOVINGSPEED - 1000);
		tracks.stop();
		tracks.forward(100);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
		while(!bumped && running) {
			if(!tracks.motorsMoving()) {
			tracks.forward();
			}
		}
		tracks.stop();
		tracks.backward(backward);
		tracks.pivotAngleRight(90);
		tracks.waitForMotors();
		arm.turnToCenter();
		arm.turnToPosition(SensorArm.MAXLEFT);
		driveAlongLeftWall();
	}

	private class BumperWithOutLight extends Bumper {
		@Override
		public void run() {
			while(running) {
				while(bumper.touchedFront()) {
					bumped();
					sleep(50);
				}
				released();
				sleep(50);
			}
			
		}

	}
}
