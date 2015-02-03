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
		tracks.turnRight(120);
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
