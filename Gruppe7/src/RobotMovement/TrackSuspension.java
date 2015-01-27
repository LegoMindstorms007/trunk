package RobotMovement;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


public class TrackSuspension {
	private NXTRegulatedMotor left;
	private	NXTRegulatedMotor right;
	private final float ROTATIONTRANSLATION = 5.3f;
	public TrackSuspension() {
		left = Motor.B;
		right = Motor.A;
	}
	
	public void setSpeed(int speed) {
		left.setSpeed(speed); 
		right.setSpeed(speed);
	}
	public void forward() {
		left.forward();
		right.forward();
	}
	
	public void backward() {
		left.backward();
		right.backward();
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
		left.stop();
		right.stop();
	}
	
	public void turnLeft() {
		right.forward();
	}
	public void turnLeft(int angle) {
		right.rotate((int)(angle * ROTATIONTRANSLATION), true);
	}
	
	public void turnRight() {
		left.forward();
	}
	
	public void turnRight(int angle) {
		left.rotate((int)(angle * ROTATIONTRANSLATION), true);
	}
	
	public void pivotAngleLeft(int angle) {
		left.rotate((int)(-angle * ROTATIONTRANSLATION), true);
		right.rotate((int)(angle * ROTATIONTRANSLATION), true);
	}
	public void pivotAngleRight(int angle) {
		left.rotate((int)(angle * ROTATIONTRANSLATION * 1.1), true);
		right.rotate((int)(-angle * ROTATIONTRANSLATION), true);
	}
	
	public void waitForMotors() {
		left.waitComplete();
		right.waitComplete();
	}
	
	public boolean motorsMoving() {
		return left.isMoving() || right.isMoving();
	}
}
