package Test;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.SensorArm;
import Sensors.Light;

public class ValueTesterLight {

	public static void main(String[] args) {
		SensorArm arm = SensorArm.getInstance();
	arm.setSpeed(50);
	LightSensor light = Light.getInstanceOf();
	light.setFloodlight(true);
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
