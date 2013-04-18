package com.simili.khepera3;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.behavior.AvoidObstacleBehavior;
import com.simili.robot.behavior.Behavior;
import com.simili.robot.behavior.GoToGoalBehavior;
import com.simili.robot.behavior.StopBehaviour;
import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.dynamics.DifferencialDriveDynamics;
import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.Sensor;
import com.simili.robot.state.DifferencialDriveState;
import com.simili.robot.state.UnicycleDriveState;

/**
 * 
 * @author zam
 * 
 *         Representation of the Khepera3 robot
 * 
 */

public class Khepera3 extends Robot {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(Khepera3.class);

	public final static double wheel_radius = 0.021; // 42mm diameter
	private final static double wheel_base_length = 0.0885; // 88.5mm
	private final static int ticks_per_rev = 2765;
	public final static double speed_factor = 6.2953e-3;

	private final static double minimumDistanceToObject = 0.1; // 10cm
	private final static double acceptableStopDistance = 0.05; // 1cm
	private final static double maxLinearVelocity = 0.3; // 30cm/s
	private final static double cruiseLinearVelocity = 0.25; // 25cm/s

	private final static double maxAngularVelocity = 2.765; // 2.765rad/s

	public final static double frequency = 50; // en ms

	public double gainKp = 0.1;
	public double gainKi = 0.2;
	public double gainKd = 0.1;

	private UnicycleDriveState unicycleDriveState = null;

	private K3WheelEncoder wheelEncoderR;
	private K3WheelEncoder wheelEncoderL;

	private K3ProximitySensor proximitySensor128;
	private K3ProximitySensor proximitySensor75;
	private K3ProximitySensor proximitySensor42;
	private K3ProximitySensor proximitySensor13;
	private K3ProximitySensor proximitySensor_13;
	private K3ProximitySensor proximitySensor_42;
	private K3ProximitySensor proximitySensor_75;
	private K3ProximitySensor proximitySensor_128;
	private K3ProximitySensor proximitySensor_180;

	private Position2D goalPosition;

	public Khepera3() {
		super();
	}

	public Khepera3(RobotInstructionSet robotInstructionSet,
			Position2D goalPosition) {
		super("Khepera3", new DifferencialDriveState(),
				new Position2D(0, 0, 0), new DifferencialDriveDynamics(
						wheel_radius, wheel_base_length),
				new ArrayList<Sensor>(), robotInstructionSet);
		this.goalPosition = goalPosition;

		wheelEncoderR = new K3WheelEncoder(K3WheelEncoder.SIDES.RIGHT,
				robotInstructionSet, K3WheelEncoder.SIDES.RIGHT, wheel_radius,
				wheel_base_length, ticks_per_rev);
		wheelEncoderL = new K3WheelEncoder(K3WheelEncoder.SIDES.LEFT,
				robotInstructionSet, K3WheelEncoder.SIDES.LEFT, wheel_radius,
				wheel_base_length, ticks_per_rev);
		proximitySensor128 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR128, 128, robotInstructionSet);
		proximitySensor75 = new K3ProximitySensor(K3ProximitySensor.NAMES.IR75,
				75, robotInstructionSet);
		proximitySensor42 = new K3ProximitySensor(K3ProximitySensor.NAMES.IR42,
				42, robotInstructionSet);
		proximitySensor13 = new K3ProximitySensor(K3ProximitySensor.NAMES.IR13,
				13, robotInstructionSet);
		proximitySensor_13 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR_13, -13, robotInstructionSet);
		proximitySensor_42 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR_42, -42, robotInstructionSet);
		proximitySensor_75 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR_75, -75, robotInstructionSet);
		proximitySensor_128 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR_128, -128, robotInstructionSet);
		proximitySensor_180 = new K3ProximitySensor(
				K3ProximitySensor.NAMES.IR_180, -180, robotInstructionSet);

		sensorList.add(wheelEncoderR);
		sensorList.add(wheelEncoderL);
		sensorList.add(proximitySensor128);
		sensorList.add(proximitySensor75);
		sensorList.add(proximitySensor42);
		sensorList.add(proximitySensor13);
		sensorList.add(proximitySensor_13);
		sensorList.add(proximitySensor_42);
		sensorList.add(proximitySensor_75);
		sensorList.add(proximitySensor_128);
		sensorList.add(proximitySensor_180);

		log.info("Sensors added.");

	}

	@Override
	public void updateOdometry() {

		// Recal the previous wheel encoder ticks
		int prev_right_ticks = wheelEncoderR.getLastValue();
		int prev_left_ticks = wheelEncoderL.getLastValue();

		// Get wheel encoder ticks from the robot
		int right_ticks = wheelEncoderR.getNewValue();
		int left_ticks = wheelEncoderL.getNewValue();

		// Compute odometry here
		double L = wheel_base_length;

		double r_right = K3WheelEncoder.ticks2distance(right_ticks
				- prev_right_ticks, wheelEncoderR.ticks_per_rev);
		double d_right = r_right * wheel_radius;

		double r_left = K3WheelEncoder.ticks2distance(left_ticks
				- prev_left_ticks, wheelEncoderL.ticks_per_rev);
		double d_left = r_left * wheel_radius;

		double d_center = (d_right + d_left) / 2;

		double x_dt = d_center * Math.cos(((Position2D) centerPosition).theta);
		double y_dt = d_center * Math.sin(((Position2D) centerPosition).theta);

		double theta_dt = (r_right - r_left) / L; // rad

		double theta_new = Math.atan2(
				Math.sin(((Position2D) centerPosition).theta + theta_dt),
				Math.cos(((Position2D) centerPosition).theta + theta_dt));
		double x_new = ((Position2D) centerPosition).x + x_dt;
		double y_new = ((Position2D) centerPosition).y + y_dt;

		// Update your estimate of (x,y,theta)
		centerPosition = new Position2D(x_new, y_new, theta_new);

		// Update new state (v_right,v_left)
		state = new DifferencialDriveState((r_right / frequency) * 1000,
				(r_left / frequency) * 1000);
		unicycleDriveState = ((DifferencialDriveDynamics) dynamics)
				.differential2unicycle((r_right / frequency) * 1000,
						(r_left / frequency) * 1000);

		log.info("*** ODOMETRY ***");
		log.info("x=" + centerPosition.x + ",y=" + centerPosition.y + ",theta="
				+ centerPosition.theta);
		log.info("v=" + unicycleDriveState.v + " m/s,w=" + unicycleDriveState.w
				+ "rad/s");
		log.info("w_rigth=" + ((DifferencialDriveState) state).v_right
				+ " rad/s,w_left=" + ((DifferencialDriveState) state).v_left
				+ "rad/s");

	}

	public double getMaxangularvelocity() {
		return maxAngularVelocity;
	}

	public UnicycleDriveState getUnicycleDriveState() {
		return unicycleDriveState;
	}

	public void setUnicycleDriveState(UnicycleDriveState unicycleDriveState) {
		this.unicycleDriveState = unicycleDriveState;
	}

	@Override
	public void computeAndInstructInputs() {

		// is what i would like,let see what the behavior says ...
		UnicycleDriveState state = (UnicycleDriveState) chooseDecision()
				.execute(this, new UnicycleDriveState(cruiseLinearVelocity, 0),
						frequency / 1000);

		log.debug("wanted v:" + state.v + ",w:" + state.w);

		DifferencialDriveState diffState = ((DifferencialDriveDynamics) dynamics)
				.unicycle2differential((Position2D) centerPosition, state);

		log.debug("wanted v_right:" + diffState.v_right + ",v_left:"
				+ diffState.v_left);

		double maxValue = Math.max(Math.abs(diffState.v_right),
				Math.abs(diffState.v_left));

		double newV_right = diffState.v_right;
		double newV_left = diffState.v_left;

		if (maxValue > maxAngularVelocity) {
			newV_right = (diffState.v_right / maxValue) * maxAngularVelocity;
			newV_left = (diffState.v_left / maxValue) * maxAngularVelocity;
			log.info("Rescaling angular speed due to robots' limitations of max angular speed : "
					+ maxAngularVelocity);
			log.debug("finally v_right:" + newV_right + ",v_left:" + newV_left);
		}

		wheelEncoderR.forceSpeed(newV_right);
		wheelEncoderL.forceSpeed(newV_left);

	}

	public Behavior chooseDecision() {

		Behavior behavior = null;

		if (checkAtGoal(goalPosition)) {
			behavior = new StopBehaviour();
		} else {
			Position2D obstaclePosition = checkAtObstacle();
			if (obstaclePosition != null) {
				behavior = new AvoidObstacleBehavior(obstaclePosition);
			} else {
				behavior = new GoToGoalBehavior(goalPosition, gainKp, gainKi,
						gainKd);
			}
		}

		return behavior;
	}

	private Position2D checkAtObstacle() {
		Position2D obstaclePosition = null;
		double mimumObstacleDistance = Double.MAX_VALUE;
		for (Sensor sensor : sensorList) {
			if (sensor instanceof K3ProximitySensor) {
				K3ProximitySensor irps = (K3ProximitySensor) sensor;
				if (irps.isSomethingAround()
						&& irps.getDistancetoNearestObject() < mimumObstacleDistance) {
					obstaclePosition = Position2D.addPositions(
							irps.getRelativePositiontoNearestObject(),
							(Position2D) centerPosition);
				}
			}
		}
		return obstaclePosition;
	}

	private boolean checkAtGoal(Position2D goalPosition) {

		boolean goalReached = false;

		// Test distance from goal
		double distanceFromGoal = Math.hypot(((Position2D) centerPosition).x
				- goalPosition.x, ((Position2D) centerPosition).y
				- goalPosition.y);
		log.info("Distance from Goal : " + distanceFromGoal + " m.");
		if (distanceFromGoal < acceptableStopDistance) {
			goalReached = true;
			log.info("  ***** !!!!!! GOAL REACHED !!!!!!  *****");
		}

		return goalReached;
	}

	protected boolean checkObstacleCleared() {

		boolean obstaclefound = false;

		for (Sensor sensor : sensorList) {
			if (sensor instanceof K3ProximitySensor) {
				K3ProximitySensor ps = (K3ProximitySensor) sensor;
				if (ps.isSomethingAround()
						&& (ps.getDistancetoNearestObject() < minimumDistanceToObject)) {
					obstaclefound = true;
					break;
				}
			}
		}

		return obstaclefound;

	}

	@Override
	public double getCruiseLinearVelocity() {
		return cruiseLinearVelocity;
	}

	@Override
	public double getMaxLinearVelocity() {
		return maxLinearVelocity;
	}

	@Override
	public double getFrequency() {
		return frequency;
	}

}
