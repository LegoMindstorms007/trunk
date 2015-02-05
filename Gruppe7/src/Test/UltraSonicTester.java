package Test;

import Sensors.UltrasoundSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.RangeReading;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.RangeFeatureDetector;

public class UltraSonicTester {
	private static RangeFeatureDetector sensor;
	private static final int MAXDISTANCE = 255;
	public static void main(String[] args) {
		/*RConsole.openBluetooth(0);
		Start start = new Start();
		new Thread(start).start();
		Button.waitForAnyPress();
		start.halt();*/
		UltrasoundSensor us = UltrasoundSensor.getInstanceOf();
		boolean running = true;
		while(running) {
			int position = us.getAverageMeasurement(5);
			LCD.drawString(String.valueOf(position),0,1);
			if (Button.waitForAnyPress(100) > 0) {
				running = false;
			}
			LCD.clear();
			sleep(100);
		}
	/*	BridgeDriving bridge = new BridgeDriving();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();*/

	}
	
	public static int getMeasurment() {
		Feature scan = sensor.scan();
		RangeReading fdscan = scan != null ? scan.getRangeReading() : null;

		return fdscan != null ? (int) fdscan.getRange() : MAXDISTANCE;
	}
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
