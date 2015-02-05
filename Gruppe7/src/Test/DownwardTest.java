package Test;

import lejos.nxt.Button;
import Programs.DownwardFollower;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;

public class DownwardTest {

	public static void main(String args[]) {
		DownwardFollower follower = new DownwardFollower();

		new Thread(follower).start();
		while (!follower.isRunning())
			;

		while (follower.isRunning()) {
			if (Button.waitForAnyPress(100) > 0) {
				follower.halt();
			}
		}
		BarcodeReader bcReader = new BarcodeReader();
		bcReader.readBarcode();
		Aligner aligner = new Aligner(35, false);
		aligner.align();
	}
}
