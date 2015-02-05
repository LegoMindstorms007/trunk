package Test;

import lejos.nxt.LCD;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

public class WallDrivingTest {

	private static final int MIN_DISTANCE = 200;
	private static final int MAX_DISTANCE = 300;
	private static TrackSuspension track;
	private static SensorArm arm;
	private static UltrasoundSensor usSensor;
	private static USStuff us;

	public static void main(String args[]) {
		track = TrackSuspension.getInstance();
		arm = SensorArm.getInstance();
		usSensor = UltrasoundSensor.getInstanceOf();
		us = new USStuff(usSensor);
		us.start();

		arm.turnToPosition(-90);
		arm.waitForArm();
		track.setSpeed(500);
		track.forward();

		while (true) {
			/*
			 * if (shouldGoLeft() && !us.isGoingLeft()) { gearLeft(); } else if
			 * (!shouldGoLeft() && us.isGoingLeft) { gearRight(); } if
			 * (shouldGoRight() && !us.isGoingRight()) { gearRight(); } else if
			 * (!shouldGoRight() && us.isGoingRight()) { gearLeft(); }
			 */
			gearLeft();
			gearRight();
		}
	}

	public static boolean shouldGoRight() {
		return us.getLastMeasurement() <= MIN_DISTANCE;
	}

	public static boolean shouldGoLeft() {
		return usSensor.getMeasurment() >= MAX_DISTANCE;
	}

	public static void gearLeft() {
		track.manipulateRight(-100);
		sleep(1000);
		track.equalSpeed();
	}

	public static void gearRight() {
		track.manipulateLeft(-100);
		sleep(1000);
		track.equalSpeed();
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class USStuff extends Thread {

		private UltrasoundSensor usSensor;
		private boolean isGoingLeft;
		private boolean isGoingRight;
		private int lastMeasurement;
		private boolean running;

		public USStuff(UltrasoundSensor usSensor) {
			this.usSensor = usSensor;
			isGoingLeft = false;
			isGoingRight = false;
			lastMeasurement = 0;
			running = false;
		}

		@Override
		public void run() {
			running = true;
			while (running) {
				int curMeasurement = usSensor.getMeasurment();
				isGoingRight = lastMeasurement - curMeasurement >= 1;
				isGoingLeft = lastMeasurement - curMeasurement <= -1;
				lastMeasurement = curMeasurement;
				LCD.drawString("Measurement: " + lastMeasurement, 0, 0);
				LCD.drawString((isGoingLeft ? "Is going left"
						: (isGoingRight ? "Is going right"
								: "Is going straight")), 0, 1);
				sleep(100);
			}
		}

		public void halt() {
			running = false;
		}

		public boolean isGoingLeft() {
			return isGoingLeft;
		}

		public boolean isGoingRight() {
			return isGoingRight;
		}

		public int getLastMeasurement() {
			return lastMeasurement;
		}

		public void sleep(int millis) {
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
