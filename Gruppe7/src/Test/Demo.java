package Test;

import RobotMovement.SensorArm;
import RobotMovement.UltrasoundSensor;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
public class Demo {

	public static void main(String[] args) {
		// TestThread test = new TestThread();
		// new Thread(test).start();
	//	LightBridgeDriving bridge = new LightBridgeDriving();
	//	new Thread(bridge).start();
		//LineFolower follower = new LineFolower(SensorPort.S4);
		//new Thread(follower).start();
	//	BridgeDriving bridge = new BridgeDriving();
	//	new Thread(bridge).start();
		// Labyrinth maze = new Labyrinth();
		// new Thread(maze).start();
		RConsole.openBluetooth(0);
		Start start = new Start();
		new Thread(start).start();
		Button.waitForAnyPress();
		//follower.halt();
		// test.halt();
	//	bridge.halt();
		start.halt();
		
	}
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
