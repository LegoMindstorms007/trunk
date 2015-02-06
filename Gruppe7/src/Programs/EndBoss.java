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
		new Thread(sweeper).start();
		sleep(100);
		tracks.setSpeed(5000);
		arm.setSpeed(2000);
		tracks.forward(200);
		while (running) {
			tracks.turnRight(60);
			tracks.waitForMotors();
			tracks.forward(200);
			tracks.turnLeft(60);
			tracks.waitForMotors();
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
