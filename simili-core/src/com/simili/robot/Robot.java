package com.simili.robot;

import java.util.List;

import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.decision.DecisionIntelligence;
import com.simili.robot.dynamics.Dynamics;
import com.simili.robot.position.Position;
import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.Sensor;
import com.simili.robot.state.State;
import com.simili.world.Object2D;

public abstract class Robot extends Object2D implements DecisionIntelligence {

	protected String name;

	// to inject
	protected State state;

	// to inject
	protected Dynamics dynamics;

	protected List<Sensor> sensorList;

	// to inject
	protected RobotInstructionSet robotInstructionSet;

	public RobotInstructionSet getRobotInstructionSet() {
		return robotInstructionSet;
	}

	public Robot(String name, State state, Position2D position,
			Dynamics dynamics, List<Sensor> sensorList,
			RobotInstructionSet robotInstructionSet) {
		super(position, null);
		this.name = name;
		this.state = state;
		this.dynamics = dynamics;
		this.sensorList = sensorList;
		this.robotInstructionSet = robotInstructionSet;
	}

	public Dynamics getDynamics() throws Exception {
		return dynamics;
	}

	public List<Sensor> getSensorList() {
		return sensorList;
	}

	public State getState() throws Exception {
		return state;
	}

	public String getName() {
		return name;
	}

	/**
	 * Reading data from sensors, refresh position and state<br>
	 * Approximates the location of the robot. It should be called from the
	 * execute function every iteration. The location of the robot is updated
	 * based on the difference to the previous wheel encoder ticks. This is only
	 * an approximation.<br>
	 * <br>
	 * state_estimate is updated with the new location and the measured wheel
	 * encoder tick counts are stored in prev_ticks.
	 * 
	 * @return the new estimate position of the robot. Can be used for GUI
	 *         rendering
	 */
	public abstract Position updateOdometry();

	/**
	 * A B C K search for matrices controlling the robot Computing input to
	 * apply and send instructions
	 */
	public abstract void computeAndInstructInputs();

	public abstract double getCruiseLinearVelocity() ;

	public abstract double getMaxLinearVelocity() ;

}
