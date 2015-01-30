package Test;

import Programs.BridgeDriving;
import Programs.PlankBridge;
import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class BridgeTester {
	public static void main(String[] args) {
		/*BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();*/
		RConsole.openBluetooth(0);
		PlankBridge bridge = new PlankBridge();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();
	}
}
