package RobotMovement;

public class Compass {
	public final int FORWARD = 180;
	public final int BACKWARD = 0;
	private int direction;
	public Compass() {
		direction = FORWARD;
	}
	
	public void turnLeft(int angle) {
		direction += angle;
		direction = direction % 360;
	}
	
	public void turnRight(int angle) {
		direction -= angle;
		direction = direction % 360;
		if(direction < 0) {
			direction += 360;
		}
	}
	
	public boolean facingForward() {
		if(direction < 270 && direction > 90) {
			return true;
		}
		else return false;
	}
	
	public int getDirection() {
		return direction;
	}
}
