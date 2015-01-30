package Test;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import RobotMovement.Aligner;

public class AlignTest {

	public static void main(String args[]) {
		LightSensor light = new LightSensor(SensorPort.S4);
		int valueFront;
		int valueBack;

		Button.waitForAnyPress();
		valueFront = light.getLightValue();
		Button.waitForAnyPress();
		valueBack = light.getLightValue();
		Button.waitForAnyPress();

		Aligner aligner = new Aligner(SensorPort.S4,
				(valueFront + valueBack) / 2, valueFront > valueBack);

		aligner.align();

	}
}
