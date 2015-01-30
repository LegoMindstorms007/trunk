package Test;

import Programs.BridgeDriving;
import Programs.LineFollower;
import Programs.PlankBridge;
import RobotMovement.LightSweeper;
import RobotMovement.SensorArm;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class BridgeTester {
	public static void main(String[] args) {
	/*	RConsole.openBluetooth(0);
		BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt(); */
	/*	RConsole.openBluetooth(0);
		LightSweeper sweeper = new LightSweeper(SensorPort.S4);
		new Thread(sweeper).start();
		Button.waitForAnyPress();
		sweeper.halt();*/ 
	/*	SensorArm arm = new SensorArm();
		arm.setSpeed(50);
		LightSensor light = new LightSensor(SensorPort.S4);
		while(true) {
			arm.turnToPosition(SensorArm.MAXLEFT, true); 
			while(arm.isMoving()) {
				LCD.drawString(String.valueOf(light.getLightValue()), 0, 1);
			}
			arm.turnToPosition(SensorArm.MAXRIGHT, true); 
			while(arm.isMoving()) {
				LCD.drawString(String.valueOf(light.getLightValue()), 0, 1);
				
			}
			
		}*/
		LineFollower follower = new LineFollower(SensorPort.S4, SensorPort.S3);
		new Thread(follower).start();
		Button.waitForAnyPress();
		follower.halt();
	}
}
