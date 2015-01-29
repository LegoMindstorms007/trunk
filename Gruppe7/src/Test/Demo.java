package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.LineFolower;
import Programs.Program;
import RobotMovement.BarcodeReader;

public class Demo {

	private static final int numPrograms = 2;

	public static void main(String[] args) {

		int program = 0;
		LineFolower follower = new LineFolower(SensorPort.S4, SensorPort.S3);
		BridgeDriving bridge = new BridgeDriving();
		Program current = null;
		BarcodeReader barcode = new BarcodeReader(SensorPort.S4);

		while (program < numPrograms) {
			switch (program) {
			case 0:
				current = follower;
				break;
			case 1:
				current = bridge;
				break;
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
