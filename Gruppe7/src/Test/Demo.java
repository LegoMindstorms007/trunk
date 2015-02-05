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
import Sensors.UltrasoundSensor;

public class Demo {

	private static final int NUM_PROGRAMS = 9;

	public static final SensorPort BUMP_RIGHT = SensorPort.S1;
	public static final SensorPort BUMP_LEFT = SensorPort.S2;
	public static final SensorPort ULTRA_SOUND = SensorPort.S3;
	public static final SensorPort LIGHT = SensorPort.S4;

	public static void main(String[] args) {

		int program = 0;
		Program current = null;
		BarcodeReader barcode = new BarcodeReader();

		while (program < NUM_PROGRAMS) {

			if (current != null) {
				while (current.isRunning()) {
					current.halt();
					sleep(10);
				}
			}

			switch (program) {
			case 0:
				current = new StartNeu();
				LCD.drawString("Start", 0, 0);
				break;
			case 1:
				current = new LineFollower();
				LCD.drawString("Line", 0, 0);
				break;
			case 2:
				current = new BridgeDriving();
				LCD.drawString("Bridge", 0, 0);
				break;
			case 3:
				current = new LiftDriving();
				LCD.drawString("Lift ", 0, 0);
				break;
			case 4:
				current = new Labyrinth();
				LCD.drawString("Labyrinth", 0, 0);
				break;
			case 5:
				current = new DoorDriving();
				break;
			case 6:
				current = new PlankBridge();
				break;
			case 7:
				current = new TurnTableProgram();
				break;
			case 8:
				// TODO : Boss
				TrackSuspension track = TrackSuspension.getInstance();
				track.setSpeed(5000);
				track.forward();
				SensorArm.getInstance().shootLeft();
				SensorArm.getInstance().shootRight();
				SensorArm.getInstance().turnToPosition(90);
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
					case 4:
						alignOnRight(12);
					case 6:
						Aligner aligner = new Aligner(35, false);
						LCD.drawString(
								"Barcode value: " + barcode.readBarcode(), 0, 1);
						aligner.align();
						break;
					case 1:
						aligner = new Aligner(35, true);
						LCD.drawString(
								"Barcode value: " + barcode.readBarcode(), 0, 1);
						TrackSuspension.getInstance().forward(50);
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

	private static void alignOnRight(int maxDist) {
		SensorArm.getInstance().turnToPosition(-90);
		if (UltrasoundSensor.getInstanceOf().getMeasurment() > maxDist + 1) {
			TrackSuspension movement = TrackSuspension.getInstance();
			movement.turnRight(-45);
			movement.waitForMotors();
			movement.backward(100);
			movement.turnRight(45);
			movement.waitForMotors();
			movement.forward(150);
		}
		SensorArm.getInstance().turnToCenter();
	}
}
