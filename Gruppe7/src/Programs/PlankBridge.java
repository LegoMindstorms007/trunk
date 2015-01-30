package Programs;

import RobotMovement.TrackSuspension;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class PlankBridge implements Program {
	private UpwardsFollower firstFollower;
	private LineFolower secondFollower;
	private BridgeDriving bridgeDriving;
	private boolean running;
	private TrackSuspension track = new TrackSuspension();
	public PlankBridge() {
		firstFollower = new UpwardsFollower(SensorPort.S4, SensorPort.S3);
		bridgeDriving = new BridgeDriving();
		secondFollower = new LineFolower(SensorPort.S4, SensorPort.S3);
		running = true;
	}
	@Override
	public void run() {
			new Thread(firstFollower).start();
			LCD.drawString("FirstFollower", 0, 1);
			sleep(200);
			while(firstFollower.isRunning()) {
				if (Button.waitForAnyPress(100) > 0) {
					firstFollower.halt();
				}
			}
			LCD.drawString("", 0, 1);
			track.setSpeed(2000);
			track.forward(100);
			track.stop();
			track.setSpeed(200);
			LCD.drawString("BridgeDriving", 0, 1);
			new Thread(bridgeDriving).start();
			sleep(200);
			while(bridgeDriving.isRunning()) {
				if (Button.waitForAnyPress(100) > 0) {
					bridgeDriving.halt();
				}
			}
			LCD.drawString("", 0, 1);
			LCD.drawString("SecondFollower", 0, 1);
			track.setSpeed(2000);
			track.forward(50);
			track.stop();
			new Thread(secondFollower).start();
			sleep(100);
			while(secondFollower.isRunning()){
				
			}
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
