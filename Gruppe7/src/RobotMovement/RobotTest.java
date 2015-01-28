package RobotMovement;


public class RobotTest {
	public TrackSuspension suspension;
	public UltrasoundArm usArm;
	
	public RobotTest() {
		suspension = new TrackSuspension();
		usArm = new UltrasoundArm(null);
	}
}
