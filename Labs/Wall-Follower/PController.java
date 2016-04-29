package wallFollower;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwidth;
	private final int motorStraight = 200, FILTER_OUT = 20;
	//Declare a low speed motor when turning
	private final int motorLow = 90;
	private EV3LargeRegulatedMotor leftMotor, rightMotor;
	private int distance;
	private int filterControl;
	
	public PController(EV3LargeRegulatedMotor leftMotor, EV3LargeRegulatedMotor rightMotor,
					   int bandCenter, int bandwidth) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwidth = bandwidth;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		leftMotor.setSpeed(motorStraight);					// Initalize motor rolling forward
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter - toss out invalid samples corresponding to null signal.
		// (n.b. this was not included in the Bang-bang controller, but easily could have).
		//
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
		
		int distanceError1=Math.abs(this.distance-this.bandCenter);
		int distanceError=this.distance-this.bandCenter;
		//3 was determine through trial and error
		
		int delta =3*distanceError1;
		
		//set a limiting increase in speed so that the motorStraight is not exceeded
		//moreover in turns it is safer at low speed
		if(delta>110)
		{
			delta=100;
		}
		//if robot is within (bandCenter+-bandWidth)distance to wall both wheels rotates at same rate 
		//causing robot to move in a straight line equidistant to wall
		if((this.distance <= (this.bandCenter + this.bandwidth) && this.distance >= (this.bandCenter - this.bandwidth))){
			rightMotor.setSpeed(motorStraight);
			leftMotor.setSpeed(motorStraight);
			rightMotor.forward();
			leftMotor.forward();  
		}

		/*
		 * We found that it is efficient to slow down one wheel and speeding the other one to make the robot turn
		 * We tried to use the backward() function but was not very efficient
		 * 90 was used for motorLow because the sensor is not very sensitive
		 */
		
		//if robot is too close to wall
		//outer wheel rotates slower than inner wheel
		//at a rate proportional to the off set distance
		else if(distanceError < this.bandwidth){
			rightMotor.setSpeed(motorLow);
			leftMotor.setSpeed(motorStraight + delta);
			rightMotor.forward();
			leftMotor.forward();  
		}
		//if robot is too far from wall
		//outer wheel rotates faster than the inner wheel 
		//at a rate proportional to the off set distance
		else if(distanceError > this.bandwidth){

			rightMotor.setSpeed(motorStraight + delta);
			leftMotor.setSpeed(motorLow);
			rightMotor.forward();
			leftMotor.forward();  

		}

	}
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}
