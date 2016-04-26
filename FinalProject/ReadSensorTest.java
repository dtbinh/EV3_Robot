package messi;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.SampleProvider;

public class ReadSensorTest {

	private SampleProvider usSensor;
	private float[] usData;
	private int distance;
	
	public ReadSensorTest(SampleProvider usSensor, float[] usData) {
		this.usSensor = usSensor;
		this.usData = usData;
	}
	
	public void readSensor() {
		while(true){
			System.out.println("DISTANCE:      " + getFilteredData());
		}
	}

	private float getFilteredData() {
		usSensor.fetchSample(usData, 0);
		float distance = usData[0]*100;
		if(distance > 90)
			distance = 90;
		return distance;
	}
}
