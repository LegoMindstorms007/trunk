package Test;

import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import Programs.StartNeu;

public class StartTester {
	public static void main(String[] args) {
		LCD.drawString("Voltage: " + Battery.getVoltage(), 0, 1);
		Button.waitForAnyPress();
		StartNeu start = new StartNeu(SensorPort.S4, SensorPort.S3);
		new Thread(start).start();
		Button.waitForAnyPress();
		start.halt();
	}
}
