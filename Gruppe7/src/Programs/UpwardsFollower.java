package Programs;

import lejos.nxt.SensorPort;

public class UpwardsFollower extends LineFollower {
	protected static final int LINE_VALUE = 40;

	public UpwardsFollower(SensorPort portOfLightSensor,
			SensorPort portOfUsSensor) {
		super(portOfLightSensor, portOfUsSensor);
	}
	@Override
	protected void findLineStart() {
	}
	@Override
	protected void fallBackSearch() {
		lineFinished = true;
	}
	protected void getToBarcode() {
		track.setSpeed(2000);
		track.forward(100);
		track.stop();
	}
}
