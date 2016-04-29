package Navigation;
/*
 * Oliver Tse Sak Kwun-260604362
 * Paul Chen-260609747
 * Group 39
 * lab-3
 */
public class Drive extends Thread {
	
	private static final int FORWARD_SPEED = 250;//Set forward and rotating speed
	private static final int ROTATE_SPEED = 150;

	private Odometer odometer;
	private boolean navigating = false;
	// constructor
	public Drive ( Odometer odometer ) {
		this.odometer = odometer;
	}
	public void run() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// there is nothing to be done here because it is not expected that
			// the odometer will be interrupted by another thread
		}
		
	
		//Coordinates that the robot has to go
		travelTo(60, 30);
		travelTo(30, 30);
		travelTo(30, 60);
		travelTo(60, 0);
		
		//Stop the wheels from moving after desired paths are done
		Lab3.rightMotor.flt();
		Lab3.leftMotor.flt();
	}
	
	//Method that takes as input coordinate in the euclidean plane causing the robot 
	//to travel in a to that particular coordinate

	public void travelTo( double x, double y ){
		
		navigating = true;
		double maxDistanceError = 5 ;		//Allow a margin of error in x,y and theta
		double maxThetaError = 10 * Math.PI / 180 ;
		double netDistance = 60;
		double turnToAngle, xDistance, yDistance;
		
		while( netDistance > maxDistanceError){
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
		//Robot stop moving
		navigating = false;
	}

	//Method that cause the robot to turn theta angle
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
			Lab3.leftMotor.setSpeed(ROTATE_SPEED + 3 * (int)rotateDegree);
			Lab3.rightMotor.setSpeed(ROTATE_SPEED - 3 * (int)rotateDegree);
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
	//Method that return True/False depending on whether robot is moving
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
