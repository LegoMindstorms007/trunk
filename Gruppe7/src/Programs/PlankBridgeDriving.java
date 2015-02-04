package Programs;

import lejos.nxt.SensorPort;

public class PlankBridgeDriving extends BridgeDriving {
	private int frontcounter;
	public static final int NOGROUND = 25;

	public PlankBridgeDriving(SensorPort lightPort, SensorPort ultraSoundPort) {
		super(lightPort, ultraSoundPort);
		frontcounter = 0;
	}

	@Override
	protected void findBridge() {
		track.setSpeed(2000);
		track.forward(250);
	}

	@Override
	protected void driveOverBridge() {
		track.setSpeed(MOVING_SPEED);
		Thread sweeping = new Thread(sweeper);
		sweeping.start();
		last = null;
		while (running) {
			if (!track.motorsMoving()) {
				track.forward();
			}
			int measurement = light.getLightValue();
			if (measurement <= NOGROUND) {
				int position = arm.getArmPosition();
				track.stop();
				track.setSpeed(ROTATINGSPEED);
				if (position > -45 && position < 45) {
					sweeper.stopSweeping();
					arm.turnToCenter();
					track.forward(10);
					if (light.getLightValue() >= NOGROUND) {
						// Keine L�cke
						frontcounter++;
					}
					if (frontcounter == 2) {
						sweeper.halt();
						track.stop();
						halt();
					}
					sweeper.startSweeping();
				} else {
					if (position < 0) {
						turnLeft(10);
					} else {
						turnRight(10);
					}
					track.setSpeed(MOVING_SPEED);
				}
			}
		}
		sweeper.stopSweeping();
		track.stop();
		sweeper.halt();
		track.stop();
		arm.turnToCenter();
		running = false;
	}

}
