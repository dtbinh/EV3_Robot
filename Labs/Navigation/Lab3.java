
package Navigation;

import lejos.hardware.Button;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.SampleProvider;


public class Lab3{
	
	//Declare the right and left motors
	public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

	public static final double WHEEL_RADIUS = 2.2;
	public static final double TRACK = 13.5;//Distance between the wheels
	
	public static void main(String[] args) {
		
		final TextLCD t = LocalEV3.get().getTextLCD();
		// some objects that need to be instantiated
		Odometer odometer = new Odometer();	
		OdometryDisplay odometryDisplay = new OdometryDisplay(odometer,t);
		AvoidObstacle avoidObstacle = new AvoidObstacle(odometer);
		
		t.clear();
			// ask the user whether the motors should drive in a square or float
		t.drawString("< Left  | Right > ", 0, 0);
		t.drawString("        |         ", 0, 1);
		t.drawString(" First  |  Second ", 0, 2);
		t.drawString(" Part!  |   Part! ", 0, 3);
		t.drawString("        |         ", 0, 4);
		int buttonChoice = Button.waitForAnyPress();
				
		if (buttonChoice == Button.ID_LEFT) { //start first half of lab3
			
			Drive drive = new Drive(odometer);
			//Start the following threads
			odometer.start();
			odometryDisplay.start();
			drive.start();
			
		}
		
		if (buttonChoice == Button.ID_RIGHT) { //start second half of lab3
			
			EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));			// usSensor is the instance
			SampleProvider usDistance = usSensor.getMode("Distance");// usDistance provides samples from this instance
			Sound.beep();//Sound to let user know that sensor is initiated

			float[] usData = new float[usDistance.sampleSize()];// usSensor is the instance
			UltrasonicSensor ultrasonicsensor=new UltrasonicSensor(usDistance,usData);
			
			//4 Threads to start
			odometer.start();
			odometryDisplay.start();
			avoidObstacle.start();
			ultrasonicsensor.start();
		
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
	
}