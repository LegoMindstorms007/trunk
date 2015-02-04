package RobotMovement;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class SensorArm {
	public static final int MAXLEFT = 89;
	public static final int MAXRIGHT = -89;
	public static final int CENTER = 0;
	private NXTRegulatedMotor motor;
	private boolean tilting;

	public SensorArm() {
		motor = Motor.C;
	}

	public void setSpeed(int speed) {
		motor.setSpeed(speed);
	}

	public void turnArmLeft(int angle) {
		int turn = Math.min(MAXLEFT, motor.getTachoCount() + angle);
		motor.rotateTo(turn);
	}

	public void turnArmLeft(int angle, boolean immediateReturn) {
		int turn = Math.min(MAXLEFT, motor.getTachoCount() + angle);
		motor.rotateTo(turn, immediateReturn);
	}

	public void turnArmRight(int angle) {
		int turn = Math.max(MAXRIGHT, motor.getTachoCount() - angle);
		motor.rotateTo(turn);
	}

	public void turnArmRight(int angle, boolean immediateReturn) {
		int turn = Math.max(MAXRIGHT, motor.getTachoCount() - angle);
		motor.rotateTo(turn, immediateReturn);
	}

	public int getArmPosition() {
		return motor.getTachoCount();
	}

	public void turnToCenter() {
		motor.rotateTo(CENTER);
	}

	public void turnToCenter(boolean immediateReturn) {
		motor.rotateTo(CENTER, immediateReturn);
	}

	public void turnToPosition(int angle) {
		angle = Math.min(MAXLEFT, angle);
		angle = Math.max(MAXRIGHT, angle);
		motor.rotateTo(angle);
	}

	public void turnToPosition(int angle, boolean immediateReturn) {
		angle = Math.min(MAXLEFT, angle);
		angle = Math.max(MAXRIGHT, angle);
		motor.rotateTo(angle, immediateReturn);
	}

	public boolean isMoving() {
		return motor.isMoving();
	}

	public void tilt() {
		motor.rotateTo(MAXLEFT);
		motor.rotateTo(MAXRIGHT);
	}

	public void stop() {
		if (isMoving()) {
			motor.stop();
		}
	}

	public void shootLeft() {
		motor.rotateTo(110);
		turnToCenter();
	}

	public void shootRight() {
		motor.rotateTo(-110);
		turnToCenter();
	}

	public void waitForArm() {
		motor.waitComplete();
	}
}
