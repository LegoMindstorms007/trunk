package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import Programs.TurnTableProgram;

public class TurnTableTester {
	public static void main(String[] args) {
		TurnTableProgram turn = new TurnTableProgram(SensorPort.S4, SensorPort.S3);
		new Thread(turn).start();
		Button.waitForAnyPress();
		turn.halt();
	}
}
