package Test;

import lejos.nxt.Button;
import Programs.BridgeDriving;
import Programs.PlankBridge;

public class BridgeTester {
	public static void main(String[] args) {
		BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();
		// BridgeDriving bridge = new BridgeDriving(SensorPort.S4,
		// SensorPort.S3);
		// new Thread(bridge).start();
		// Button.waitForAnyPress();
		// bridge.halt();
	}
}
