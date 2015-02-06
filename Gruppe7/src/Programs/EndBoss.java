package Programs;

import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.UltrasoundSensor;

public class EndBoss implements Program {
	private boolean running;
	private BumpSensor bump;
	private UltrasoundSensor us;
	private SensorArm arm;
	private TrackSuspension tracks;
	private LightSweeper sweeper;
	public EndBoss() {
		bump = BumpSensor.getInstanceOf();
		us = UltrasoundSensor.getInstanceOf();
		tracks = TrackSuspension.getInstance();
		arm = SensorArm.getInstance();
		sweeper = new LightSweeper();
	}

	@Override
	public void run() {
		running = true;
		tracks.setSpeed(5000);
		arm.turnToPosition(SensorArm.MAXLEFT);
		tracks.forward(200);
			tracks.turnRight(70);
			tracks.waitForMotors();
			tracks.forward(300);
			tracks.turnLeft(70);
			tracks.waitForMotors();
		while(running) {
			if(bump.touchedAny()) {
				tracks.setSpeed(5000);
				tracks.stop();
				tracks.backward(150);
				tracks.stop();
				tracks.pivotAngleLeft(95);
				tracks.waitForMotors();
			}
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
			
		}
		running = false;
	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
