package Programs;

import Communication.TurnTable;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;
import lejos.nxt.SensorPort;

public class TurnTableProgram implements Program {
	private UpwardsFollower upwards;
	private boolean running;
	UltrasoundSensor us;
	TrackSuspension tracks;
	private TurnTable table;
	public TurnTableProgram(SensorPort  portOfLightSensor, SensorPort portOfUsSensor) {
		upwards = new UpwardsFollower(portOfLightSensor, portOfUsSensor);
		us = new UltrasoundSensor(portOfUsSensor);
		tracks = new TrackSuspension();
		table = new TurnTable();
		running = true;
	}
	@Override
	public void run() {
		new Thread(upwards).start();
		while(upwards.isRunning() && running) {
			sleep(100);
		}
		/*while(!table.connect() && running) {
			table.connect();
			sleep(1000);	
		} */
		while(us.getMeasurment() >= 6 && running) {
			if(tracks.motorsMoving()) {
				tracks.forward();
			}
		}
		tracks.stop();
		halt();
//		table.use();
	}

	@Override
	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
