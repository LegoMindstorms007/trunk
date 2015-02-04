package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import Programs.DownwardFollower;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;

public class DownwardTest {

	public static void main(String args[]) {
		DownwardFollower follower = new DownwardFollower(SensorPort.S4,
				SensorPort.S3);

		new Thread(follower).start();
		while (!follower.isRunning())
			;

		while (follower.isRunning()) {
			if (Button.waitForAnyPress(100) > 0) {
				follower.halt();
			}
		}
		BarcodeReader bcReader = new BarcodeReader(SensorPort.S4);
		bcReader.readBarcode();
		Aligner aligner = new Aligner(SensorPort.S4, 35, false);
		aligner.align();
	}
}
