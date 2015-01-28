package RobotMovement;

import lejos.nxt.SensorPort;

public class RobotTest {
	public TrackSuspension suspension;
	public UltrasoundArm usArm;

	public RobotTest() {
		suspension = new TrackSuspension();
<<<<<<< HEAD
		usArm = new UltrasoundArm(SensorPort.S3);
=======
		usArm = new UltrasoundArm(null);
>>>>>>> 12c2ddee5f3a72fe46388819d01b6eef42ad20f8
	}
}
