package RobotMovement;


public class LightSweeper implements Runnable {
	boolean running;
	private SensorArm arm;
	
	public LightSweeper() {
		running = true;
		arm = new SensorArm();
	}
	@Override
	public void run() {
		while(running) {
			arm.tilt();
		}
		
	}
	
	public void halt() {
		running = false;
	}
}
