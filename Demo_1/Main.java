import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Main {

	/** fixed robot dimensions in cm */
	public static final double WHEEL_RADIUS = 2.12;
	public static final double DISTANCE_WHEELS = 16.45;
	public static final double DISTANCE_CENTERAXEL_SENSOR = 18.0;
	// TODO: Confirm that these values are correct

	public static Navigation nav;
	public static Odometer odometer;
	public static Localization localization;

	public static EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3
			.get().getPort("A")); //left motor
	public static EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3
			.get().getPort("D")); //right motor

	public static SensorModes usSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));
	public static SampleProvider usValue = usSensor.getMode("Distance");
	static float[] usData = new float[usValue.sampleSize()];

	public static SensorModes colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
	public static SampleProvider colorValue = colorSensor.getMode("ColorID"); 
	static float[] colorData = new float[colorValue.sampleSize()];
	
	public static void main(String[] args) {
		nav = new Navigation(odometer, leftMotor, rightMotor);
		localization = new Localization(leftMotor, rightMotor, odometer, usValue, usData, colorSensor, colorData, nav);
		localization.localize();
		nav.travelTo(180, 150);
		//Localization complete
		
		
		
		
	}
	
	
	
	

}
