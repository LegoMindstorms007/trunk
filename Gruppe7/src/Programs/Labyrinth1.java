
package Programs;

import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.nxt.TouchSensor;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;

public class Labyrinth1 implements Runnable {

	private static final int MOVING_SPEED = 520;
	//private static final int ARM_SPEED = 100;
	private SensorArm sArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;
	//private int amoutCollision = 0;
	private boolean running;
	TouchSensor bump1;
	TouchSensor bump2;
	LightSensor light;

	public Labyrinth1(){
		sArm = new SensorArm();
		usSensor = new UltrasoundSensor(SensorPort.S3);
		movement = new TrackSuspension();
		movement.setSpeed(MOVING_SPEED);
		bump1 = new TouchSensor(SensorPort.S1);
		bump2 = new TouchSensor(SensorPort.S2);
		light= new LightSensor(SensorPort.S4);
		//movement.pivotAngleRight(90+12);
		//movement.forward(10);
		//movement.pivotAngleLeft(90+12);	
	}

	//@Override
	public void run(){
		running = true;
		sArm.turnArmRight(90);
		movement.forward(100);
		while (running) {
			if(!isLine()){
				if(isRightHolz() &&( bump1.isPressed() || bump2.isPressed())){// 
					movement.stop();
					searchHolzByCollision();
				}
				else if(isRightHolz() && usSensor.getMeasurment()>=8 && usSensor.getMeasurment()<=11){//follow  holz
					movement.forward();
				}
				else if(isRightHolz() && usSensor.getMeasurment()>11){ //Adjust the distance with the wall 
					movement.pivotAngleRight(5);
					movement.waitForMotors();
					movement.forward();
				}
				else if(isRightHolz() && usSensor.getMeasurment()<8){// Adjust the distance with the wall 
					movement.pivotAngleLeft(10);
					movement.waitForMotors();
					movement.forward();
				}
				else if(!isRightHolz() && usSensor.getMeasurment()>=50){//luft
					movement.stop();
					turntoHolz();
				}
				else if(!isRightHolz() && usSensor.getMeasurment()<50 && usSensor.getMeasurment()>35){
					movement.stop();
					searchHolz();
				}
			}
			else{
				movement.stop();
				sArm.turnArmLeft(90);
				running=false;
			}
			
		}
	}
	private boolean isLine() {
		return light.getLightValue() >= 35;
	}

	private boolean isRightHolz(){
		return usSensor.getMeasurment()<=35;
	}
	
	private void searchHolz(){
		movement.pivotAngleLeft(10);
		movement.waitForMotors();
		movement.forward(50);
			
	}
	private void turntoHolz(){
		movement.forward(100);
		movement.pivotAngleRight(90);
		movement.waitForMotors();
		movement.forward(120);
		
	}
	
	private void searchHolzByCollision(){
		movement.backward(80);
		movement.pivotAngleLeft(90);
		movement.waitForMotors();
		movement.forward(100);
		}
		

}


			

