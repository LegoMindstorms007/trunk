package Programs;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class ReachBridge {

	private static final int numPrograms = 2;

	public static void main(String[] args) {
		LightSensor light = new LightSensor(SensorPort.S4);

		int program = 0;
		UpwardsFollower follower = new UpwardsFollower();
		BridgeDriving bridge = new BridgeDriving();
		Program current = null;

		while (program < numPrograms) {
			switch (program) {
			case 0:
				current = follower;
				Thread followerthread = new Thread(follower);
				followerthread.start();
				while (followerthread.isAlive()) {
					if (!follower.isLine()) {
						break;
					}
				}
				break;

			case 1:
				current = bridge;
				Thread bridgethread = new Thread(bridge);
				bridgethread.start();
				while (bridgethread.isAlive()) {
					if (follower.isLine()) {
						break;
					}
				}
				break;
			}

			// new Thread(current).start();

			Button.waitForAnyPress(100);
			boolean buttonPressed = false;
			while (current.isRunning()) {
				if (Button.waitForAnyPress(100) > 0) {
					buttonPressed = true;
					current.halt();
				}
			}
			program++;
			program %= 2;
		}
	}
}
