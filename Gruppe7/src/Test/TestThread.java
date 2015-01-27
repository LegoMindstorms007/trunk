package Test;

import lejos.nxt.Motor;

public class TestThread implements Runnable {

	private static final int TIMEOUT = 300;
	private static final int DEGREE = 65;
	@Override
	public void run() {
		Motor.C.setSpeed(60);
		while (true) {
			Motor.C.rotate(DEGREE);
			sleep(TIMEOUT);
			Motor.C.rotate(-DEGREE);
			sleep(TIMEOUT);
			Motor.C.rotate(-DEGREE);
			sleep(TIMEOUT);
			Motor.C.rotate(DEGREE);
			sleep(TIMEOUT);
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

}