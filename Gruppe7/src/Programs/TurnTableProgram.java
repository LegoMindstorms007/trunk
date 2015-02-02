package Programs;

import Communication.TurnTable;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;
import lejos.nxt.SensorPort;

public class TurnTableProgram implements Program {
	private TurnTableFollower upwards;
	private boolean running;
	UltrasoundSensor us;
	TrackSuspension tracks;
	private TurnTable table;
	SensorArm arm;
	public TurnTableProgram(SensorPort  portOfLightSensor, SensorPort portOfUsSensor) {
		upwards = new TurnTableFollower(portOfLightSensor, portOfUsSensor);
		us = new UltrasoundSensor(portOfUsSensor);
		tracks = new TrackSuspension();
		table = new TurnTable();
		running = true;
		arm = new SensorArm();
	}
	@Override
	public void run() {
		while(running) {
		new Thread(upwards).start();
		sleep(100);
		while(upwards.isRunning() && running) {
			sleep(100);
		}
		table.connect();
		driveInTable();
		while(!table.isConnected() && running) {
			sleep(100);
		}
		while(!table.turn() && running) {
			sleep(100);
		}
		halt();
		new Thread(upwards).start();
		sleep(100);
		while(upwards.isRunning() && running) {
			sleep(100);
		}
		}
	}

	@Override
	public void halt() {
		if(table.isConnected()) {
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
		while(us.getMeasurment() >= 10 && running) {
			if(!tracks.motorsMoving()) {
				tracks.forward();
			}
			sleep(10);
		}
		tracks.stop();
	}
	
	private void driveOutTable() {
		while(us.getMeasurment() < 30) {
			if(!tracks.motorsMoving()) {
				tracks.backward();
			}
			sleep(10);
		}
		tracks.stop();
		table.deregister();
		tracks.pivotAngleLeft(180);
		tracks.waitForMotors();
	}
}
