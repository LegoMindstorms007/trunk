package Programs;

import lejos.nxt.SensorPort;

public class DownwardFollower extends LineFollower {

	public DownwardFollower(SensorPort portOfLightSensor,
			SensorPort portOfUsSensor) {
		super(portOfLightSensor, portOfUsSensor);
	}

	@Override
	protected void findLineStart() {
	}

	protected void getToBarcode() {
		track.backward(50);
		searchTrack();
		track.forward(50);
	}

}
