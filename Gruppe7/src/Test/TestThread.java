package Test;

import lejos.nxt.Motor;

public class TestThread implements Runnable {

	private static final int TIMEOUT = 200;

	@Override
	public void run() {
		while (true) {
			Motor.C.rotate(45);
			sleep(TIMEOUT);
			Motor.C.rotate(-45);
			sleep(TIMEOUT);
			Motor.C.rotate(-45);
			sleep(TIMEOUT);
			Motor.C.rotate(45);
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