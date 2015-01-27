package Test;

import RobotMovement.RobotTest;

public class Demo {

	public static void main(String[] args) {
		TestThread test  = new TestThread();
		new Thread(test).start();
		RobotTest testbot = new RobotTest();
		testbot.suspension.setSpeed(1000);
		testbot.suspension.pivotAngleLeft(90);

	}

}
