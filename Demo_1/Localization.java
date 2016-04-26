/*
 * Oliver Tse Sak Kwun-260604362
 * Paul Chen-260609747
 * Group 39
 * lab-3
 */


import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Localization {

	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	// Ultrasonic sensor port connected to input S1
	// Color sensor port connected to input S2
	private static EV3LargeRegulatedMotor leftMotor;
	private static EV3LargeRegulatedMotor rightMotor;
	private static Port usPort;		
	private static Port colorPort;		
	private static Odometer odo;
	private SampleProvider usSensor;
	private float[] usData;
	private SampleProvider colorSensor;
	private float[] colorData;

	public static final double WHEEL_RADIUS = 2.1;
	public static final double TRACK = 16.3;//Distance between the wheels
//	CREATE A NAVIGATION VARIABLE
	public static Navigation navigate;
//	DISTANCE FROM LIGHT SENSOR TO THE CENTER OF ROTATION
	public static double sensorToCenterOfRotation = 18.4;
	
	
	public Localization(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor, Odometer odo, SampleProvider usSensor, float[] usData, SampleProvider colorSensor, float[] colorData, Navigation nav){
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		this.odo = odo;
		this.usSensor = usSensor;
		this.usData = usData;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
		this.navigate = nav;
	}
	
	public void localize() {
		
		// setup the odometer and display
//		INITIATE NAVIGATE AND PASS THE ODOMETER
		
		
		// perform the ultrasonic localization
		USLocalizer usl = new USLocalizer(odo, usSensor, usData, USLocalizer.LocalizationType.FALLING_EDGE);
		//LCD.drawString("ULTRASONIC Starts", 0, 5);
//		
		usl.doLocalization();
//		navigate.turnTo(0, true);
		
		//WAIT TO PERFORM LIGHT LOCALIZATION 
		Button.waitForAnyPress();
		//LCD.drawString("LIGHT starts", 0, 6);

		// perform the light sensor localization
		LightLocalizer lsl = new LightLocalizer(odo, colorSensor, colorData);

		lsl.doLocalization();			
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);	
		
		
	}

}
