package Test;

 import Programs.Labyrinth1;
	import lejos.nxt.Button;
	
	public class Demo2 {
		public static void main(String[] args) {
			Labyrinth1 labydrive = new Labyrinth1();
			new Thread(labydrive).start();
			Button.waitForAnyPress();
		}
	
	}
	

/*import Programs.Labyrinth3;
import lejos.nxt.Button;

public class Demo2 {
	public static void main(String[] args) {
		Labyrinth3 labydrive = new Labyrinth3();
		new Thread(labydrive).start();
		Button.waitForAnyPress();
	}

}*/