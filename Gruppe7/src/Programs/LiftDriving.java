package Programs;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import Communication.BluetoothCommunication;
import RobotMovement.Aligner;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.BumpSensor;
import Sensors.Light;

/**
 * program for driving a lift
 * 
 * @author Dominik Muth
 * 
 */
public class LiftDriving implements Program {

	private static final String LIFT = "Lift";
	private static final String LIFT_ADDRESS = "00165309448C";
	private static final int GO_DOWN = 0;
	private static final int IS_DOWN = 1;
	private static final int CLOSE_CONNECTION = 2;
	private static final int IS_UP = 3;
	// private static final int GREENLIGHT = 35;
	private static final int RIFT_LIMIT = 35;
	private BluetoothCommunication com;
	private boolean running;
	private LightSensor light;
	private SensorArm arm;
	private TrackSuspension track;
	private BumpSensor bump;
	private Aligner aligner;

	/**
	 * constructs a new lift driver
	 * 
	 * @param portOfLightSensor
	 *            self explaining
	 * @param leftBumpSensor
	 *            self explaining
	 * @param rightBumpSensor
	 *            self explaining
	 */
	public LiftDriving() {

		aligner = new Aligner(30, true, false);
		arm = SensorArm.getInstance();
		bump = BumpSensor.getInstanceOf();
		track = TrackSuspension.getInstance();
		track.setSpeed(1000);
		arm.setSpeed(250);
		com = new BluetoothCommunication();
		light = Light.getInstanceOf();
	}

	@Override
	public void run() {
		running = true;
		com.connect(LIFT, LIFT_ADDRESS);

		alignRobotOnPlate();

		track.stop();

		// sort of join, tough not blocking -> checking of running variable is
		// possible
		while (running && !com.isConnected()) {
			sleep(50);
			LCD.drawString("Waiting for connection", 0, 1);
		}
		LCD.drawString("Connected", 0, 1);

		while (running && !isGreen()) {
			sleep(50);
			LCD.drawString("Waiting for green panel", 0, 1);
		}

		if (running) {
			LCD.drawString("Driving into lift", 0, 1);
			driveIntoLift();
		}

		if (running) {
			LCD.drawString("Going down", 0, 1);
			goDown();
		}

		while (running && !canExit()) {
			LCD.drawString("Waiting for fift", 0, 1);
			sleep(50);
		}

		if (running) {
			LCD.drawString("Driving out of lift", 0, 1);
			driveOut();
		}

		closeConnection();

		// halt connector if this program is premature interrupted via halt()
		com.halt();
		running = false;
	}

	private void alignRobotOnPlate() {
		aligner.align();

		light.setFloodlight(true);

		track.forward(200);
		// turn right
		track.pivotAngleRight(90);
		track.waitForMotors();

		// find rift
		track.setSpeed(400);
		while (running && !isRift()) {
			track.forward();
		}
		track.stop();
		// back up a little
		while (running && isRift()) {
			track.backward();
		}
		track.backward(25);
		track.stop();

		// turn back left
		track.pivotAngleLeft(90);
		track.waitForMotors();

		// should be centered now
		track.setSpeed(1000);
	}

	private boolean isRift() {
		return light.getLightValue() < RIFT_LIMIT;
	}

	@Override
	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	/**
	 * 
	 * @return whether the panel is green or not
	 */
	public boolean isGreen() {
		// return light.getLightValue() >= GREENLIGHT;
		com.writeInt(IS_UP);
		return com.readBool();
	}

	private void driveIntoLift() {
		arm.turnArmLeft(90);
		track.forward();

		while (!bump.touchedFront()) {
			sleep(100);
		}
		track.stop();
		track.backward(30);
	}

	private void driveOut() {
		arm.turnToCenter();
		track.forward();
		sleep(2000);
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
		com.disconnect();
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
