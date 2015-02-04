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

	@Override
	protected void alignOnEnd() {
		/*
		 * track.forward(100); track.pivotAngleRight(180);
		 * track.waitForMotors(); while (!searchTrack()) { track.forward(10); }
		 * track.forward(30); searchTrack(); track.pivotAngleLeft(180);
		 * track.waitForMotors(); track.forward(300);
		 */
	}

}
