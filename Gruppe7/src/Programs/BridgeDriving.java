package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;

public class BridgeDriving implements Program {
	private static final int MOVING_SPEED = 200;
	private static final int SWEEPING_SPEED = 400;
	private static final int ROTATINGSPEED = 500;
	public static final int NOGROUND = 25;
	private static final int BLACKGROUND = 26;
	private TrackSuspension track;
	private SensorArm arm;
	private LightSensor light;
	private boolean running;
	private LightSweeper sweeper;
	Last last;
	private int frontcounter;

	public BridgeDriving(SensorPort lightPort, SensorPort ultraSoundPort) {
		track = new TrackSuspension();
		arm = new SensorArm();
		light = new LightSensor(lightPort);
		arm.setSpeed(SWEEPING_SPEED);
		track.setSpeed(MOVING_SPEED);
		sweeper = new LightSweeper(ultraSoundPort);
		frontcounter = 0;
		running = true;
	}

	@Override
	public void run() {
		//findBridge();
		// Start the sweeping to prevent a Downfall
		driveOverBridge();
	}

	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public enum Last {
		LEFT, RIGHT;
	}

	protected void turnLeft(int angle) {
		track.backward(10);
		track.pivotAngleLeft(angle);
		track.waitForMotors();
		last = Last.LEFT;
	}

	protected void turnRight(int angle) {
		track.backward(10);
		track.pivotAngleRight(angle);
		track.waitForMotors();
		last = Last.RIGHT;
	}

	protected void findBridge() {
		boolean foundBridge = false;
		track.setSpeed(1000);
		// Find the Bridge
		while (running && !foundBridge) {
			if (!track.motorsMoving()) {
				track.forward();
			}
			if (light.getLightValue() >= BLACKGROUND) {
				foundBridge = true;
				track.forward(250);
			}
			sleep(50);
		}
	}

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
				if (position > -40 && position < 40) {
					if(frontcounter >=3) {
						track.stop();
						sweeper.stopSweeping();
						sweeper.halt();
						halt();
					} else {
						track.forward(10);
						frontcounter++;
					}
				} else {
					if (position < 0) {
						turnLeft(20);
						// Problem if found Cliff direcetly in Front
					} else if (position == 0) {
					/*if (last == null) {
							turnLeft(10);
						} else if (last == Last.RIGHT) {
							turnRight(10);
						} else {
							turnLeft(10);
						turnLeft(10);
					} else if (last == Last.RIGHT) {
						turnRight(10);
					} else {
						turnLeft(10);
					}*/
					} else {
						turnRight(20);
					}
					track.setSpeed(MOVING_SPEED);
				} else {
					turnRight(20);
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
		arm.turnToCenter();
		running = false;
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
