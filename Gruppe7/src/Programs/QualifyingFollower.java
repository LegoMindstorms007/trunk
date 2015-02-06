package Programs;

public class QualifyingFollower extends LineFollower {

	/**
	 * Constructs a line follower
	 * 
	 * @param doFallback
	 *            if a fallback search should be done or not
	 */
	public QualifyingFollower(boolean doFallback) {
		super(doFallback);
	}

	protected void alignOnEnd() {
		track.turnRight(35);
		track.waitForMotors();
	}

	protected void getToBarcode() {
		if (running) {
			track.setSpeed(1000);
			track.forward();
			while (!isLine()) {
				sleep(10);
			}
			track.stop();
			track.forward(400);
			track.pivotAngleLeft(90);
			track.waitForMotors();
			track.forward(400);
		}
	}

	protected void findLineStart() {
		track.forward();
		track.setSpeed(MOVING_SPEED + deltaSpeed);

		while (running && !isLine()) {
			sleep(10);
		}

	}
}
