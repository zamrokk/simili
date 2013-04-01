package com.simili.robot.state;

public class DifferencialDriveState implements State {

	public double v_right;
	public double v_left;

	public DifferencialDriveState() {
		this.v_right = 0;
		this.v_left = 0;
	}
	
	public DifferencialDriveState(double v_right, double v_left) {
		this.v_right = v_right;
		this.v_left = v_left;
	}

	public double getX_dot(double theta, double radius, double length) {
		return radius / 2 * (v_right + v_left) * Math.cos(theta);
	}

	public double getY_dot(double theta, double radius, double length) {
		return radius / 2 * (v_right + v_left) * Math.sin(theta);
	}

	public double getTheta_dot(double theta, double radius, double length) {
		return radius / length * (v_right - v_left);
	}

}
