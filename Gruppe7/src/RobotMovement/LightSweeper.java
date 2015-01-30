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
	public LightSweeper(ADSensorPort port) {
		running = true;
		moving = true;
		arm = new SensorArm();
	}
	@Override
	public void run() {
		while(running) {
		while(moving) {
		 arm.tilt();
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
}
