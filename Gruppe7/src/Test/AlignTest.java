package Test;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import Programs.LineFollower;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;
import RobotMovement.TrackSuspension;

public class AlignTest {

	public static void main(String args[]) {
		LightSensor light = new LightSensor(SensorPort.S4);
		LineFollower line = new LineFollower(SensorPort.S4, SensorPort.S3);
		BarcodeReader barcode = new BarcodeReader(SensorPort.S4);
		int valueFront;
		int valueBack;
		Button.waitForAnyPress();

		new Thread(line).start();

		while (!line.isRunning()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (line.isRunning()) {
			if (Button.waitForAnyPress(100) > 0) {
				line.halt();
			}
		}

		Sound.beep();

		barcode.readBarcode();
		Aligner aligner = new Aligner(SensorPort.S4, 35, false);

		Sound.buzz();
		aligner.align();

		Sound.twoBeeps();

		TrackSuspension track = new TrackSuspension();
		track.setSpeed(2000);

		track.forward(1400);

	}
}
