package Programs;


public class TurnTableFollower extends LineFollower {

	@Override
	protected void fallBack() {

		lineFinished = true;
	}

	@Override
	protected void getToBarcode() {
	}

	@Override
	protected void findLineStart() {
	}
}
