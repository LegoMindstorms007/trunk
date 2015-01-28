package RobotMovement;

import lejos.nxt.SensorPort;

public class RobotTest {
	public TrackSuspension suspension;
	public UltrasoundArm usArm;

	public RobotTest() {
		suspension = new TrackSuspension();
		usArm = new UltrasoundArm(SensorPort.S3);
		usArm = new UltrasoundArm(null);
	}
}
