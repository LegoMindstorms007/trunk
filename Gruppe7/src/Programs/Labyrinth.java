package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.UltrasoundSensor;

public class Labyrinth implements Program {

	private static final int MOVING_SPEED = 650;
	private static final int ARM_SPEED = 150;
	private SensorArm sArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;
	private boolean running;
	private BumpSensor bump;
	private LightSensor light;
	// private int leftvalue=0;
	// private int rightvalue=0;
	private boolean adjust = true;

	// private int distance = 0;

	public Labyrinth(SensorPort lightPort, SensorPort ultraSoundPort,
			SensorPort leftBumpPort, SensorPort rightBumpPort) {
		sArm = new SensorArm();
		sArm.setSpeed(ARM_SPEED);
		usSensor = new UltrasoundSensor(ultraSoundPort);
		movement = new TrackSuspension();
		movement.setSpeed(MOVING_SPEED);
		bump = BumpSensor.getInstanceOf();
		light = new LightSensor(lightPort);
	}

	// @Override
	public void run() {
		running = true;
		sArm.turnArmRight(90);
		movement.forward(40);
		while (running) {
			if (!isLine()) {
				if (bump.touchedAny()) {
					searchHolzByCollision();
				} else if (isRightHolz()) {
					if (usSensor.getMeasurment() >= 8
							&& usSensor.getMeasurment() <= 15) {// follow the
																// wood
						movement.forward();
					} else if (usSensor.getMeasurment() < 5) {
						movement.backward(60);
						movement.pivotAngleLeft(30);
						movement.waitForMotors();
						movement.forward(40);
					} else if (usSensor.getMeasurment() >= 5
							&& usSensor.getMeasurment() < 8) {// adjust
						movement.backward(60);
						movement.pivotAngleLeft(15);
						movement.waitForMotors();
						movement.forward(50);
					} else if (usSensor.getMeasurment() > 15
							&& usSensor.getMeasurment() <= 24) {// adjust
						movement.pivotAngleRight(5);
						movement.waitForMotors();
						movement.forward(60);
					} else if (usSensor.getMeasurment() > 24) {
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
		while (adjust) {
			if (usSensor.getMeasurment() >= 5 || usSensor.getMeasurment() <= 7) {
				adjust = false;
			} else if (usSensor.getMeasurment() > 7) {
				movement.backward(50);
				movement.pivotAngleRight(5);
				movement.waitForMotors();
				movement.forward(45);
			} else if (usSensor.getMeasurment() < 5) {
				movement.backward(50);
				movement.pivotAngleLeft(5);
				movement.waitForMotors();
				movement.forward(45);

			}
		}

		sArm.turnToCenter();

		/*
		 * distance = getDistance(); while(change){ if(distance<=3 &&
		 * distance>=-3){ change = false; } else if(distance >3){
		 * movement.pivotAngleLeft(10); movement.waitForMotors();
		 * movement.backward(40); movement.pivotAngleRight(10);
		 * movement.waitForMotors(); movement.forward(35); distance =
		 * getDistance(); } else if(distance<-3){ movement.pivotAngleRight(10);
		 * movement.waitForMotors(); movement.backward(40);
		 * movement.pivotAngleLeft(10); movement.waitForMotors();
		 * movement.forward(35); distance = getDistance(); } }
		 */

	}

	private boolean isLine() {
		return light.getLightValue() >= 35;
	}

	/*
	 * private int getDistance(){ int dis; sArm.turnArmRight(90); rightvalue =
	 * usSensor.getMeasurment(); sArm.turnToCenter(); sArm.turnArmLeft(90);
	 * leftvalue = usSensor.getMeasurment(); sArm.turnToCenter(); dis =
	 * rightvalue - leftvalue; return dis; }
	 */

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

		boolean turn = false;
		movement.backward(80);
		movement.pivotAngleLeft(90);
		movement.waitForMotors();
		movement.forward(40);
		if (usSensor.getMeasurment() > 25 && usSensor.getMeasurment() < 50) {
			turn = true;
		}
		while (turn) {
			movement.backward(50);
			movement.pivotAngleLeft(50);
			movement.waitForMotors();
			movement.forward(20);
			if (isRightHolz()) {
				turn = false;
			}
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
