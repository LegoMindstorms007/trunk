import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;


public class TrackSuspension {
	private NXTRegulatedMotor left;
	private	NXTRegulatedMotor right;
	private final int ROTATIONTRANSLATION = 1;
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
	
	public void turnRight() {
		left.forward();
	}
	
	public void pivotAngleLeft(int angle) {
		
	}
	public void pivotAngleRight(int angle) {
		
	}
}
