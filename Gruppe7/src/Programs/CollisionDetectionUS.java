package Programs;

import Sensors.UltrasoundSensor;

public class CollisionDetectionUS implements Program{
	private boolean running;
	private boolean possibleCollsion;
	private boolean collided;
	private UltrasoundSensor us = UltrasoundSensor.getInstanceOf();
	@Override
	public void run() {
		while(running) {
			checkForCollision();
		}
		
	}

	public boolean possibleCollision() {
		return possibleCollsion;
		
	}
	
	private void checkForCollision() {
		boolean checking = true;
		int distance = 0;
		while(checking && running) {
			distance = us.getAverageMeasurement(10);
			if(distance < 10) {
				possibleCollsion = true; 
				checking = false;
			}
		}
		sleep(500);
		if(us.getAverageMeasurement(10) <= distance) {
			collided = true;
		} else {
			possibleCollsion = false;
		}
		while(collided && running) {
			if(us.getAverageMeasurement(10) > distance) {
				collided = false;
				possibleCollsion = false;
			}
			sleep(10);
		}
		
	}
	
	private void sleep(int i) {
	try {
		Thread.sleep(i);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

	public boolean collided() {
		return collided;
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
