package Test;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import RobotMovement.SensorArm;
import Sensors.Light;

public class ValueTester {

	public static void main(String[] args) {
			SensorArm arm= SensorArm.getInstance();
		arm.setSpeed(50);
		LightSensor light = Light.getInstanceOf();
		LightSensor light = new LightSensor(SensorPort.S4);
		light.setFloodlight(false);
		int lightValue = 0;
		while (true) {
			arm.turnToPosition(SensorArm.MAXLEFT, true);
			while (arm.isMoving()) {
		while(true) {
			arm.turnToPosition(SensorArm.MAXLEFT, true); 
			while(arm.isMoving()) {
				light.setFloodlight(true);
				lightValue = light.getLightValue();
				light.setFloodlight(false);
				sleep(10);
				lightValue += light.getLightValue();
				sleep(10);
				LCD.drawString(String.valueOf(lightValue), 0, 1);
			}
			arm.turnToPosition(SensorArm.MAXRIGHT, true); 
			while(arm.isMoving()) {
				light.setFloodlight(true);
				lightValue = light.getLightValue();
				light.setFloodlight(false);		
				sleep(10);
				lightValue += light.getLightValue();
				sleep(10);
				LCD.drawString(String.valueOf(lightValue), 0, 1);
			}

			
		}
	}

	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
