package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class Demo {

	private static final int numPrograms = 2;

	public static void main(String[] args) {

		int program = 0;
		LineFolower follower = new LineFolower(SensorPort.S4, SensorPort.S3);
		BridgeDriving bridge = new BridgeDriving();
		Runnable current;

		while (program < numPrograms) {
			switch (program) {
			case 0:
				current = follower;
				break;
			case 1:
				current = bridge;
				break;
			}
			Button.waitForAnyPress();

			// should do this with interfaces
			follower.halt();
			bridge.halt();
			program++;
		}
	}

}
