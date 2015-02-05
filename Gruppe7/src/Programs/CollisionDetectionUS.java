package Programs;

import lejos.nxt.LCD;
import Sensors.UltrasoundSensor;

public class CollisionDetectionUS implements Program{
	private boolean running;
	private boolean possibleCollsion;
	private boolean collided;
	private UltrasoundSensor us;
	
	public CollisionDetectionUS() {
		running = true;
		us = UltrasoundSensor.getInstanceOf();
	} 
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
			LCD.drawString("Distance: " +distance, 0, 1);
			if(distance < 20) {
				possibleCollsion = true; 
				checking = false;
				LCD.drawString("Possible Collision", 0,1);
			}
		}
		sleep(500);
		int nextdistance = us.getAverageMeasurement(10);
		if(nextdistance < 30 && nextdistance >= distance) {
			collided = true;
			LCD.clear();
			LCD.drawString("Collision Detected", 0,1);
			while(collided && running) {
				if(us.getAverageMeasurement(10) >= nextdistance) {
					collided = false;
					possibleCollsion = false;
				}
				sleep(10);
			}
			LCD.clear();
		} else {
			//Wall
			possibleCollsion = false;
			LCD.clear();
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
