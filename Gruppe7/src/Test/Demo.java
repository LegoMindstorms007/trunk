package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import RobotMovement.RobotTest;

public class Demo {

	public static void main(String[] args) {
		//TestThread test  = new TestThread();
	//	new Thread(test).start();
		/*RobotTest testbot = new RobotTest();
		testbot.suspension.setSpeed(1000);
		testbot.suspension.pivotAngleRight(180);
		Button.waitForAnyPress();*/
		LineFolower follower = new LineFolower(SensorPort.S4);
		new Thread(follower).start();
	}

}
