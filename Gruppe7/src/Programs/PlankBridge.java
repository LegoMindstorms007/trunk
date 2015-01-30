package Programs;

import lejos.nxt.SensorPort;

public class PlankBridge implements Program {
	// private LineFolower firstFollower;
	private LineFollower secondFollower;
	private BridgeDriving bridgeDriving;
	private boolean running;

	public PlankBridge(SensorPort lightSensorPort, SensorPort ultaSoundPort) {
		// firstFollower = new LineFolower(SensorPort.S4, SensorPort.S3);
		bridgeDriving = new BridgeDriving(lightSensorPort, ultaSoundPort);
		secondFollower = new LineFollower(lightSensorPort, ultaSoundPort);
		running = true;
	}

	@Override
	public void run() {
		new Thread(bridgeDriving).start();
		while (bridgeDriving.isRunning()) {
			sleep(500);
		}
		new Thread(secondFollower).start();
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
