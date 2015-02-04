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

}
