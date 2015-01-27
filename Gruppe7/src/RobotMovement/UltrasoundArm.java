package RobotMovement;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


public class UltrasoundArm {
	private NXTRegulatedMotor motor;
	final int LEFTPOSITION = 0;
	final int RIGHTPOSITION = 0;
	final int CENTERPOSITION = 0;
	public UltrasoundArm() {
		motor = Motor.C;
	}
	
	public void turnToLeft() {
		motor.rotateTo(LEFTPOSITION);
	}
	
	public void TurnToRight() {
		motor.rotateTo(RIGHTPOSITION);
	}
	
	public void center() {
		motor.rotateTo(CENTERPOSITION);
	}
}
