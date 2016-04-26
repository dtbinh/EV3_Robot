/*
 * Oliver Tse Sak Kwun-260604362
 * Paul Chen-260609747
 * Group 39
 * lab-3
 */
package messi;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 100;

	private Odometer odo;
	private SampleProvider usSensor;
	private float[] usData;
	private LocalizationType locType;
	private int distance;
	private double [] angles = new double [3];
	
	public USLocalizer(Odometer odo,  SampleProvider usSensor, float[] usData, LocalizationType locType) {
		this.odo = odo;
		this.usSensor = usSensor;
		this.usData = usData;
		this.locType = locType;
	}
	
	public void doLocalization() {
		double [] pos = new double [3];
		double angleA, angleB;
		
		Sound.setVolume(10);
			// rotate the robot until it sees no wall
			// Starts away from wall
			if(getFilteredData() > 40){
				Localization.navigate.rotateClockWise();
				while(getFilteredData() > 40){ //while no wall
					
				}
				
				double alpha = 360 - odo.getAng();
				//System.out.println("ALPHA:  " + alpha);
				Localization.navigate.rotateCounterClockWise();
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Sound.beep();
				
				while(getFilteredData() > 40){
					//System.out.println(getFilteredData());
				}
				double beta = -odo.getAng();
			
				//System.out.println("BETA   " + beta);
				
				Localization.navigate.turnAmount(-beta);
				//System.out.println("Turned -beta!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				double correctAngle = 67 + ((alpha + beta)/2);
				
				Localization.navigate.turnAmount(correctAngle);
				
				odo.setTheta(0);
			}

			
			
			
			
			// Starts facing the wall
			else{

			Localization.navigate.rotateClockWise();

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Sound.beep();
			
			while(getFilteredData() > 40){ //wall not detected
				
			}
			
			//System.out.println("First Wall");
			double alpha = 360 - odo.getAng();
			//System.out.println("ALPHA:  " + alpha);
			Localization.navigate.rotateCounterClockWise();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Sound.beep();
			
			while(getFilteredData() > 40){
				//System.out.println(getFilteredData());
			}
			double beta = 360 -  odo.getAng();
		
			//System.out.println("BETA   " + beta);
			
			Localization.navigate.turnAmount(-beta);
			//System.out.println("Turned -beta!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			double correctAngle = 62 + ((alpha + beta)/2);
			
			Localization.navigate.turnAmount(correctAngle);
			
			odo.setTheta(0);
		}
	}
	
	
	private float getFilteredData() {
		usSensor.fetchSample(usData, 0);
		float distance = usData[0]*100;
		if(distance > 60)
			distance = 60;
		return distance;
	}

	private static int convertDistance(double distance) {
		return (int) ((180.0 * distance) / (Math.PI * Localization.WHEEL_RADIUS));
	}
	
	private static int convertAngle(double angle) {
		return convertDistance(Math.PI * Localization.TRACK * angle / 360.0);
	}

}
