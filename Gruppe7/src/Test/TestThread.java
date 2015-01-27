package Test;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import RobotMovement.UltrasoundArm;

public class TestThread implements Runnable {
	private static final int MOTORSPEED = 90;
	private static final int TIMEOUT = 300;
	private static final int DEGREE = 65;
	private UltrasoundArm arm;
	private boolean running;

	private int measurements[];
	private long times[];

	public TestThread() {
		arm = new UltrasoundArm(SensorPort.S3);
		times = new int[3];
		measurements = new int[3];
	}

	@Override
	public void run() {
		Motor.C.setSpeed(MOTORSPEED);
		int i = 0;
		running = true;
		while (running) {
			switch (i) {
			case 0:
				arm.turnToLeft();
				measurements[0] = arm.getMeasurment();
				times[0] = System.currentTimeMillis();
				break;
			case 2:
				arm.TurnToRight();
				measurements[0] = arm.getMeasurment();
				times[0] = System.currentTimeMillis();
				break;
			default:
				arm.center();
				measurements[0] = arm.getMeasurment();
				times[0] = System.currentTimeMillis();
			}
			sleep(TIMEOUT);
			i++;
			i %= 4;
		}

	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void halt() {
		running = false;
	}

}