package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import Programs.DoorDriving;
import Programs.PlankBridge;
import Programs.TurnTableProgram;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;

public class DoorTest {

	public static void main(String args[]) {
		DoorDriving door = new DoorDriving(SensorPort.S3, SensorPort.S1,
				SensorPort.S2);
		PlankBridge bridge = new PlankBridge(SensorPort.S4, SensorPort.S3);
		Aligner aligner = new Aligner(SensorPort.S4, 35, false);
		BarcodeReader barcode = new BarcodeReader(SensorPort.S4);
		TurnTableProgram turntable = new TurnTableProgram(SensorPort.S4,
				SensorPort.S3);
		Button.waitForAnyPress();
		aligner.align();

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
