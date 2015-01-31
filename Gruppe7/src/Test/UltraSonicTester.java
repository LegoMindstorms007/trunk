package Test;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class StartTester {

	public static void main(String[] args) {
		//RConsole.openBluetooth(0);
		Start start = new Start();
		new Thread(start).start();
		Button.waitForAnyPress();
		start.halt();
	/*	BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();*/

	}

}
