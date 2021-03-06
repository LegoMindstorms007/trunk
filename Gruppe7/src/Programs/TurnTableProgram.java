package Programs;

import Communication.TurnTable;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

public class TurnTableProgram implements Program {
	private TurnTableFollower upwards;
	private boolean running;
	UltrasoundSensor us;
	TrackSuspension tracks;
	private TurnTable table;
	SensorArm arm;

	public TurnTableProgram() {
		upwards = new TurnTableFollower();
		us = UltrasoundSensor.getInstanceOf();
		tracks = TrackSuspension.getInstance();
		table = new TurnTable();
		running = true;
		arm = SensorArm.getInstance();
	}

	@Override
	public void run() {
		while (running) {
			tracks.forward(100);
			table.connect();
			new Thread(upwards).start();
			sleep(100);
			while (upwards.isRunning() && running) {
				sleep(100);
			}
			while (!table.isConnected() && running) {
				sleep(100);
			}
			driveInTable();
			while (!table.turn() && running) {
				sleep(100);
			}
			driveOutTable();
			new Thread(upwards).start();
			sleep(100);
			while (upwards.isRunning() && running) {
				sleep(100);
			}
			new Thread(upwards).start();
			sleep(100);
			while (upwards.isRunning() && running) {
				sleep(100);
			}
			halt();
		}
	}

	@Override
	public void halt() {
		if (table.isConnected()) {
			table.deregister();
		}
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void driveInTable() {
		arm.turnToCenter();
		tracks.stop();
		while (us.getMeasurment() >= 10 && running) {
			if (!tracks.motorsMoving()) {
				tracks.forward();
			}
			sleep(10);
		}
		tracks.stop();
	}

	private void driveOutTable() {
		tracks.setSpeed(2000);
		tracks.stop();
		tracks.backward(220);
		table.deregister();
		tracks.pivotAngleRight(190);
		tracks.waitForMotors();
	}
}
