package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.DoorDriving;
import Programs.Labyrinth;
import Programs.LiftDriving;
import Programs.LineFollower;
import Programs.PlankBridge;
import Programs.Program;
import Programs.StartNeu;
import Programs.TurnTableProgram;
import RobotMovement.Aligner;
import RobotMovement.BarcodeReader;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;

public class Demo {

	private static final int NUM_PROGRAMS = 9;

	public static final SensorPort BUMP_RIGHT = SensorPort.S1;
	public static final SensorPort BUMP_LEFT = SensorPort.S2;
	public static final SensorPort ULTRA_SOUND = SensorPort.S3;
	public static final SensorPort LIGHT = SensorPort.S4;

	public static void main(String[] args) {

		int program = 0;
		new LiftDriving(LIGHT, BUMP_LEFT, BUMP_RIGHT);
		Program current = null;
		BarcodeReader barcode = new BarcodeReader(LIGHT);

		while (program < NUM_PROGRAMS) {

			if (current != null) {
				while (current.isRunning()) {
					current.halt();
					sleep(10);
				}
			}

			switch (program) {
			case 0:
				current = new StartNeu(LIGHT, ULTRA_SOUND);
				LCD.drawString("Start", 0, 0);
				break;
			case 1:
				current = new LineFollower(LIGHT, ULTRA_SOUND);
				LCD.drawString("Line", 0, 0);
				break;
			case 2:
				current = new BridgeDriving(LIGHT, ULTRA_SOUND);
				LCD.drawString("Bridge", 0, 0);
				break;
			case 3:
				current = new LiftDriving(LIGHT, BUMP_LEFT, BUMP_RIGHT);
				LCD.drawString("Lift ", 0, 0);
				break;
			case 4:
				current = new Labyrinth(LIGHT, ULTRA_SOUND, BUMP_LEFT,
						BUMP_RIGHT);
				LCD.drawString("Labyrinth", 0, 0);
				break;
			case 5:
				current = new DoorDriving(ULTRA_SOUND, BUMP_LEFT, BUMP_RIGHT);
				break;
			case 6:
				current = new PlankBridge(LIGHT, ULTRA_SOUND);
				break;
			case 7:
				current = new TurnTableProgram(LIGHT, ULTRA_SOUND);
				break;
			case 8:
				// TODO : Boss
				TrackSuspension track = TrackSuspension.getInstance();
				track.setSpeed(5000);
				track.forward();
				new SensorArm().shootLeft();
				new SensorArm().shootRight();
				new SensorArm().turnToPosition(90);
				break;
			default:
				current = null;
			}

			if (current != null) {
				new Thread(current).start();

				while (!current.isRunning()) {
					sleep(100);
				}
				boolean buttonPressed = false;
				while (current.isRunning()) {
					if (Button.waitForAnyPress(100) > 0) {
						buttonPressed = true;
						current.halt();
					}
				}
				if (!buttonPressed) {
					switch (program) {
					case 1:
					case 4:
					case 6:
						LCD.drawString(
								"Barcode value: " + barcode.readBarcode(), 0, 1);
						Aligner aligner = new Aligner(LIGHT, 35, false);
						aligner.align();
						break;
					}
				}
			}

			program++;
			LCD.clear();
		}
		Button.waitForAnyPress();
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
