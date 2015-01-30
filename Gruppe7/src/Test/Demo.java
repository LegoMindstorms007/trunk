package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.LiftDriving;
import Programs.LineFolower;
import Programs.Program;
import RobotMovement.BarcodeReader;

public class Demo {

	private static final int NUM_PROGRAMS = 3;
	private static final int START_WITH = 1;

	private static final SensorPort BUMP_RIGHT = SensorPort.S1;
	private static final SensorPort BUMP_LEFT = SensorPort.S2;
	private static final SensorPort ULTRA_SOUND = SensorPort.S3;
	private static final SensorPort LIGHT = SensorPort.S4;

	public static void main(String[] args) {

		int program = START_WITH;
		LineFolower follower = new LineFolower(LIGHT, ULTRA_SOUND);
		BridgeDriving bridge = new BridgeDriving();
		LiftDriving lift = new LiftDriving(LIGHT, BUMP_LEFT, BUMP_RIGHT);
		Program current = null;
		BarcodeReader barcode = new BarcodeReader(LIGHT);

		while (program < NUM_PROGRAMS) {
			switch (program) {
			case 1:
				current = follower;
				break;
			case 2:
				current = bridge;
				break;
			case 3:
				current = lift;
			}

			new Thread(current).start();
			Button.waitForAnyPress(100);
			boolean buttonPressed = false;
			while (current.isRunning()) {
				if (Button.waitForAnyPress(100) > 0) {
					buttonPressed = true;
					current.halt();
				}
			}
			if (!buttonPressed) {
				LCD.drawString("Barcode value: " + barcode.readBarcode(), 0, 1);
				barcode.alignOnBarcode();
			}

			program++;
		}
	}
}
