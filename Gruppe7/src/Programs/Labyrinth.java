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

	public Labyrinth(SensorPort lightPort, SensorPort ultraSoundPort,
			SensorPort leftBumpPort, SensorPort rightBumpPort) {
		sArm = new SensorArm();
		sArm.setSpeed(ARM_SPEED);
		usSensor = new UltrasoundSensor(ultraSoundPort);
		movement = new TrackSuspension();
		movement.setSpeed(MOVING_SPEED);
		bump = new BumpSensor(leftBumpPort, rightBumpPort);
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
					} else if (usSensor.getMeasurment() < 8) {// adjust
						movement.backward(60);
						movement.waitForMotors();
						movement.pivotAngleLeft(15);
						movement.waitForMotors();
						movement.forward(50);
					} else if (usSensor.getMeasurment() > 15
							&& usSensor.getMeasurment() <= 23) {// adjust
						movement.pivotAngleRight(5);
						movement.waitForMotors();
						movement.forward(50);
					} else if (usSensor.getMeasurment() > 23) {
						movement.pivotAngleRight(10);
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
		boolean turn=false;
		movement.backward(90);
		movement.pivotAngleLeft(90);
		movement.waitForMotors();
		movement.forward(20);
		if(usSensor.getMeasurment() > 23 && usSensor.getMeasurment() < 50){
			turn=true;
		}
		while(turn){
			movement.backward(50);
			movement.pivotAngleLeft(50);
			movement.waitForMotors();
			movement.forward(20);
			if(isRightHolz()){
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
