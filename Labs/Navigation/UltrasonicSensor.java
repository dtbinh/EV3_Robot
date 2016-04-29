package Navigation;
/*
 * Oliver Tse Sak Kwun-260604362
 * Paul Chen-260609747
 * Group 39
 * lab-3
 */
//From lab-1 with minor changes
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor extends Thread{

	private float[] usData;
	private SampleProvider us ;
	private static int distance;
	//Robot has encountered an obstacle
	private static double emergencyDistance = 10 ;
	
	public UltrasonicSensor(SampleProvider us,float[] usData) {	
		this.us=us;
		this.usData=usData;
		}
	public void run(){
		while (true) {
			us.fetchSample(usData,0);		
			// acquire data
			
			distance=(int)(usData[0] * 100.0);
			// Filter value to 80	
			if(distance>80){
				distance=80;
			}
			//Print the ultrasonic result on display
			LCD.drawString("Value" + distance,0, 6);
			try { 
				
				Thread.sleep(100); 
			} catch(Exception e){}		
		}
	}
	//This is a method that return True/False depending on whether the emergency distance is exceeded
	//will be call by avoid obstacle 
	public static boolean isEmergency()	{
		// acquire data
		if( distance < emergencyDistance){
			return true;
		}
		return false;
	}

}
