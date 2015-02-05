package Test;

import lejos.nxt.Button;
import Programs.TurnTableProgram;

public class TurnTableTester {
	public static void main(String[] args) {
		TurnTableProgram turn = new TurnTableProgram();
		new Thread(turn).start();
		Button.waitForAnyPress();
		turn.halt();
	}
}
