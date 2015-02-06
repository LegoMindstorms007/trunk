package Programs;

public class DownwardFollower extends LineFollower {

	public DownwardFollower() {
		super(-25);
	}

	@Override
	protected void findLineStart() {
	}

	@Override
	protected void alignOnEnd() {
		track.setSpeed(2000);
		track.forward(300);
		// track.pivotAngleLeft(35);
		// track.waitForMotors();
		/*
		 * track.forward(100); track.pivotAngleRight(180);
		 * track.waitForMotors(); while (!searchTrack()) { track.forward(10); }
		 * track.forward(30); searchTrack(); track.pivotAngleLeft(180);
		 * track.waitForMotors(); track.forward(300);
		 */
	}

}
