package Programs;

import RobotMovement.TrackSuspension;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class PlankBridge implements Program {
	private UpwardsFollower firstFollower;
	private  DownwardFollower secondFollower;
	private PlankBridgeDriving bridgeDriving;
	private LightSensor light;
	private boolean running;
	private TrackSuspension track = new TrackSuspension();
	public PlankBridge(SensorPort lightSensorPort, SensorPort ultaSoundPort) {
		firstFollower = new UpwardsFollower(lightSensorPort, ultaSoundPort);
		bridgeDriving = new PlankBridgeDriving(lightSensorPort, ultaSoundPort);
		secondFollower = new DownwardFollower(lightSensorPort, ultaSoundPort);
		light = new LightSensor(lightSensorPort);
		running = true;
	}
	@Override
	public void run() {
		while(running) {
			new Thread(firstFollower).start();
			LCD.drawString("FirstFollower", 0, 1);
			sleep(200);
			while(firstFollower.isRunning() && running) {
				if (Button.waitForAnyPress(100) > 0) {
					firstFollower.halt();
				}
			}
			LCD.drawString("", 0, 1);
			LCD.drawString("BridgeDriving", 0, 1);
			new Thread(bridgeDriving).start();
			sleep(200);
			while(bridgeDriving.isRunning() && running) {
				if (Button.waitForAnyPress(100) > 0) {
					bridgeDriving.halt();
				}
			}
			LCD.drawString("", 0, 1);
			LCD.drawString("SecondFollower", 0, 1);
			track.setSpeed(2000);
			track.forward(100);
			track.stop();
			
			new Thread(secondFollower).start();
			sleep(100);
			while(secondFollower.isRunning() && running){
				
			}
			while(light.getLightValue() <= 35 && running) {
				if(!track.motorsMoving()) {
					track.forward();
				}
			}
			track.stop();
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
