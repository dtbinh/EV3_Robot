package messi;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Defensive {

	private EV3LargeRegulatedMotor deployedMotor;
	
	/**
	 *  This is the constructor called when Defensive mode is selected
	 * @param deployedMotor
	 */
	public void Defensive(EV3LargeRegulatedMotor deployedMotor)
	{
		this.deployedMotor = deployedMotor;
		defend();
	}
	/**
	 * Defend based on strategy chosen
	 */
	private void defend()
	{
		
	}
	
}
