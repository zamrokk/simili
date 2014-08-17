package com.simili.robot;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.decision.DecisionIntelligence;
import com.simili.robot.dynamics.Dynamics;
import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.ProximitySensor;
import com.simili.robot.sensor.WheelEncoder;
import com.simili.robot.state.State;
import com.simili.world.Object2D;

public abstract class Robot<PROXIMITY_SENSOR extends ProximitySensor<?>, WHEEL_ENCODER extends WheelEncoder<?>,INSTRUCTIONSET extends RobotInstructionSet<?,?>>
		extends Object2D implements DecisionIntelligence, Serializable,
		Runnable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected State state;

	protected Dynamics dynamics;

	protected List<PROXIMITY_SENSOR> proximitySensorList;

	protected List<WHEEL_ENCODER> whellEncoderSensorList;

	@JsonIgnore
	protected INSTRUCTIONSET robotInstructionSet;

	public INSTRUCTIONSET getRobotInstructionSet() {
		return robotInstructionSet;
	}

	public void setRobotInstructionSet(
			INSTRUCTIONSET robotInstructionSet) {
		this.robotInstructionSet = robotInstructionSet;
	}

	public Robot(
			String name,
			State state,
			Position2D position,
			Dynamics dynamics,
			List<PROXIMITY_SENSOR> proximitySensorList,
			List<WHEEL_ENCODER> whellEncoderSensorList,
			INSTRUCTIONSET robotInstructionSet) {
		super(position);
		this.robotInstructionSet = robotInstructionSet;
		this.name = name;
		this.state = state;
		this.dynamics = dynamics;
		this.proximitySensorList = proximitySensorList;
		this.whellEncoderSensorList = whellEncoderSensorList;
	}

	public Dynamics getDynamics() throws Exception {
		return dynamics;
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
	 */
	public abstract void updateOdometry();

	/**
	 * A B C K search for matrices controlling the robot Computing input to
	 * apply and send instructions
	 */
	public abstract void computeAndInstructInputs();

	public abstract double getCruiseLinearVelocity();

	public abstract double getMaxLinearVelocity();

	public abstract double getMaxangularvelocity();

	public abstract double getFrequency();

	public List<PROXIMITY_SENSOR> getProximitySensorList() {
		return proximitySensorList;
	}

	public List<WHEEL_ENCODER> getWhellEncoderSensorList() {
		return whellEncoderSensorList;
	}
}
