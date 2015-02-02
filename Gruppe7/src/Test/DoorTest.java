package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import Programs.DoorDriving;
import RobotMovement.Aligner;

public class DoorTest {

	public static void main(String args[]) {
		DoorDriving door = new DoorDriving(SensorPort.S3);
		Aligner aligner = new Aligner(SensorPort.S4, 35, false);

		aligner.align();

		Button.waitForAnyPress();

		new Thread(door).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Button.waitForAnyPress(100) < 1) {
			// nothing
		}
		door.halt();
	}
}
