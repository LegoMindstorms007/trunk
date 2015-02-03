
package Programs;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import RobotMovement.SensorArm;
import RobotMovement.TrackSuspension;
import Sensors.UltrasoundSensor;
import lejos.nxt.LightSensor;

public class Labyrinth implements Runnable {

	private static final int MOVING_SPEED = 650;
	//private static final int ARM_SPEED = 100;
	private SensorArm sArm;
	private UltrasoundSensor usSensor;
	private TrackSuspension movement;
//	private int amoutMovement = 0;
	//private int amoutCollision = 0;
	private boolean running;
	TouchSensor bump1;
	TouchSensor bump2;
	LightSensor light;

	public Labyrinth(){
		sArm = new SensorArm();
		//sArm.setSpeed(150);
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
		movement.forward(40);
		while (running) {	
			if(!isLine()){
				if(bump1.isPressed() || bump2.isPressed()){
					searchHolzByCollision();
				}
				else if(isRightHolz()){
					if( usSensor.getMeasurment()>=8 && usSensor.getMeasurment()<=15){// follow the holz
						movement.forward();
					}
					/*else if( usSensor.getMeasurment()>12 && usSensor.getMeasurment()<=15){//adjust
						movement.pivotAngleRight(5);
						movement.waitForMotors();
						movement.forward(50);
					}*/
					else if(usSensor.getMeasurment()<8){//adjust 
						movement.backward(60);
						movement.waitForMotors();
						movement.pivotAngleLeft(15);
						movement.waitForMotors();
						movement.forward(50);
					}
					else if( usSensor.getMeasurment()>15 &&  usSensor.getMeasurment()<=23 ){//adjust
						movement.pivotAngleRight(5);
						movement.waitForMotors();
						movement.forward(50);
					}
					else if(usSensor.getMeasurment()>23){
						movement.pivotAngleRight(15);
						movement.waitForMotors();
						movement.forward(50);
					}
				}
				else if(!isRightHolz()) {// luft  turn to right
					turntoHolz();
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
		return usSensor.getMeasurment()<=31;
	}
	
	/*private void searchHolz(){
		movement.pivotAngleRight(20);
		movement.waitForMotors();
		movement.forward(40);
			
	}*/
	private void turntoHolz(){// luft
		movement.forward(100);
		movement.pivotAngleRight(90);
		movement.waitForMotors();
		movement.forward(150);
	}
	private void searchHolzByCollision(){
		//boolean turn=false;
		movement.backward(90);
		movement.pivotAngleLeft(90);
		movement.waitForMotors();
		movement.forward(20);
	}
}

		

