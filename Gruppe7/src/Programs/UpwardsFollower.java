package Programs;


public class UpwardsFollower extends LineFollower {
	protected static final int LINE_VALUE = 37;

	@Override
	protected void findLineStart() {
	}

	@Override
	protected void fallBack() {
		lineFinished = true;
	}

	protected void getToBarcode() {
		track.setSpeed(2000);
		track.forward(100);
		track.stop();
	}
}
