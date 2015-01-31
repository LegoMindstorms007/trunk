package Programs;

import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

public class Labyrinth implements Runnable {

	private static final int MOVING_SPEED = 500;
	private static final int ARM_SPEED = 100;
	private SensorArm sensorArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;
	private boolean running;

	public Labyrinth() {
		sensorArm = new SensorArm();
		usSensor = new UltrasoundSensor(SensorPort.S3);
		movement = new TrackSuspension();
		movement.setSpeed(MOVING_SPEED);
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			if (isFree()) {
				movement.forward();
			} else {
				movement.stop();
				int angle = getBestDirection();
				if (angle < 0)
					movement.pivotAngleRight(-angle);
				else
					movement.pivotAngleLeft(angle);
			}
		}
	}

	private boolean isFree() {
		return usSensor.getMeasurment() > 20;
	}

	public void halt() {
		running = false;
	}

	private int getBestDirection() {
		int bestAngle = 0;
		int bestValue = 0;

		sensorArm.setSpeed(2 * ARM_SPEED);
		sensorArm.turnToPosition(-90);
		sensorArm.setSpeed(ARM_SPEED);
		sensorArm.turnToPosition(90);

		while (sensorArm.isMoving()) {
			int measurment = usSensor.getMeasurment();
			int angle = sensorArm.getArmPosition();
			if (measurment > bestValue) {
				bestValue = measurment;
				bestAngle = angle;
			}
		}

		return bestAngle;
	}
}
