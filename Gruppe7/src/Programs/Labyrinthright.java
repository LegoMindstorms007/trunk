package Programs;

import lejos.nxt.LightSensor;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.Light;
import Sensors.UltrasoundSensor;

public class Labyrinthright implements Program {

	private static final int MOVING_SPEED = 750;
	private static final int ARM_SPEED = 200;
	private SensorArm sArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;
	private boolean running;
	private BumpSensor bump;
	private LightSensor light;
	// private int leftvalue=0;
	// private int rightvalue=0;
	private boolean adjust = true;
	private LineChecker checker;

	// private int distance = 0;

	public Labyrinthright() {
		sArm = SensorArm.getInstance();
		sArm.setSpeed(ARM_SPEED);
		usSensor = UltrasoundSensor.getInstanceOf();
		movement = TrackSuspension.getInstance();
		movement.setSpeed(MOVING_SPEED);
		bump = BumpSensor.getInstanceOf();
		light = Light.getInstanceOf();
		checker = new LineChecker(35);
	}

	// @Override
	public void run() {
		checker.start();
		running = true;
		sArm.turnToPosition(-90);
		movement.forward(40);
		// movement.forward();
		while (running) {
			if (!checker.isLine()) {
				if (bump.touchedAny()) {
					searchHolzByCollision();
				} else if (isRightHolz()) {
					int measurment = usSensor.getMeasurment();
					if (measurment >= 8 && measurment <= 15) {// follow the
																// wood
						movement.forward();
					} else if (measurment < 5) {
						movement.backward(60);
						movement.pivotAngleLeft(30);
						movement.waitForMotors();
						movement.forward(40);
					} else if (measurment >= 5 && measurment < 8) {// adjust
						movement.backward(60);
						movement.pivotAngleLeft(15);
						movement.waitForMotors();
						movement.forward(50);
					} else if (measurment > 15 && measurment <= 24) {// adjust
						movement.pivotAngleRight(5);
						movement.waitForMotors();
						movement.forward(60);
					} else if (measurment > 24) {
						movement.pivotAngleRight(15);
						movement.waitForMotors();
						movement.forward(50);
					}
				} else if (!isRightHolz()) {// air, turn to right
					turntoHolz();
				}
			} else {
				running = false;
			}

		}
		movement.stop();
		sArm.turnToCenter();
		checker.halt();
	}

	private boolean isLine() {
		return light.getLightValue() >= 35;
	}

	private boolean isRightHolz() {
		return usSensor.getMeasurment() <= 31;
	}

	private void turntoHolz() {// air
		movement.forward(100);
		movement.pivotAngleRight(90);
		movement.waitForMotors();
		movement.forward(150);
	}

	private void searchHolzByCollision() {
		/*
		 * int angle=0; movement.backward(70); sArm.turnArmLeft(180);
		 * while(sArm.isMoving()){ if(usSensor.getMeasurment()>60){ sArm.stop();
		 * angle=sArm.getArmPosition(); } } if(angle>0){
		 * movement.pivotAngleLeft(angle); movement.waitForMotors();
		 * sArm.turnToCenter(); sArm.turnArmRight(angle); } else if(angle ==0){
		 * movement.pivotAngleLeft(45); movement.waitForMotors();
		 * sArm.turnArmRight(90); } else if(angle < 0){
		 * movement.pivotAngleLeft(45); movement.waitForMotors();
		 * sArm.turnArmRight(90+angle); }
		 */

		// boolean turn = false;
		movement.backward(80);
		movement.pivotAngleLeft(90);
		movement.waitForMotors();
		movement.forward(50);
		/*
		 * int measurment = usSensor.getMeasurment(); if (measurment > 25 &&
		 * measurment < 50) { turn = true; } while (turn) {
		 * movement.backward(50); movement.pivotAngleLeft(50);
		 * movement.waitForMotors(); movement.forward(20); if (isRightHolz()) {
		 * turn = false; } }
		 */
	}

	@Override
	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private class LineChecker extends Thread {
		private boolean isLine;
		private int lightValue;
		private boolean running;
		private Light light;

		public LineChecker(int lightValue) {
			this.lightValue = lightValue;
			isLine = false;
			light = Light.getInstanceOf();
		}

		@Override
		public void run() {
			running = true;
			while (running && !isLine) {
				isLine = light.getLightValue() > lightValue;
				sleep(20);
			}
			running = false;
		}

		public void halt() {
			running = false;
		}

		public boolean isLine() {
			return isLine;
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
}
