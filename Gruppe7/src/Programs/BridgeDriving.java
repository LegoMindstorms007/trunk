package Programs;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.Light;

public class BridgeDriving implements Program {
	public static final int MOVING_SPEED = 200;
	public static final int SWEEPING_SPEED = 400;
	public static final int ROTATINGSPEED = 500;
	public static final int PANEL = 40;
	public static final int NOGROUND = 28;
	public static final int BLACKGROUND = 26;
	public static final int GREEN = 35;
	protected CollisionDetectionUS collisionDetection;
	protected TrackSuspension track;
	protected SensorArm arm;
	protected LightSensor light;
	protected boolean running;
	protected LightSweeper sweeper;
	protected BumpSensor bump;
	Last last;

	public BridgeDriving() {
		track = TrackSuspension.getInstance();
		arm = SensorArm.getInstance();
		light = Light.getInstanceOf();
		arm.setSpeed(SWEEPING_SPEED);
		track.setSpeed(MOVING_SPEED);
		sweeper = new LightSweeper();
		running = true;
		light.setFloodlight(true);
		collisionDetection = new CollisionDetectionUS();
		bump = BumpSensor.getInstanceOf();
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
		track.setSpeed(2000);
		// Find the Bridge
				arm.turnToPosition(SensorArm.MAXRIGHT);
				int currentTacho = track.getLeftTachoCount();
				int distance = 0;
				while(light.getLightValue() >= NOGROUND && running && (distance < 5000)) {
					if(!track.motorsMoving()) {
						track.forward();
					}
					if(bump.touchedAny()) {
						track.stop();
					}
					distance = track.getLeftTachoCount() - currentTacho;
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
			while(collisionDetection.possibleCollision()) {
				track.stop();
				sweeper.stopSweeping();
			}
			if(!sweeper.isSweeping()) {
				sweeper.startSweeping();
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
		return light.getLightValue();
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
