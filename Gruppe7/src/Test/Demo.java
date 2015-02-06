package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.BridgeDriving;
import Programs.DoorDriving;
import Programs.DownwardFollower;
import Programs.EndBoss;
import Programs.Labyrinthleft;
import Programs.Labyrinthright;
import Programs.LiftDriving;
import Programs.LineFollower;
import Programs.PlankBridge;
import Programs.Program;
import Programs.StartNeu;
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

		int buttonPushed = 0;

		while (buttonPushed != Button.ID_ENTER) {
			LCD.clear();
			LCD.drawString("Labyrinth direction:", 0, 0);

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
			LCD.drawString("Choose program:", 0, 0);
			String programName = "";
			switch (program) {
			case 0:
				programName = "Start program";
				break;
			case 1:
				programName = "line following";
				break;
			case 2:
				programName = "Bridge";
				break;
			case 3:
				programName = "Lift";
				break;
			case 4:
				programName = "Labyrinth";
				break;
			case 5:
				programName = "Gate";
				break;
			case 6:
				programName = "Plank-Bridge";
				break;
			case 7:
				programName = "After Plank-Bridge";
				break;
			// case 8:
			// programName = "Turntable";
			// break;
			case 8:
				programName = "Bossssssss";
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
				if (goLeftInLabyrinth) {
					current = new Labyrinthleft();
				} else {
					current = new Labyrinthright();
				}
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
			// case 8:
			// current = new TurnTableProgram();
			// break;
			case 8:
				current = new EndBoss();
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
						// case 7:
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
