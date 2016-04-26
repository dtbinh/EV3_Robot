package odometerTest;
import lejos.hardware.*;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class OdometerTest {

	// Static Resources:
	// Left motor connected to output A
	// Right motor connected to output D
	private static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));
	private static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));

	
	public static void main(String[] args) {
		
		// setup the odometer and display
		Odometer odo = new Odometer(leftMotor, rightMotor, 30, true);
		Navigation nav = new Navigation(odo);
		LCDInfo lcd = new LCDInfo(odo);
		
		// perform "square driver"
		nav.goForward(60.96);
		nav.turnTo(0.0, false);
		
		nav.goForward(60.96);
		nav.turnTo(270.0, false);
		
		nav.goForward(60.96);
		nav.turnTo(180.0, false);
		
		nav.goForward(60.96);
		nav.turnTo(90.0, true);
					
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);	
		
	}

}
