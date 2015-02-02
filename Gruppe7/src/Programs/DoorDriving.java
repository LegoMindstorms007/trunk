package Programs;

import lejos.nxt.SensorPort;
import Communication.BluetoothCommunication;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

public class DoorDriving implements Program {

	private static final String DOOR = "TestName";
	private UltrasoundSensor usSensor;
	private SensorArm arm;
	private BluetoothCommunication com;
	private TrackSuspension track;
	private boolean running;

	public DoorDriving(SensorPort portOfUltrasoundSensor) {
		usSensor = new UltrasoundSensor(portOfUltrasoundSensor);
		arm = new SensorArm();
		com = new BluetoothCommunication();
		track = new TrackSuspension();
	}

	@Override
	public void run() {
		running = true;
		com.connect(DOOR);
		arm.turnToCenter();
		track.forward();

		while (running && usSensor.getMeasurment() > 10) {
			sleep(10);
		}

		track.stop();

		while (running && !com.isConnected()) {
			sleep(50);
		}

		sleep(1000);
		if (running)
			track.forward();
		sleep(4000);

		// we are through the door, at least we hope so
		com.writeBool(true);

		com.disconnect();
		track.stop();
	}

	@Override
	public void halt() {
		running = false;
		track.stop();
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
