package Test;

import RobotMovement.SensorArm;
import lejos.nxt.Button;
public class Demo {

	public static void main(String[] args) {
		// TestThread test = new TestThread();
		// new Thread(test).start();
	//	LightBridgeDriving bridge = new LightBridgeDriving();
	//	new Thread(bridge).start();
		//LineFolower follower = new LineFolower(SensorPort.S4);
		//new Thread(follower).start();
		BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		//follower.halt();
		// test.halt();
		//bridge.halt();
	}
}
