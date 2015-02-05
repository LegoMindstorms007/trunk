package Test;

import lejos.nxt.Button;
import Programs.EndBoss;

public class EndbossTest {

	public static void main(String[] args) {
		EndBoss boss = new EndBoss();
		new Thread(boss).start();
		Button.waitForAnyPress();
		boss.halt();

	}

}
