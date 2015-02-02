package Test;

import RobotMovement.SensorArm;

public class ShootTest {

	public static void main(String args[]) {

		SensorArm arm = new SensorArm();

		arm.turnToPosition(110);
		arm.turnToPosition(-110);

		arm.shootLeft();
		arm.shootRight();
	}

}
