package messi;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Main {

	private static SampleProvider usValue;
	private static SampleProvider colorValue;
	
	private static float[] usData;
	private static int distance;
	public static void main(String[] args) {
		
		try{
			
			//String address = BrickFinder.discover()[0].getIPAddress(); //Gets the IP Address of the slave brick and stores it in a String
			
			RemoteRequestEV3 masterBrick = new RemoteRequestEV3(BrickFinder.find("EV1")[0].getIPAddress());
			RemoteRequestEV3 slaveBrick = new RemoteRequestEV3(BrickFinder.find("EV2")[0].getIPAddress());
		
			//slaveBrick = new RemoteRequestEV3(address); //connect the two bricks
			
			System.out.println("The brick is connected");
			
			//Initialization of Slave Motors
//			RegulatedMotor slaveMotorA = slaveBrick.createRegulatedMotor("A", 'L'); // Port A
//			RegulatedMotor slaveMotorB = slaveBrick.createRegulatedMotor("B", 'L'); // Port B
//			RegulatedMotor slaveMotorC = slaveBrick.createRegulatedMotor("C", 'L'); // Port C
//			RegulatedMotor slaveMotorD = slaveBrick.createRegulatedMotor("D", 'L'); // Port D
			
			//Initialization of Master Motors
//			RegulatedMotor masterMotorA = masterBrick.createRegulatedMotor("A", 'L');
//			RegulatedMotor masterMotorB = masterBrick.createRegulatedMotor("B", 'L');
//			RegulatedMotor masterMotorC = masterBrick.createRegulatedMotor("C" , 'L');
//			RegulatedMotor masterMotorD = masterBrick.createRegulatedMotor("D" , 'L');

			/*
			 * At this point the connection between the Master and Slave bricks is complete and all the motors have been initialized
			 */
			
			usValue = masterBrick.createSampleProvider("S2", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance"); //S1 USsensor
			colorValue = slaveBrick.createSampleProvider("S1", "lejos.hardware.sensor.EV3ColorSensor", "Red"); //S2 colorSensor
			
			float[] usData = new float[usValue.sampleSize()];
			float[] colorData = new float[colorValue.sampleSize()];
			
			ReadSensorTest read = new ReadSensorTest(usValue, usData);
			ReadLight light = new ReadLight(colorValue, colorData);
			
			read.readSensor();
			light.readSensor();
			
			/*masterMotorA.setSpeed(300);
			slaveMotorA.setSpeed(300);
			
			masterMotorA.forward();
			slaveMotorA.forward();
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			masterMotorA.setSpeed(0);
			slaveMotorA.setSpeed(0);*/
			
		}catch(Exception e){
			System.out.println("Error has occured. Could not connect the brick");
		}
		
		System.exit(0);
		
	}

}