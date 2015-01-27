package Test;

import lejos.nxt.Motor;
import RobotMovement.UltrasoundArm;

public class TestThread implements Runnable {

	private static final int TIMEOUT = 300;
	private static final int DEGREE = 65;
	private UltrasoundArm arm;
	private boolean running;

	public TestThread() {
		arm = new UltrasoundArm();
	}

	@Override
	public void run() {
		Motor.C.setSpeed(60);
		int i = 0;
		running = true;
		while (running) {
			switch (i) {
			case 0:
				arm.turnToLeft();
				break;
			case 2:
				arm.TurnToRight();
				break;
			default:
				arm.center();
			}
			sleep(TIMEOUT);
			i++;
			i %= 4;
		}

	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void halt() {
		running = false;
	}

}