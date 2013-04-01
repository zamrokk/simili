package com.simili.robot.behavior;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;
import com.simili.robot.state.State;

public interface Behavior {

	public enum BEHAVIORS{ GO_TO_GOAL , STOP };
	
	public BEHAVIORS getName();

	public void setPIDGains(double Kp, double Ki, double Kd);

	/**
	 * 
	 * EXECUTE Computes the left and right wheel speeds for go-to-goal. [v, w] =
	 * execute(obj, robot, x_g, y_g, v) will compute the necessary linear and
	 * angular speeds that will steer the robot to the goal location (x_g, y_g)
	 * with a constant linear velocity of v.
	 * 
	 * @param robot
	 * @param desiredState
	 * @param delta_t
	 * @return
	 */
	public State execute(Robot robot,
			State desiredState, double delta_t);

}
