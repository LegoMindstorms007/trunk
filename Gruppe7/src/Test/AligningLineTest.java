package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import RobotMovement.LineAligner;

public class AligningLineTest {

	public static void main(String args[]) {
		Button.waitForAnyPress();

		new LineAligner(SensorPort.S4).align();
	}
}
