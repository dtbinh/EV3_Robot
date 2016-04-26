package messi;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

/**
 * 
 * 
 *
 */
public class Offensive {
	
	private RegulatedMotor graspMotor;
	private RegulatedMotor armMotor;
	private RegulatedMotor gearReleaseMotor;
	private RegulatedMotor loadingMotor;
	private Navigation nav;

	
	/**
	 * This is the constructor called when offensive mode is selected
	 * @param grabMotor
	 * @param throMotor
	 * @param ballColorSensor
	 */
	public Offensive(RegulatedMotor armMotor,RegulatedMotor graspMotor,RegulatedMotor gearReleaseMotor, RegulatedMotor loadingMotor, Navigation nav)
	{
		this.graspMotor = graspMotor;
		this.armMotor = armMotor;
		this.gearReleaseMotor = gearReleaseMotor;
		this.loadingMotor = loadingMotor;
		this.nav = nav;
	}

	public void attack()
	{
		nav.travelTo(5, 5); //TODO: positions as hardcoded (from mycourses) or received from wificlass
		//confirm position
		grab(); //pick up ball and place on shooting mechanism
		nav.turnTo(0, true); //turn o shooting direction
		throwBall(0); //calculate distance to throw based on present position and goal position 
		
	}
	/**
	 * Call to grab the ball
	 */
	private void grab()
	{
		//TODO implement picking up of ball 
		colorOfBall(); //check color of ball
	}
	/**
	 * Call to throw the ball a certain distance
	 * @param distance
	 */
	private void throwBall(int distance)
	{
		//TODO release gear
	}
	/**
	 * Return the color of the ball placed in the grab position
	 * @return
	 */
	private String colorOfBall()
	{
		int threshold = 0 ;
		if(threshold < getFilteredData())
		{
			return "RED";
		}
		else
		{
			return "BLUE";
		}
		
	}
	/**
	 * Get the filtered data from color sensor
	 * @return 
	 */
	private float getFilteredData()
	{
		return 0;
		
	}
	
	
}
