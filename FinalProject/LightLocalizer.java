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
import lejos.utility.Delay;

public class LightLocalizer {
	private Odometer odo;
	private SampleProvider colorSensor;
	private float[] colorData;
//	SET INITIAL CORRDINATE
	private final int[] stratingCoordinate={10, 10};
//	STORE THE THETA WHEN CROSSING BLACK LINE
//	ith POSITION CORRESPOND TO ith POSISTION IN ARRAY
	private double [] thetaAtBlackLine=new double[4];
//	TO KEEP TRACK OF HOW MANY LINES THE SENSOR HAS CROSSED
	private int lineCount=0; 
	
	public LightLocalizer(Odometer odo, SampleProvider colorSensor, float[] colorData) {
		this.odo = odo;
		this.colorSensor = colorSensor;
		this.colorData = colorData;
	}
	
	public void doLocalization() {

			
//		TRAVEL TO INITIAL COORDINATE AS SHOWN IN TUTORIAL
	//	Lab4.navigate.travelTo(stratingCoordinate[0],stratingCoordinate[0]);
		
		Localization.navigate.turnAmount(-45);		
		
//		SET POSITION TO (0,0) WHICH WILL THEN BE LATER CORRECTED
		odo.setPosition(0, 0);

//		TURING COUNTER-CLOCKWISE
		Localization.navigate.rotateCounterClockWise();
//		UNTIL 4 BLACK LINES ARE CROSSED
		while(lineCount < 4)
		{
//			LCD.drawString("light : "+getFilteredData(), 0, 5);
//			WHEN THE LIGHT SENSOR DETECT A BLACK LINE
			if(getFilteredData() < 0.5)
			{
				
//				STORE THE ANGLE THETA WHEN SENSOR DETECT A BLACK LINE
				thetaAtBlackLine[lineCount] = odo.getAng();
				//System.out.println(odo.getAng());
//				INCREMENT LINE COUNT
				lineCount++;
//				FOR INFORM EXPERIMENTER THAT ROBOT HAS CORRECTLY DETECTED A BLACK LINE
				Sound.beep();
				if (lineCount < 3){
				Delay.msDelay(500);
				}
			}
		}
//		if(true){//4th line
//			if(getFilteredData() < 0.5)
//			{
//				
////				STORE THE ANGLE THETA WHEN SENSOR DETECT A BLACK LINE
//				thetaAtBlackLine[lineCount] = odo.getAng();
//				//System.out.println(odo.getAng());
////				INCREMENT LINE COUNT
//				lineCount++;
////				FOR INFORM EXPERIMENTER THAT ROBOT HAS CORRECTLY DETECTED A BLACK LINE
//				Sound.beep();
//			}
//		}
//		STOP THE COUNTER_CLOCKWISE MOTION AND STOP THE ROBOT FROM MOVING
		Localization.navigate.setFloat();
		
//		COMPUTE THE VARIOUS THETA(X) AND THETA(Y)
//		USING TRIGONOMETRY AS SHOWN IN TUTORIAL
		for( int n=1 ; n<4 ; n++){
			thetaAtBlackLine[n] += 1;
		}
		
		double thetaY = thetaAtBlackLine[0] - thetaAtBlackLine[2];
		double thetaX = thetaAtBlackLine[1] - thetaAtBlackLine[3];
		double deltaAngle = thetaAtBlackLine[0] - thetaAtBlackLine[3];
		double changeX = Math.abs(Localization.sensorToCenterOfRotation * Math.cos(Math.toRadians(thetaY) / 2));
		double changeY = Math.abs(Localization.sensorToCenterOfRotation * Math.cos(Math.toRadians(thetaX) / 2));
		System.out.println("CHANGEX:    " + changeX);
		System.out.println("CHANGEY:    " + changeY);
		
		double changeInTheta = (360-Math.toDegrees(Math.asin(Math.abs(changeY)/Localization.sensorToCenterOfRotation)))-thetaAtBlackLine[3];
		odo.setPosition(0.0, 0.0);
		
		if(odo.getAng() > 300){
			Localization.navigate.turnAmount(odo.getAng()-360);
		}
		else{
			Localization.navigate.turnAmount(odo.getAng());
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		odo.setTheta(0.0);
		
		odo.setPosition(0.0,0.0);
		Localization.navigate.goForward(changeX);
		Localization.navigate.turnAmount(-90);
		Localization.navigate.goForward(changeY);
	}
//	METHOD TO GET DISTACE FROM SENSOR TO FLOOR
	private float getFilteredData(){
		colorSensor.fetchSample(colorData, 0);
		float distance = colorData[0];
		return distance;
	}

}
