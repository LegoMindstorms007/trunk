package Test;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class BridgeTester {
	public static void main(String[] args) {
		BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();

	}
}
