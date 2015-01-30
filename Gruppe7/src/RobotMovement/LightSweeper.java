package RobotMovement;

import lejos.nxt.ADSensorPort;

public class LightSweeper implements Runnable {
	boolean running;
	boolean moving;
	private SensorArm arm;

	public LightSweeper(ADSensorPort port) {
		arm = new SensorArm();
	}

	@Override
	public void run() {
		running = true;
		moving = true;
		while (running) {
			while (running && moving) {
				arm.tilt();
			}
			sleep(200);
		}
	}

	public void halt() {
		running = false;
	}

	public void stopSweeping() {
		moving = false;
	}

	public void startSweeping() {
		moving = true;
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
