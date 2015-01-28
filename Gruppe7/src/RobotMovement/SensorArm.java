package RobotMovement;

import lejos.nxt.Motor;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;

public class SensorArm {
	private static final int MAXLEFT = 90;
	private static final int MAXRIGHT = -90;
	private static final int CENTER = 0;
	private NXTRegulatedMotor motor;
	public SensorArm() {
		motor = Motor.C;
	}
	
	public void turnArmLeft(int angle) {
		int turn = Math.max(MAXLEFT, motor.getTachoCount() + angle);
		motor.rotateTo(turn);
	}
	
	public void turnArmRight(int angle) {
		int turn = Math.min(MAXRIGHT, motor.getTachoCount() - angle);
		motor.rotateTo(turn);
	}
	
	public int getArmPosition() {
		return motor.getTachoCount();
	}
	
	public void turnToCenter() {
		motor.rotateTo(CENTER);
	}
	
	public void turnToPosition(int angle) {
		angle = Math.min(MAXLEFT, angle);
		angle = Math.max(MAXRIGHT, angle);
		motor.rotate(angle);
	}
}
