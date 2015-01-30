package Test;
import Programs.BridgeDriving;
import Programs.PlankBridge;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;


public class BridgeTester {
	public static void main(String[] args) {
		PlankBridge bridge = new PlankBridge(SensorPort.S4, SensorPort.S3);
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt(); 
/*		BridgeDriving bridge = new BridgeDriving(SensorPort.S4, SensorPort.S3);
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt(); */
	}
}
