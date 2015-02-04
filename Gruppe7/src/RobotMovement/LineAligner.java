package RobotMovement;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class LineAligner {

	private static final int LINE_VALUE = 35;
	private static final int ARM_SPEED = 100;
	private LightSensor light;
	private SensorArm arm;
	private TrackSuspension track;

	public LineAligner(SensorPort lightPort) {
		light = new LightSensor(lightPort);
		arm = new SensorArm();
		track = TrackSuspension.getInstance();
	}

	/**
	 * this is fucking crazy, hope it works well...
	 */
	public void align() {
		track.backward(50);
		int delta = 90;

		while (arm.isMoving())
			;
		while (delta >= 5) {
			track.backward(15);
			int angle1 = checkAngle();
			track.forward(15);
			int angle2 = checkAngle();

			delta = angle2 - angle1;

			if (delta < 0)
				track.pivotAngleRight(-delta / 2);
			else
				track.pivotAngleLeft(delta / 2);
			track.waitForMotors();
		}
		track.forward(50);
		arm.turnToCenter();

	}

	private int checkAngle() {
		arm.turnArmLeft(90);
		arm.turnArmRight(90, true);
		int angle = 0;

		while (arm.isMoving()) {
			if (isLine()) {
				angle = arm.getArmPosition();
				arm.stop();
			}
		}
		return angle;
	}

	private boolean isLine() {
		return light.getLightValue() > LINE_VALUE;
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
