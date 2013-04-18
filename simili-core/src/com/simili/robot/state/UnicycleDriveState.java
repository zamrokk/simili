package com.simili.robot.state;

/**
 * 
 * @author zam
 * 
 *         Unicycle state with : <br>
 *         <li>v : velocity 
 *         <li>w : angular velocity
 *         
 * 
 */
public class UnicycleDriveState implements State {

	public double v;	
	public double w;
	
	public UnicycleDriveState() {
		this.v = 0;
		this.w = 0;
	}

	public UnicycleDriveState(double v, double w) {
		this.v = v;
		this.w = w;
	}
	
	public UnicycleDriveState(double x_dot,double y_dot, double w, double theta) {
		this.v = x_dot/Math.cos(theta);
		this.w = w;
	}


	public double getX_dot(double theta) {
		return v*Math.cos(theta);
	}

	public double getY_dot(double theta) {
		return v*Math.sin(theta);
	}

}
