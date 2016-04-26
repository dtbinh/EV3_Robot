package messi;
import lejos.robotics.SampleProvider;


public class ReadLight {

	private SampleProvider colorSensor;
	private float[] colorData;
	
	public ReadLight(SampleProvider colorSensor, float[] colorData){
		this.colorSensor = colorSensor;
		this.colorData = colorData;
	}
	
	public void readSensor() {
		while(true){
			System.out.println("COLOR: " + getFilteredData());
		}
	}
	
	private float getFilteredData(){
		colorSensor.fetchSample(colorData, 0);
		float distance = colorData[0];
		return distance;
	}
}
