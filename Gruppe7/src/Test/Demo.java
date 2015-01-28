package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

public class Demo {

	public static void main(String[] args) {
		// TestThread test = new TestThread();
		// new Thread(test).start();
		/*
		 * RobotTest testbot = new RobotTest();
		 * testbot.suspension.setSpeed(1000);
		 * testbot.suspension.pivotAngleRight(90);
		 * testbot.suspension.waitForMotors();
		 * testbot.suspension.pivotAngleLeft(90);
		 */
		LineFolower follower = new LineFolower(SensorPort.S4);
		new Thread(follower).start();
		// Labyrinth maze = new Labyrinth();
		// new Thread(maze).start();
		Button.waitForAnyPress();
		// maze.halt();
		follower.halt();
		// test.halt();
	}
}
