package Test;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;

import Programs.Labyrinth;

import Programs.Program;


public class Demo2 {
	private static final SensorPort BUMP_RIGHT = SensorPort.S1;
	private static final SensorPort BUMP_LEFT = SensorPort.S2;
	private static final SensorPort ULTRA_SOUND = SensorPort.S3;
	private static final SensorPort LIGHT = SensorPort.S4;
	private static Program current = null;

	public static void main(String[] args) {
		current = new Labyrinth(LIGHT, ULTRA_SOUND, BUMP_LEFT,BUMP_RIGHT);
		new Thread(current).start();
	}
}
