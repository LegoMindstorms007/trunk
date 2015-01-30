package Test;

import lejos.nxt.SensorPort;
import Programs.LiftDriving;

public class LiftTester {
	public static void main(String args[]) {
		LiftDriving lift = new LiftDriving(SensorPort.S4, SensorPort.S2,
				SensorPort.S1);

		new Thread(lift).start();
	}

}
