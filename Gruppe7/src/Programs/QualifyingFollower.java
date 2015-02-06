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
		track.turnRight(25);
	}

	protected void getToBarcode() {
		if (running) {
			track.setSpeed(1000);
			track.forward();
			while (!isLine()) {
				sleep(10);
			}
			track.stop();
			track.forward(200);
			track.turnLeft(90);
			track.forward(200);
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
