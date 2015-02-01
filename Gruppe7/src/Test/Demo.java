package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.LiftDriving;
import Programs.LineFollower;
import Programs.Program;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;

public class Demo {

	private static final int NUM_PROGRAMS = 4;

	private static final SensorPort BUMP_RIGHT = SensorPort.S1;
	private static final SensorPort BUMP_LEFT = SensorPort.S2;
	private static final SensorPort ULTRA_SOUND = SensorPort.S3;
	private static final SensorPort LIGHT = SensorPort.S4;

	public static void main(String[] args) {

		int program = 0;
		LineFollower follower = new LineFollower(LIGHT, ULTRA_SOUND);
		BridgeDriving bridge = new BridgeDriving(LIGHT, ULTRA_SOUND);
		LiftDriving lift = new LiftDriving(LIGHT, BUMP_LEFT, BUMP_RIGHT);
		Program current = null;
		BarcodeReader barcode = new BarcodeReader(LIGHT);

		while (program < NUM_PROGRAMS) {

			if (current != null) {
				while (current.isRunning()) {
					current.halt();
					sleep(10);
				}
			}

			switch (program) {
			case 1:
				current = follower;
				LCD.drawString("Line", 0, 0);
				break;
			case 2:
				current = bridge;
				LCD.drawString("Bridge", 0, 0);
				break;
			case 3:
				current = lift;
				LCD.drawString("Lift ", 0, 0);
				break;
			default:
				current = null;
			}

			if (current != null) {
				new Thread(current).start();

				while (!current.isRunning()) {
					sleep(10);
				}
				boolean buttonPressed = false;
				while (current.isRunning()) {
					if (Button.waitForAnyPress(100) > 0) {
						buttonPressed = true;
						current.halt();
					}
				}
				if (!buttonPressed) {
					LCD.drawString("Barcode value: " + barcode.readBarcode(),
							0, 1);
					Aligner aligner = new Aligner(LIGHT, 35, false);
				}
			}

			program++;
		}
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
