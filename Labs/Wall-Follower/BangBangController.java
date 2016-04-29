package wallFollower;
import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController{
	private final int bandCenter, bandwidth,FILTER_OUT = 20;;
	private final int motorLow, motorHigh;
	//private final int motorStraight=150;  //Define an average motor speed
	private int distance;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	
	public BangBangController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
							  int bandCenter, int bandwidth, int motorLow, int motorHigh) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftMotor.setSpeed(motorHigh);				// Start robot moving forward
		rightMotor.setSpeed(motorHigh);
		leftMotor.forward();
		rightMotor.forward();
	}
	
	@Override
	public void processUSData(int distance) {
		
		//Add filter for off values
		int filterControl=0;
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		
		this.distance = distance;
		//ACCEPTED MARGIN OF ERROR
		int distanceError=this.distance-this.bandCenter;

		
		//if robot is within (bandCenter+-bandWidth)distance to wall both wheels rotates at same rate 
		//causing robot to move in a straight line equidistant to wall
		if((this.distance <= (this.bandCenter + this.bandwidth) && this.distance >= (this.bandCenter - this.bandwidth))){
			rightMotor.setSpeed(motorHigh);
			leftMotor.setSpeed(motorHigh);
			leftMotor.forward();
			rightMotor.forward();
		}
		//if robot is too far from wall
		//outer wheel rotates faster than the inner wheel
		else if(distanceError>this.bandwidth){
			
			rightMotor.setSpeed(motorHigh);
			leftMotor.setSpeed(motorLow);
			
			leftMotor.forward();
			rightMotor.forward();
		}
		//if robot is too close to wall
		//outer wheel rotates at a lower rate than the inner wheel
		else if(distanceError<this.bandwidth){
			
			leftMotor.setSpeed(motorHigh);
			rightMotor.setSpeed(motorLow);
			
			leftMotor.forward();
			rightMotor.forward();
		}
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
