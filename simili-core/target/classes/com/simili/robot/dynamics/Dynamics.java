package com.simili.robot.dynamics;


import com.simili.robot.position.Position;
import com.simili.robot.state.State;

public interface Dynamics {

 public Position applyDynamics(Position position,int delta_t,State state);
		
}
