package com.simili.robot.dynamics;

import com.simili.robot.position.Position;
import com.simili.robot.position.Position2D;
import com.simili.robot.state.DifferencialDriveState;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;

public class DifferencialDriveDynamics implements Dynamics {

	public final double wheel_radius;
	public final double wheel_base_length;

	public DifferencialDriveDynamics(double wheel_radius,
			double wheel_base_length) {
		this.wheel_radius = wheel_radius;
		this.wheel_base_length = wheel_base_length;
	}

	/**
	 * Transform unicycle state (v,w) to differential state
	 * (v_right,v_left)
	 */
	public DifferencialDriveState unicycle2differential(Position2D position,
			UnicycleDriveState unicycleDriveState) {

		double v_right = unicycleDriveState.v / this.wheel_radius
				+ (unicycleDriveState.w * this.wheel_base_length)
				/ (2 * this.wheel_radius);
		double v_left = unicycleDriveState.v / this.wheel_radius
				- (unicycleDriveState.w * this.wheel_base_length)
				/ (2 * this.wheel_radius);
		
		return new DifferencialDriveState(v_right, v_left);
	}

	/**
	 * Transform differential state (v_right,v_left) to unicycle state (v,w)
	 * 
	 */
	public UnicycleDriveState differential2unicycle(double v_right,
			double v_left) {

		double v = this.wheel_radius / 2 * (v_right + v_left);
		double w = this.wheel_radius / this.wheel_base_length
				* (v_right - v_left);
		return new UnicycleDriveState(v, w);
	}

	public Position applyDynamics(Position position, int delta_t, State state) {
		Position2D currentPosition = (Position2D) position;
		DifferencialDriveState currentState = (DifferencialDriveState) state;
		UnicycleDriveState unicycleDriveState = differential2unicycle(currentState.v_right,currentState.v_left);
	
		double v = unicycleDriveState.v;
        double w = unicycleDriveState.w;
		
		double x_t_1 = currentPosition.x + delta_t
				* (v * Math.cos(currentPosition.theta))
				;
		double y_t_1 = currentPosition.y + delta_t
				* (v * Math.sin(currentPosition.theta))
				;
		double theta_t_1 = currentPosition.theta + delta_t 
				* w
				;

		return new Position2D(x_t_1, y_t_1, theta_t_1);
	}

}
