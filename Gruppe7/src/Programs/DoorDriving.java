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
		track.setSpeed(1000);

		// drive while door is not reached
		while (running && usSensor.getMeasurment() > 20 && !com.isConnected()) {
			sleep(10);
		}
		track.stop();

		// wait until brick is connected
		while (running && !com.isConnected()) {
			sleep(50);
		}

		// wait until door is open
		while (running && usSensor.getMeasurment() < 50) {
			sleep(10);
		}

		// drive through the door
		if (running)
			track.forward();
		sleep(6000);

		track.stop();

		// we are through the door, at least we hope so
		com.writeBool(true);

		// disconnect from server
		com.disconnect();

		running = false;
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
