/*
 * File: Navigation.java
 * Written by: Sean Lawlor
 * ECSE 211 - Design Principles and Methods, Head TA
 * Fall 2011
 * Ported to EV3 by: Francois Ouellet Delorme
 * Fall 2015
 * 
 * Movement control class (turnTo, travelTo, flt, localize)
 */
/*
 * Oliver Tse Sak Kwun-260604362
 * Paul Chen-260609747
 * Group 39
 * lab-3
 */
package messi;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.RegulatedMotor;

public class Navigation {
	final static int FAST = 130, SLOW = 100, ACCELERATION = 3000,LOCALIZATION = 100;
	
	final static double DEG_ERR = 3.0, CM_ERR = 1.0;
	private Odometer odometer;
	private RegulatedMotor leftMotor, rightMotor;

	public Navigation(Odometer odo) {
		this.odometer = odo;

		RegulatedMotor[] motors = this.odometer.getMotors();
		this.leftMotor = motors[0];
		this.rightMotor = motors[1];

		// set acceleration
//		this.leftMotor.setAcceleration(ACCELERATION);
//		this.rightMotor.setAcceleration(ACCELERATION);
	}

	/*
	 * Functions to set the motor speeds jointly
	 */
//	public void setSpeeds(float lSpd, float rSpd) {
//		this.leftMotor.setSpeed(lSpd);
//		this.rightMotor.setSpeed(rSpd);
//		if (lSpd < 0)
//			this.leftMotor.backward();
//		else
//			this.leftMotor.forward();
//		if (rSpd < 0)
//			this.rightMotor.backward();
//		else
//			this.rightMotor.forward();
//	}

	public void setSpeeds(int lSpd, int rSpd) {
		this.leftMotor.setSpeed(lSpd);
		this.rightMotor.setSpeed(rSpd);
		if (lSpd < 0)
			this.leftMotor.backward();
		else
			this.leftMotor.forward();
		if (rSpd < 0)
			this.rightMotor.backward();
		else
			this.rightMotor.forward();
	}

	/*
	 * Float the two motors jointly
	 */
	public void setFloat() {
		this.leftMotor.setSpeed(0);
		this.rightMotor.setSpeed(0);
		this.leftMotor.forward();
		this.rightMotor.forward();
	}

	/*
	 * TravelTo function which takes as arguments the x and y position in cm Will travel to designated position, while
	 * constantly updating it's heading
	 */
	public void travelTo(double x, double y) {
		double minAng;
		while (Math.abs(x - odometer.getX()) > CM_ERR || Math.abs(y - odometer.getY()) > CM_ERR) {
			minAng = (Math.atan2(y - odometer.getY(), x - odometer.getX())) * (180.0 / Math.PI);
			if (minAng < 0)
				minAng += 360.0;
			this.turnTo(minAng, false);
			this.setSpeeds(FAST, FAST);
		}
		this.setSpeeds(0, 0);
		
	}
	
	public void localTravelTo(double x, double y){
		goForward(x);
		turnAmount(-90);
		goForward(y);
		
	}

	/*
	 * TurnTo function which takes an angle and boolean as arguments The boolean controls whether or not to stop the
	 * motors when the turn is completed
	 */
	public void turnTo(double angle, boolean stop) {

		double error = angle - this.odometer.getAng();

		while (Math.abs(error) > DEG_ERR) {

			error = angle - this.odometer.getAng();

			if (error <= -180.0) {
				this.setSpeeds(-LOCALIZATION, LOCALIZATION);
			} else if (error < 0.0) {
				this.setSpeeds(LOCALIZATION, -LOCALIZATION);
			} else if (error > 180.0) {
				this.setSpeeds(LOCALIZATION, -LOCALIZATION);
			} else {
				this.setSpeeds(-LOCALIZATION, LOCALIZATION);
			}
		}

		if (stop) {
			this.setSpeeds(0, 0);
		}
	}
	
	
	public void turnAmount(double theta){ //turns clockwise
		leftMotor.setSpeed(LOCALIZATION);
		rightMotor.setSpeed(LOCALIZATION);

		leftMotor.rotate(convertAngle(Localization.WHEEL_RADIUS, Localization.TRACK, theta), true);
		rightMotor.rotate(-convertAngle(Localization.WHEEL_RADIUS, Localization.TRACK, theta), false);
	}
	
	public void rotateCounterClockWise()
	{
		rightMotor.setSpeed(LOCALIZATION);
		leftMotor.setSpeed(LOCALIZATION);
		rightMotor.forward();
		leftMotor.backward();
	}
	public void rotateClockWise()
	{
		rightMotor.setSpeed(LOCALIZATION);
		leftMotor.setSpeed(LOCALIZATION);
		rightMotor.backward();
		leftMotor.forward();
	}
	
	/*
	 * Go foward a set distance in cm
	 */
	public void goForward(double distance) {
		leftMotor.setSpeed(LOCALIZATION);
		rightMotor.setSpeed(LOCALIZATION);
		leftMotor.rotate(convertDistance(Localization.WHEEL_RADIUS, distance), true);
		rightMotor.rotate(convertDistance(Localization.WHEEL_RADIUS, distance), false);
	}
	
	public void goForward8(){
		
		rightMotor.setSpeed(SLOW);
		leftMotor.setSpeed(SLOW);
		leftMotor.rotate((int)(8/(2.2*2*Math.PI)*360), true);
		rightMotor.rotate((int)(8/(2.2*2*Math.PI)*360), false);
		
	}
	
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	public static int convertDistance(double radius, double distance) { //how many times the wheels turn
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
}
