package Test;

import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import RobotMovement.UltrasoundSensor;

public class Labyrinth implements Runnable {

	private SensorArm sensorArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;

	public Labyrinth() {
		sensorArm = new SensorArm();
		usSensor = new UltrasoundSensor(SensorPort.S3);
		movement = new TrackSuspension();
	}

	@Override
	public void run() {
		while (isFree()) {
			if (!movement.motorsMoving())
				movement.forward();
		}
		movement.stop();
	}

	private boolean isFree() {
		return usSensor.getMeasurment() > 40;
	}
}
