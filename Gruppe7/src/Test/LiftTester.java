package Test;

import lejos.nxt.Button;
import Programs.BridgeDriving;
import Programs.LiftDriving;

public class LiftTester {
	public static void main(String args[]) {
		BridgeDriving bridge = new BridgeDriving();
		LiftDriving lift = new LiftDriving();
		new Thread(bridge).start();
		sleep(100);
		while (bridge.isRunning()) {
			sleep(100);
		}
		new Thread(lift).start();
		sleep(100);
		Button.waitForAnyPress();
		lift.halt();

	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
