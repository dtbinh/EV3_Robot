package Navigation;

import lejos.hardware.Sound;

public class AvoidObstacle extends Thread {
	
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;
	
	private static final double turningRadius = 15;
	private static final int rightTurningDegree = (int)((turningRadius + Lab3.TRACK) * (180 / Lab3.WHEEL_RADIUS));
	private static final int leftTurningDegree = (int)(turningRadius * (180 / Lab3.WHEEL_RADIUS)) ;

	private Odometer odometer;
	private boolean navigating = false;
	// constructor
	public AvoidObstacle ( Odometer odometer) {
		this.odometer = odometer;
	}
	public void run() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		travelTo(0, 60);
		travelTo(68, 10);

		Lab3.rightMotor.flt();
		Lab3.leftMotor.flt();
	}
	
	public void travelTo( double x, double y ){
		
		navigating = true;
		double maxDistanceError = 3 ;
		double maxThetaError = 10 * Math.PI / 180 ;
		double netDistance = 60;
		double turnToAngle, xDistance, yDistance;
		
		
		
		while( netDistance > maxDistanceError){
			//Modified TravelTo for obstacle
			if ( UltrasonicSensor.isEmergency() ){
				
				emergency( x, y );
				break;
			}
			xDistance = x - odometer.getX();
			yDistance = y - odometer.getY();
			turnToAngle = Math.atan2( yDistance , xDistance );
			netDistance = Math.sqrt(  Math.pow( yDistance, 2 ) + Math.pow( xDistance, 2 )  );

			Lab3.leftMotor.setSpeed(FORWARD_SPEED);
			Lab3.rightMotor.setSpeed(FORWARD_SPEED);
			
			if( Math.abs( turnToAngle - odometer.getTheta() ) > maxThetaError ){
				turnTo( turnToAngle );
			}
			
			Lab3.leftMotor.forward();
			Lab3.rightMotor.forward();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// there is nothing to be done here because it is not
				// expected that OdometryDisplay will be interrupted
				// by another thread
			}
			
			
		}
			
		navigating = false;
		
	}

	public void turnTo ( double theta ){
		
		navigating = true;
		
		double rotateDegree = ( odometer.getTheta() - theta ) * 180 / Math.PI ;
		if(rotateDegree > 180){
			rotateDegree -= 360;
		}
		if(rotateDegree > 180){
			rotateDegree -= 360;
		}
		
		if( Math.abs(rotateDegree)<15 ){
			Lab3.leftMotor.setSpeed(ROTATE_SPEED + 2 * (int)rotateDegree);
			Lab3.rightMotor.setSpeed(ROTATE_SPEED - 2 * (int)rotateDegree);
			Lab3.leftMotor.forward();
			Lab3.rightMotor.forward();
		}
		else{
			Lab3.leftMotor.setSpeed(ROTATE_SPEED);
			Lab3.rightMotor.setSpeed(ROTATE_SPEED);
			Lab3.leftMotor.rotate(convertAngle(rotateDegree), true);
			Lab3.rightMotor.rotate(convertAngle(-rotateDegree), false);
		}
		
		navigating = false;
		
	}
	
	public void emergency(double x, double y ){
		Sound.setVolume(100);
		Sound.beep();
		
		Lab3.rightMotor.setSpeed(150);
		Lab3.leftMotor.setSpeed(150);
		Lab3.rightMotor.rotate(-267, true);
		Lab3.leftMotor.rotate(267, false);
		
		Lab3.rightMotor.rotate(300, true);
		Lab3.leftMotor.rotate(300, false);
		
		Lab3.rightMotor.setSpeed(rightTurningDegree / 10);
		Lab3.leftMotor.setSpeed(leftTurningDegree / 10);
		Lab3.rightMotor.rotate( rightTurningDegree , true);
		Lab3.leftMotor.rotate( leftTurningDegree , false);
		
		
		Lab3.leftMotor.setSpeed(150);
		Lab3.leftMotor.rotate(266 * 2, true);
		
		travelTo( x, y );
		
	}
	
	
	public boolean isNavigating(){
		return navigating;
	}
	
	private static int convertDistance(double distance) {
		return (int) ((180.0 * distance) / (Math.PI * Lab3.WHEEL_RADIUS));
	}
	
	private static int convertAngle(double angle) {
		return convertDistance(Math.PI * Lab3.TRACK * angle / 360.0);
	}
}