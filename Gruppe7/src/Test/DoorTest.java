package Test;

import lejos.nxt.Button;
import Programs.DoorDriving;
import Programs.PlankBridge;
import Programs.TurnTableProgram;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;

public class DoorTest {

	public static void main(String args[]) {
		DoorDriving door = new DoorDriving();
		PlankBridge bridge = new PlankBridge();
		Aligner aligner = new Aligner(35, false);
		BarcodeReader barcode = new BarcodeReader();
		TurnTableProgram turntable = new TurnTableProgram();
		Button.waitForAnyPress();

		new Thread(door).start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (Button.waitForAnyPress(100) < 1 && door.isRunning()) {
			// nothing
		}
		door.halt();
		new Thread(bridge).start();

		while (Button.waitForAnyPress(100) < 1 && bridge.isRunning()) {
			// nothing
		}
		bridge.halt();
		barcode.readBarcode();
		aligner.align();
		new Thread(turntable).start();
		while (Button.waitForAnyPress(100) < 1 && turntable.isRunning()) {
			// nothing
		}
		turntable.halt();
	}
}
