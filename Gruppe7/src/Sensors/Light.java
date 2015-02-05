package Sensors;

import lejos.nxt.ADSensorPort;
import Test.Demo;

public class Light extends lejos.nxt.LightSensor {

	public static Light instance;

	private Light(ADSensorPort port) {
		super(port);
	}

	public static Light getInstanceOf() {
		if (instance == null) {
			instance = new Light(Demo.LIGHT);
		}
		return instance;
	}

}
