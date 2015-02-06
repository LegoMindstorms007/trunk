package Test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import Programs.Qualifying;

public class QualifyingS {

	public static void main(String args[]) {
		boolean goRight = true;
		boolean doFallback = true;

		int buttonPushed = 0;

		while (buttonPushed != Button.ID_ENTER) {
			LCD.clear();
			LCD.drawString("Should do", 0, 0);
			LCD.drawString("Fallback Search?", 0, 1);

			if (doFallback)
				LCD.drawString("Yes", 0, 4);
			else
				LCD.drawString("No", 0, 4);

			buttonPushed = Button.waitForAnyPress();

			if (buttonPushed == Button.ID_LEFT
					|| buttonPushed == Button.ID_RIGHT)
				doFallback = !doFallback;

		}

		buttonPushed = 0;

		while (buttonPushed != Button.ID_ENTER) {
			LCD.clear();
			LCD.drawString("Labyrinth direction:", 0, 0);

			if (goRight)
				LCD.drawString("Right", 0, 4);
			else
				LCD.drawString("Left", 0, 4);

			buttonPushed = Button.waitForAnyPress();

			if (buttonPushed == Button.ID_LEFT)
				goRight = false;
			else if (buttonPushed == Button.ID_RIGHT)
				goRight = true;

		}

		Qualifying quali = new Qualifying(goRight, doFallback);

		new Thread(quali).start();

		while (!quali.isRunning()) {
			sleep(100);
		}

		while (quali.isRunning()) {
			if (Button.waitForAnyPress(1000) > 0)
				quali.halt();
		}
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
