package Test;

import RobotMovement.RobotTest;
import RobotMovement.SensorArm;
import RobotMovement.UltrasoundArm;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class Demo {

	public static void main(String[] args) {
		TestThread test = new TestThread();
		//new Thread(test).start();
		//RobotTest testbot = new RobotTest();
		  /*testbot.suspension.setSpeed(1000);
		  testbot.suspension.pivotAngleRight(90); 
		  testbot.suspension.waitForMotors();
		  testbot.suspension.pivotAngleLeft(90);*/
	//	LineFolower follower = new LineFolower(SensorPort.S4);
	//	new Thread(follower).start();
	/*	RConsole.openBluetooth(0);
		
		BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();*/
		Button.waitForAnyPress();
	//	bridge.halt();
	//	follower.halt();
		//itest.halt();
	}

}
