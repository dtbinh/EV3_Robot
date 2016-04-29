/*
 * Odometer.java
 */

package ev3Odometer;



public class Odometer extends Thread {
	// robot position
	private double x, y, theta;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;
	private int previousTacoRight = 0, previousTacoLeft = 0;
	// default constructor 
	private final int degreeToRadian = 1745;  
	public Odometer() {
		x = 0.0;
		y = 0.0;
		theta = (Math.PI/2);//We start theta at 90 degrees(Pi/2)
		lock = new Object();
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			// put (some of) your odometer code here
			
			synchronized (lock) {
				// don't use the variables x, y, or theta anywhere but here!
				double leftD = Lab2.WHEEL_RADIUS * (Lab2.getLeftMotor().getTachoCount() - previousTacoLeft) * degreeToRadian/100000; //displacement of left wheel
				double rightD = Lab2.WHEEL_RADIUS * (Lab2.getRightMotor().getTachoCount() - previousTacoRight) * degreeToRadian/100000 ; //displacement of right wheel
				double centerD = (leftD + rightD)/2; //displacement of center point
				
				double dx = centerD*Math.cos(theta); //change in x
				double dy = centerD*Math.sin(theta); //change in y
				previousTacoRight =Lab2.getRightMotor().getTachoCount();
				previousTacoLeft =Lab2.getLeftMotor().getTachoCount();
				x += dx; //update x coordinate
				y += dy; //update y coordinate
				//Calibrate 1.02 from experiment
				this.theta -= ((leftD -rightD)/Lab2.TRACK)*1.02; // change in theta //update theta
				//Bound theta 
				if (theta <= 0){
                    theta += Math.PI*2;
                }
                else if (theta > Math.PI*2){
                    theta -= Math.PI*2;
                }
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}

	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}

	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}

	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}

	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}