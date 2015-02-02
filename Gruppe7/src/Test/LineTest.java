package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import Programs.LineFollower;

public class LineTest {

	public static void main(String args[]) {
		Button.waitForAnyPress();

		LineFollower lf = new LineFollower(SensorPort.S4, SensorPort.S3);
		new Thread(lf).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Button.waitForAnyPress(50) <= 0) {
		}

		lf.halt();
	}

}
