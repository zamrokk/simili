package com.simili.robot.behavior;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;
import com.simili.robot.state.State;

public class AvoidObstacleBehavior implements Behavior {

	private Position obstaclePosition;
	
	public AvoidObstacleBehavior(Position position) {
		obstaclePosition = position;
	}
	
	@Override
	public BEHAVIORS getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPIDGains(double Kp, double Ki, double Kd) {
		// TODO Auto-generated method stub

	}

	@Override
	public State execute(Robot robot,
			State desiredState, double delta_t) {
		// TODO Auto-generated method stub
		return null;
	}

}
