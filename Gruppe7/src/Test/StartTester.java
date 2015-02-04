package Test;

import Programs.Start;
import Programs.StartBackwards;
import RobotMovement.TrackSuspension;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;

public class StartTester {
	public static void main(String[] args) {
		Start start = new Start(SensorPort.S4, SensorPort.S3);
		new Thread(start).start();
		Button.waitForAnyPress();
		start.halt();
	}
}
