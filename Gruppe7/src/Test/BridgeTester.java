package Test;
import Programs.PlankBridge;
import lejos.nxt.Button;


public class BridgeTester {
	public static void main(String[] args) {
		PlankBridge bridge = new PlankBridge();
		new Thread(bridge).start();
		Button.waitForAnyPress();
		bridge.halt();
	}
}
