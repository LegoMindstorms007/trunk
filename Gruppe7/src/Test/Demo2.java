package Test;

import Programs.Labyrinthright;

import Programs.Program;


/*public class Demo2 {

	private static Program current = null;

	public static void main(String[] args) {
		current = new LineFollower();
		new Thread(current).start();
	}
}*/
public class Demo2 {

	private static Program current = null;

	public static void main(String[] args) {
		current = new Labyrinthright();
		new Thread(current).start();
	}
}