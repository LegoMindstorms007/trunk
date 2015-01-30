package Programs;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import Communication.BluetoothCommunication;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;

public class LiftDriving implements Program {

	private static final String LIFT = "Lift";
	private static final int GO_DOWN = 0;
	private static final int IS_DOWN = 1;
	private static final int CLOSE_CONNECTION = 2;
	private static final int GREENLIGHT = 40;
	private BluetoothCommunication com;
	private boolean running;
	private LightSensor light;
	private SensorArm arm;
	private TrackSuspension track;

	public LiftDriving(SensorPort portOfLightSensor) {
		light = new LightSensor(portOfLightSensor);
		arm = new SensorArm();
		track = new TrackSuspension();
		track.setSpeed(1000);
		arm.setSpeed(250);
	}

	@Override
	public void run() {
		running = true;
		while (running && !com.openConnection(LIFT))
			sleep(250);

		while (running && !isGreen())
			sleep(100);

		if (running)
			driveIntoLift();

		if (running)
			goDown();

		while (running && !canExit())
			sleep(100);

		if (running)
			driveOut();

		closeConnection();
	}

	@Override
	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	public boolean isGreen() {
		return light.getLightValue() >= GREENLIGHT;
	}

	private void driveIntoLift() {
		arm.turnArmRight(90);
		track.forward();

		// // redo this with push-sensors...
		// while (shouldDoThis) {
		// sleep(100);
		// }
		sleep(1000);
		track.stop();
	}

	private void driveOut() {
		arm.turnToCenter();
		track.forward();
		sleep(1000);
		track.stop();
	}

	/**
	 * moves the lift down
	 * 
	 * @return true
	 */
	public boolean goDown() {
		com.writeInt(GO_DOWN);
		return com.readBool();
	}

	/**
	 * returns if you can exit the lift
	 * 
	 * @return if the lift is on the bottom
	 */
	public boolean canExit() {
		com.writeInt(IS_DOWN);
		return com.readBool();
	}

	/**
	 * this method's name should be self explaining
	 */
	private void closeConnection() {
		com.writeInt(CLOSE_CONNECTION);
		com.closeConnection();
	}

	/**
	 * sleep method
	 * 
	 * @param milliseconds
	 *            milliseconds to sleep
	 */
	public void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
