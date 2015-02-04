package RobotMovement;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class TrackSuspension {
	private NXTRegulatedMotor left;
	private NXTRegulatedMotor right;
	private final float ROTATIONTRANSLATION = 5.3f;
	private int speed;
	private static TrackSuspension instance = null;
	public static TrackSuspension getInstance() {
		if(instance == null) {
			instance = new TrackSuspension();
		}
		return instance;
	}
	private TrackSuspension() {
		left = Motor.B;
		right = Motor.A;
		speed = Motor.B.getSpeed();
		Motor.A.setSpeed(speed);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
		left.setSpeed(speed);
		right.setSpeed(speed);
	}

	public void forward() {
		left.forward();
		right.forward();
	}

	public void forward(int angle) {
		left.rotate((int) (angle * ROTATIONTRANSLATION), true);
		right.rotate((int) (angle * ROTATIONTRANSLATION), true);
		waitForMotors();
	}

	public void backward() {
		left.backward();
		right.backward();
	}

	public void backward(int angle) {
		left.rotate((int) (-angle * ROTATIONTRANSLATION), true);
		right.rotate((int) (-angle * ROTATIONTRANSLATION), true);
		waitForMotors();
	}

	public void pivotLeft() {
		right.forward();
		left.backward();
	}

	public void pivotRight() {
		right.backward();
		left.backward();
	}

	public void stop() {
		left.stop(true);
		right.stop(true);
		waitForMotors();
	}

	public void turnLeft() {
		left.stop(true);
		right.forward();
	}

	public void turnLeftBackward() {
		right.backward();
	}

	public void turnLeft(int angle) {
		right.rotate((int) (angle * ROTATIONTRANSLATION), true);
	}

	public void turnRight() {
		right.stop(true);
		left.forward();
	}

	public void turnRight(int angle) {
		left.rotate((int) (angle * ROTATIONTRANSLATION), true);
	}

	public void turnRightBackward() {
		left.backward();
	}

	public void pivotAngleLeft(int angle) {
		left.rotate((int) (-angle * ROTATIONTRANSLATION), true);
		right.rotate((int) (angle * ROTATIONTRANSLATION), true);
	}

	public void pivotAngleRight(int angle) {
		left.rotate((int) (angle * ROTATIONTRANSLATION), true);
		right.rotate((int) (-angle * ROTATIONTRANSLATION), true);
	}

	public void waitForMotors() {
		left.waitComplete();
		right.waitComplete();
	}

	public boolean motorsMoving() {
		return left.isMoving() || right.isMoving();
	}

	public void setSpeedLeft(int speed) {
		left.setSpeed(speed);
	}

	public void setSpeedRight(int speed) {
		right.setSpeed(speed);
	}

	public void manipulateLeft(int deltaSpeed) {
		left.setSpeed(Math.min(0, speed + deltaSpeed));
	}

	public void manipulateRight(int deltaSpeed) {
		right.setSpeed(Math.min(0, speed + deltaSpeed));
	}

	public void equalSpeed() {
		right.setSpeed(speed);
		left.setSpeed(speed);
	}
}
