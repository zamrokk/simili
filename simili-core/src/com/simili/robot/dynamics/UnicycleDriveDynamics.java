package com.simili.robot.dynamics;

import com.simili.robot.position.Position;
import com.simili.robot.position.Position2D;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;

public class UnicycleDriveDynamics implements Dynamics {

	public Position applyDynamics(Position position, int delta_t, State state) {

		Position2D currentPosition = (Position2D) position;
		UnicycleDriveState currentState = (UnicycleDriveState) state;
		double x_t_1 = currentPosition.x + delta_t
				* currentState.getX_dot(currentPosition.theta);
		double y_t_1 = currentPosition.y + delta_t
				* currentState.getY_dot(currentPosition.theta);
		double theta_t_1 = currentPosition.theta + delta_t * currentState.w;

		return new Position2D(x_t_1, y_t_1, theta_t_1);
	}

}
