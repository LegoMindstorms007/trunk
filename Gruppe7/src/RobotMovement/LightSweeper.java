package RobotMovement;

public class LightSweeper implements Runnable {
	boolean running;
	boolean moving;
	private SensorArm arm;

	public LightSweeper() {
		arm = SensorArm.getInstance();
	}

	@Override
	public void run() {
		running = true;
		moving = true;
		while (running) {
			while (running && moving) {
				arm.turnToPosition(SensorArm.MAXLEFT,true );
				while(arm.isMoving() && moving) {
					sleep(10);
				}
				arm.turnToPosition(SensorArm.MAXRIGHT,true );
				while(arm.isMoving() && moving) {
					sleep(10);
				}
			}
			sleep(200);
		}
	}

	public void halt() {
		running = false;
	}

	public void stopSweeping() {
		moving = false;
	}

	public void startSweeping() {
		moving = true;
	}

	public boolean isSweeping() {
		return moving;
	}
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
