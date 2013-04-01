package com.simili.robot.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;
import com.simili.robot.position.Position2D;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;

public class GoToGoalBehavior implements Behavior {

	private final static Logger log = LoggerFactory
			.getLogger(GoToGoalBehavior.class);
	
	private BEHAVIORS name;
	private Position goalPosition;

	// memory banks
	private double E_k;
	private double e_k_1;

	// gains
	public double Kp;
	public double Ki;
	public double Kd;

	public GoToGoalBehavior(Position position) {
		name = BEHAVIORS.GO_TO_GOAL;
		goalPosition = position;
		E_k = 0;
		e_k_1 = 0;
		Kp = 5;
		Ki = 0.1;
		Kd = 0.3;
	}

	@Override
	public BEHAVIORS getName() {
		return name;
	}

	@Override
	public UnicycleDriveState execute(Robot robot,
			State desiredState, double delta_t) {

		log.info(" *** Behavior "+name+" is calculating new inputs ...");
		
		Position2D position_c = (Position2D) robot.getCenterPosition();
		Position2D position_g = (Position2D) goalPosition;
		UnicycleDriveState state_g = (UnicycleDriveState) desiredState;

		// 1. Calculate the heading (angle) to the goal.

		// distance between goal and robot in x-direction
		double u_x = position_g.x - position_c.x;

		// distance between goal and robot in y-direction
		double u_y = position_g.y - position_c.y;

		// angle from robot to goal. Hint: use ATAN2, u_x, u_y here.
		double theta_g = Math.atan2(u_y, u_x);

		// 2. Calculate the heading error.

		// error between the goal angle and robot's angle
		// Hint: Use ATAN2 to make sure this stays in [-pi,pi].
		double e_k = theta_g - position_c.theta;
		e_k = Math.atan2(Math.sin(e_k), Math.cos(e_k));

		// 3. Calculate PID for the steering angle

		// error for the proportional term
		double e_P = e_k;

		// error for the integral term. Hint: Approximate the integral using
		// the accumulated error, obj.E_k, and the error for
		// this time step, e_k.
		double e_I = E_k + e_k * delta_t;

		// error for the derivative term. Hint: Approximate the derivative
		// using the previous error, obj.e_k_1, and the
		// error for this time step, e_k.
		double e_D = (e_k - e_k_1) / delta_t;

		double w = Kp * e_P + Ki * e_I + Kd * e_D;

		// 4. Save errors for next time step
		E_k = e_I;
		e_k_1 = e_k;

		//just has to go at max if possible in function of w
		double v = robot.getMaxLinearVelocity()/(Math.log(Math.abs(w)+2)+1); 

		log.info(" *** new velocity should be : "+v+" and angular velocity : "+w);
		
		return new UnicycleDriveState(v, w);
	}

	@Override
	public void setPIDGains(double Kp, double Ki, double Kd) {
		this.Kp=Kp;
		this.Ki=Ki;
		this.Kd=Kd;
	}

}
