package Programs;

import lejos.nxt.SensorPort;

public class TurnTableFollower extends LineFollower {

	public TurnTableFollower(SensorPort portOfLightSensor,
			SensorPort portOfUsSensor) {
		super(portOfLightSensor, portOfUsSensor);
	}
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
