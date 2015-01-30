package Test;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;

public class ValueTester {

	public static void main(String[] args) {
			SensorArm arm = new SensorArm();
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
			
		}
	}

}
