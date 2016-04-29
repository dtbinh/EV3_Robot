/* 
 * OdometryCorrection.java
 */
package ev3Odometer;

import java.util.Arrays;

import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	private Odometer odometer;
	private static SampleProvider sampleProvider;
	private static int sampleSize=1;
	private EV3ColorSensor sensorC= new EV3ColorSensor(LocalEV3.get().getPort("S2"));// create a sensor objec	
	
	
	
	// constructor
	public OdometryCorrection(Odometer odometer) {
		this.odometer = odometer;
		Sound.setVolume(50);
		sensorC.setFloodlight(true);
	}
	private static float[] getSample() {
        // Initializes the array for holding samples
        float[] sample = new float[sampleSize];

        // Gets the sample an returns it   
        sampleProvider.fetchSample(sample, 0);
        return sample;
    }

	// run method (required for Thread)
	public void run() {
		long correctionStart, correctionEnd;
		double[] lastDetectionPoint = { 17, 0, Math.PI}; 
		
		while (true) {
			correctionStart = System.currentTimeMillis();
			sampleProvider=sensorC.getRedMode();			
			//For experiment purposes
			//	LCD.drawString("Color"+Arrays.toString(getSample()),0,6);
			
			if(getSample()[0]<0.5){
				
				Sound.beep(); //beep when detect black line
				

				/*
				 * Values chosen
				 * 17 - value of X or Y position when traveling in the negative X or Y direction
				 * 43 - value of X or Y position when traveling in the positive X or Y direction
				 * 47 - value of X or Y position when traveling in the negative X or Y direction
				 * 13 - value of X or Y position when traveling in the positive X or Y direction
				 */
				 
				//when traveling straight, calculate the deviated angle by using the distance between current position and last detection point 
				//compare with odometer theta and correct angles when robot is deviated last than 6 degrees
				if( Math.abs(lastDetectionPoint[2]-odometer.getTheta()) < (Math.PI/12)  &&  
						( Math.abs(lastDetectionPoint[0]-odometer.getX()) < 30/Math.cos(Math.toRadians(6)) ||
						  Math.abs(lastDetectionPoint[1]-odometer.getY()) < 30/Math.cos(Math.toRadians(6)) ) ) {
					odometer.setTheta(0.5*Math.PI*Math.round(odometer.getTheta()/(Math.PI/2)));
				}
				//make correction when hitting one of the eight crossing points
				//correct X when travel in the X direction and Y when travel in Y direction
				//beep after each correction
				if( Math.abs(odometer.getTheta()-(Math.PI/2))<(Math.PI/12) && Math.abs(odometer.getY()-13)<15 ){
				//	Sound.beep();
					odometer.setY( 13 );
				}
				if( Math.abs(odometer.getTheta()-(Math.PI/2))<(Math.PI/12) && Math.abs(odometer.getY()-43)<15 ){
				//	Sound.beep();
					odometer.setY( 43 );
				}
				if( Math.abs(odometer.getTheta())<(Math.PI/12) && Math.abs(odometer.getX()-13)<15 ){
				//	Sound.beep();
					odometer.setX( 13 );
				}
				if( Math.abs(odometer.getTheta())<(Math.PI/12) && Math.abs(odometer.getX()-43)<15 ){
				//	Sound.beep();
					odometer.setX( 43 );
				}
				if( Math.abs(odometer.getTheta()-((Math.PI)*3/2))<(Math.PI/12) && Math.abs(odometer.getY()-47)<15 ){
				//	Sound.beep();
					odometer.setY( 47 );
				}
				if( Math.abs(odometer.getTheta()-((Math.PI)*3/2))<(Math.PI/12) && Math.abs(odometer.getY()-17)<15 ){
				//	Sound.beep();
					odometer.setY( 17 );
				}
				if( Math.abs(odometer.getTheta()-(Math.PI))<(Math.PI/12) && Math.abs(odometer.getX()-47)<15 ){
				//	Sound.beep();
					odometer.setX( 47 );
				}
				if( Math.abs(odometer.getTheta()-(Math.PI))<(Math.PI/12) && Math.abs(odometer.getX()-17)<15 ){
				//	Sound.beep();
					odometer.setX( 17 );
				}
				lastDetectionPoint[0] = odometer.getX();
				lastDetectionPoint[1] = odometer.getY();
				lastDetectionPoint[2] = odometer.getTheta();

				
			}

			// this ensure the odometry correction occurs only once every period
			correctionEnd = System.currentTimeMillis();
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometry correction will be
					// interrupted by another thread
				}
			}
			
		}
	}
	
}