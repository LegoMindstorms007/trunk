package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.DoorDriving;
import Programs.DownwardFollower;
import Programs.Labyrinth;
import Programs.Labyrinthleft;
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

	private static final int NUM_PROGRAMS = 10;

	public static final SensorPort BUMP_RIGHT = SensorPort.S1;
	public static final SensorPort BUMP_LEFT = SensorPort.S2;
	public static final SensorPort ULTRA_SOUND = SensorPort.S3;
	public static final SensorPort LIGHT = SensorPort.S4;

	public static void main(String[] args) {

		int program = 0;
		boolean goLeftInLabyrinth = false;
		Program current = null;
		BarcodeReader barcode = new BarcodeReader();

		while (program < NUM_PROGRAMS) {

			if (current != null) {
				while (current.isRunning()) {
					current.halt();
					sleep(10);
				}
			}

			int buttonPushed = 0;

			while (buttonPushed != Button.ID_ENTER) {
				LCD.clear();
				LCD.drawString("Labyrinth richtung:", 0, 0);

				if (goLeftInLabyrinth)
					LCD.drawString("Left", 0, 2);
				else
					LCD.drawString("Right", 0, 2);

				buttonPushed = Button.waitForAnyPress();

				if (buttonPushed == Button.ID_LEFT)
					goLeftInLabyrinth = true;
				else if (buttonPushed == Button.ID_RIGHT)
					goLeftInLabyrinth = false;
			}
			buttonPushed = 0;

			while (buttonPushed != Button.ID_ENTER) {
				LCD.clear();
				LCD.drawString("Programm wählen:", 0, 0);
				String programName = "";
				switch (program) {
				case 0:
					programName = "Start Programm";
					break;
				case 1:
					programName = "Linie folgen";
					break;
				case 2:
					programName = "Brücke fahren";
					break;
				case 3:
					programName = "Lift fahren";
					break;
				case 4:
					programName = "Labyrinth";
					break;
				case 5:
					programName = "Sumpf + Gate";
					break;
				case 6:
					programName = "Hängebrücke";
					break;
				case 7:
					programName = "Nach Hängebrücke";
					break;
				case 8:
					programName = "Drehteller";
					break;
				case 9:
					programName = "Endgegner";
					break;
				}

				LCD.drawString(programName, 0, 2);

				buttonPushed = Button.waitForAnyPress();

				if (buttonPushed == Button.ID_LEFT)
					program += NUM_PROGRAMS - 1;
				else if (buttonPushed == Button.ID_RIGHT)
					program++;

				program %= NUM_PROGRAMS;
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
				if (goLeftInLabyrinth)
					current = new Labyrinthleft();
				else
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
				current = new DownwardFollower();
				break;
			case 8:
				current = new TurnTableProgram();
				break;
			case 9:
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
