package Programs;

import RobotMovement.TrackSuspension;

public class Qualifying implements Program {
	private boolean running;
	private LineFollower follower;
	private TrackSuspension tracks;
	private Labyrinthright labyrinthright;
	private Labyrinthleft labyrinthleft;
	private boolean Labrinthright;

	public Qualifying(boolean LabrinthRight, boolean doFallback) {
		follower = new QualifyingFollower(doFallback);
		tracks = TrackSuspension.getInstance();
		Labrinthright = LabrinthRight;
		labyrinthright = new Labyrinthright();
		labyrinthleft = new Labyrinthleft();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			new Thread(follower).start();
			sleep(100);
			while (follower.isRunning()) {

			}
			if (Labrinthright) {
				new Thread(labyrinthright).start();
				sleep(100);
				while (labyrinthright.isRunning()) {

				}
			} else {
				new Thread(labyrinthleft).start();
				sleep(100);
				while (labyrinthleft.isRunning()) {

				}
			}
		}
		running = false;
	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void halt() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

}
