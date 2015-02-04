package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import Programs.LineFollower;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;
import RobotMovement.TrackSuspension;

public class LineTest {

	public static void main(String args[]) {
		Button.waitForAnyPress();

		LineFollower lf = new LineFollower();
		new Thread(lf).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (lf.isRunning() && Button.waitForAnyPress(50) <= 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Aligner aligner = new Aligner(35, true);
		LCD.drawString("Barcode value: " + new BarcodeReader().readBarcode(),
				0, 1);
		TrackSuspension.getInstance().forward(20);
		aligner.align();

		lf.halt();
	}
}
