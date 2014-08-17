package com.simili.robot.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;

public class StopBehaviour<ROBOT extends Robot<?, ?,?>> implements
		Behavior<ROBOT> {

	private Logger log = LoggerFactory.getLogger(StopBehaviour.class);

	@Override
	public BEHAVIORS getName() {
		return BEHAVIORS.STOP;
	}

	@Override
	public void setPIDGains(double Kp, double Ki, double Kd) {
	}

	@Override
	public State execute(ROBOT robot, State desiredState, double delta_t) {
		log.info(" *** Behavior " + getName() + " is stopping the robot");
		return new UnicycleDriveState(0, 0);
	}

}
