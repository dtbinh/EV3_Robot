package messi;

import java.io.IOException;

import lejos.hardware.Sound;

import javax.swing.text.DefaultEditorKit.BeepAction;
import javax.swing.text.StyleContext.SmallAttributeSet;

import com.jcraft.jsch.IO;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.sensor.SensorModes;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * <h1>GameView - Main robot class<h1>
 * Entry point into system flow. Contains all component declarations for both
 * master and slave brick
 * 
 * @author Jason Dias
 * @version 1.0
 * @since 2016-03-23 
 *
 * <p>Note: * after/before any description acts as wild-card notation</p>
 */

public class GameView {

	/** fixed robot dimensions in cm */
	public static final double WHEEL_RADIUS = 2.12;
	public static final double DISTANCE_WHEELS = 16.45;
	public static final double DISTANCE_CENTERAXEL_SENSOR = 18.0;
	//TODO: Confirm that these values are correct

	
	public static Navigation nav;
	public static Odometer odometer;
	public static Localization localization;
	public static Offensive offensive;
	public static Defensive defensive;

	
	
	/** Main method, acts as entry point and does initialization for 
	 * connected hardware and required classes.
	 * @param args Unused
	 * @return Nothing.
	 * @exception IOException on unable to connect bricks 
	 * @see IOException
	 */
	public static void main(String[] args) {
		try {
			//TODO: THIS FLOW NEEDS TO BE TESTED ON THE ROBOT THIS WEEKEND 
			Sound.beep();
			/** Request connection between EV1 (Master) and EV2 (Slave)
			 *  masterBrick: code base brick used to implement system flow
			 *  slaveBrick: used purely for its ports
			 */
			RemoteRequestEV3 masterBrick = new RemoteRequestEV3(BrickFinder.find("EV1")[0].getIPAddress());
			RemoteRequestEV3 slaveBrick = new RemoteRequestEV3(BrickFinder.find("EV2")[0].getIPAddress());

			
			System.err.println("Bricks Successfully Connected");
			Sound.beep();
			/**
			 * Initialization of MasterBrick motor components 
			 * leftMotor: field declaration of left motor object for traveling 
			 * rightMotor: field declaration of right motor object for traveling 
			 * armMotor: field declaration of arm motor for controlling movement of robotic arm
			 * graspMotor: field declaration of grasping motor for controlling grasping motion of robotic arc
			 */
			final RegulatedMotor leftMotor = masterBrick.createRegulatedMotor("A", 'L');
			final RegulatedMotor rightMotor = masterBrick.createRegulatedMotor("D", 'L');
//			final RegulatedMotor armMotor = masterBrick.createRegulatedMotor("C", 'L');
//			final RegulatedMotor graspMotor = masterBrick.createRegulatedMotor("B", 'L');
			//TODO: We need to check where we are using NXT motors instead of the EV3 motors as they require the 'N' code. 
			
			/**
			 * Initialization of SlaveBrick motor components
			 * gearReleaseMotor: motor used to release shooting mechanism
			 * 
			 */	
//			final RegulatedMotor gearReleaseMotor = slaveBrick.createRegulatedMotor("A", 'M');
//			final RegulatedMotor loadingMotor = slaveBrick.createRegulatedMotor("B", 'L'); 
			Sound.beep();
			Sound.beep();
			System.err.println("motors initialized");
				
			/**
			 * Initialization & setup of MasterBrick sensor components
			 * usSensorFwd: initialization of forward facing US Sensor(localization/object avoidance) 
			 * usSensorSide: initialization side facing US Sensor (ball detection/wall following) 
			 * localizationSensor: light sensor used for light localization
			 * colorSensor: sensor used to detect the color of the ball when being picked up
			 */
			SampleProvider usSensorRight = masterBrick.createSampleProvider("S2", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
			float[] usData = new float[usSensorRight.sampleSize()];
			System.out.println("US Connected");
//			SampleProvider usSensorSide = masterBrick.createSampleProvider(
//					"S2", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
			
			SampleProvider localizationSensor = masterBrick.createSampleProvider("S1", "lejos.hardware.sensor.EV3ColorSensor", "Red");
			float[] colorData = new float[localizationSensor.sampleSize()];
//			SampleProvider colorSensor = masterBrick.createSampleProvider(
//					"S3", "lejos.hardware.sensor.EV3ColorSensor", "ColorID");
			
			// Set up Odometer
			/* Motor testing TODO: RAWAD PLEASE TAKE A LOOK AT THIS CODE/ CHECK WHY IT ISNT WORKING
			gearReleaseMotor.rotate(90, false);
			Delay.msDelay(2000);
			loadingMotor.rotate(500, false);
			Delay.msDelay(2000);
			gearReleaseMotor.rotate(-90);
			System.out.println("motor testing complete");
			*/
			
			odometer = new Odometer(leftMotor, rightMotor, 30, true);
			nav = new Navigation(odometer);
			
			localization = new Localization(leftMotor, rightMotor, odometer, usSensorRight, usData, localizationSensor, colorData);
			localization.localize();
			//LOCALIZATION COMPLETE
			
			//determine here if we are defensive or offensive. for the demo we are only going to be offensive
//			offensive = new Offensive(armMotor, graspMotor, gearReleaseMotor, loadingMotor, nav);
//			offensive.attack();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Brick Communication Error!");
			Sound.beepSequenceUp();
		}
		System.exit(0);
	}
}
