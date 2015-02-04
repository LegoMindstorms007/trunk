package Programs;

import lejos.nxt.LightSensor;
import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.Light;

public class BridgeDriving implements Program {
	public static final int MOVING_SPEED = 200;
	public static final int SWEEPING_SPEED = 400;
	public static final int ROTATINGSPEED = 500;
	public static final int PANEL = 57;
	public static final int NOGROUND = 42;
	public static final int BLACKGROUND = 26;
	public static final int GREEN = 35;
	protected TrackSuspension track;
	protected SensorArm arm;
	protected LightSensor light;
	protected boolean running;
	protected LightSweeper sweeper;
	Last last;

	public BridgeDriving() {
		track = TrackSuspension.getInstance();
		;
		arm = new SensorArm();
		light = Light.getInstanceOf();
		arm.setSpeed(SWEEPING_SPEED);
		track.setSpeed(MOVING_SPEED);
		sweeper = new LightSweeper();
		running = true;
	}

	@Override
	public void run() {
		findBridge();
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
				track.forward(1300);
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
			Ground currentGround = checkGround(measure());
			if (currentGround == Ground.AIR) {
				int position = arm.getArmPosition();
				track.stop();
				track.setSpeed(ROTATINGSPEED);
				if (position < 0) {
					turnLeft(20);
					// Problem if found Cliff direcetly in Front
				} else if (position == 0) {
					if (last == null) {
						turnLeft(10);
					} else if (last == Last.RIGHT) {
						turnRight(10);
					} else {
						turnLeft(10);
						turnLeft(10);
					}
				} else {
					turnRight(20);
				}
				track.setSpeed(MOVING_SPEED);
			} else if (currentGround == Ground.PANEL) {
				track.stop();
				sweeper.stopSweeping();
				sweeper.halt();
				arm.turnToCenter();
				halt();
			}
		}
		sweeper.stopSweeping();
		track.stop();
		sweeper.halt();
		track.stop();
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

	private int measure() {
		// WithLight
		light.setFloodlight(true);
		int lightValue = light.getLightValue();
		light.setFloodlight(false);
		sleep(10);
		lightValue += light.getLightValue();
		sleep(10);
		return lightValue;
	}

	private Ground checkGround(int measurement) {
		if (measurement < NOGROUND) {
			return Ground.AIR;
		} else if (measure() > PANEL) {
			return Ground.PANEL;
		} else {
			return Ground.WOOD;
		}
	}

	private enum Ground {
		AIR, WOOD, PANEL;
	}
}
