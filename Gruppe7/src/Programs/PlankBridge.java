package Programs;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import RobotMovement.TrackSuspension;

public class PlankBridge implements Program {
	private UpwardsFollower firstFollower;
	private PlankBridgeDriving bridgeDriving;
	private boolean running;
	private TrackSuspension track = TrackSuspension.getInstance();

	public PlankBridge() {
		firstFollower = new UpwardsFollower();
		bridgeDriving = new PlankBridgeDriving();
		running = true;
	}

	@Override
	public void run() {
		new Thread(firstFollower).start();
		LCD.drawString("FirstFollower", 0, 1);
		sleep(200);
		while (firstFollower.isRunning() && running) {
			if (Button.waitForAnyPress(100) > 0) {
				firstFollower.halt();
			}
		}
		LCD.clear();
		LCD.drawString("BridgeDriving", 0, 1);
		new Thread(bridgeDriving).start();
		sleep(200);
		while (bridgeDriving.isRunning() && running) {
			if (Button.waitForAnyPress(100) > 0) {
				bridgeDriving.halt();
			}
		}
		LCD.clear();
		LCD.drawString("SecondFollower", 0, 1);
		track.setSpeed(2000);
		track.forward(200);
		track.stop();
		sleep(100);
		halt();
		track.stop();
	}

	@Override
	public void halt() {
		running = false;

	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
