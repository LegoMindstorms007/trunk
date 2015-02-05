package Test;

import Programs.Labyrinthleft;

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
		current = new Labyrinthleft();
		new Thread(current).start();
	}
}