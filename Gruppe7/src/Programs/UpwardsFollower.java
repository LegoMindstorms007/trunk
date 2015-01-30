package Programs;

import lejos.nxt.SensorPort;

public class UpwardsFollower extends LineFolower {
	protected static final int LINE_VALUE = 40;

	public UpwardsFollower(SensorPort portOfLightSensor,
			SensorPort portOfUsSensor) {
		super(portOfLightSensor, portOfUsSensor);
	}

}
