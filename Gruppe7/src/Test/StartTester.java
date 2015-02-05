package Test;

import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import Programs.StartNeu;

public class StartTester {
	public static void main(String[] args) {
		LCD.drawString("Voltage: " + Battery.getVoltage(), 0, 1);
		Button.waitForAnyPress();
		StartNeu start = new StartNeu();
		new Thread(start).start();
		Button.waitForAnyPress();
		start.halt();
	}
}
