package RobotMovement;

import Programs.BridgeDriving;
import lejos.nxt.ADSensorPort;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;


public class LightSweeper implements Runnable {
	boolean running;
	boolean moving;
	private SensorArm arm;
	private LightSensor light;
	public int[] angles;
	private static int LEFT = 0;
	private static int RIGHT = 1;
	private boolean lineFound;
	public LightSweeper(ADSensorPort port) {
		running = true;
		moving = true;
		arm = new SensorArm();
		light = new LightSensor(port);
		angles = new int[2];
		arm.setSpeed(300);
	}
	@Override
	public void run() {
		while(running) {
		while(moving) {
			arm.turnToPosition(SensorArm.MAXLEFT, true);
			int leftposition = 0;
			int rightposition = 0;
			while(arm.isMoving()) {
				if(light.getLightValue() >= BridgeDriving.NOGROUND) {
					leftposition = arm.getArmPosition();
					if(Math.abs(leftposition) < 40){ 
					angles[LEFT] = leftposition;
					}
				}
			}
			arm.turnToPosition(SensorArm.MAXRIGHT, true);
			while(arm.isMoving()) {
				if(light.getLightValue() >= BridgeDriving.NOGROUND) {
					rightposition = arm.getArmPosition();
					if(Math.abs(rightposition) < 4){ 
					angles[LEFT] = rightposition;
					}
				}
			}
			lineFound = false;
			LCD.drawString("Angle Left: " + angles[LEFT] + " Angle Right: " + angles[RIGHT], 0, 1);
			int between = Math.abs(angles[LEFT]) + Math.abs(angles[RIGHT]);
			if(between > 10 && between < 50) {
				lineFound = true;
				LCD.drawString("Found Line", 0, 5);
			} else {
				LCD.drawString("Not Found", 0, 5);
			}
		}
		sleep(200);
		}
	}
	
	public void halt() {
		running = false;
	}
	public void stopSweeping() {
		moving = false;
	}
	
	public void startSweeping() {
		moving = true;
	}
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public boolean lineFound() {
		return lineFound;
	}
}
