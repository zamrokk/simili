package com.simili.khepera3;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.behavior.AvoidObstacleBehavior;
import com.simili.robot.behavior.Behavior;
import com.simili.robot.behavior.GoToGoalBehavior;
import com.simili.robot.behavior.StopBehaviour;
import com.simili.robot.dynamics.DifferencialDriveDynamics;
import com.simili.robot.position.Position2D;
import com.simili.robot.state.DifferencialDriveState;
import com.simili.robot.state.UnicycleDriveState;

/**
 * 
 * @author zam
 * 
 *         Representation of the Khepera3 robot
 * 
 */

public class Khepera3 extends Robot<K3ProximitySensor, K3WheelEncoder, K3RobotInstructionSet> {

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

	public final static double frequency = 0.050; // 50 m

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

	public Position2D goalPosition;

	public Khepera3(K3RobotInstructionSet robotInstructionSet,
			Position2D goalPosition) {
		super("Khepera3", new DifferencialDriveState(),
				new Position2D(0, 0, 0), new DifferencialDriveDynamics(
						wheel_radius, wheel_base_length),
				new ArrayList<K3ProximitySensor>(),
				new ArrayList<K3WheelEncoder>(), robotInstructionSet);
		this.goalPosition = goalPosition;

		wheelEncoderR = new K3WheelEncoder(this, K3WheelEncoder.SIDES.RIGHT,
				K3WheelEncoder.SIDES.RIGHT, wheel_radius, wheel_base_length,
				ticks_per_rev);
		wheelEncoderL = new K3WheelEncoder(this, K3WheelEncoder.SIDES.LEFT,
				K3WheelEncoder.SIDES.LEFT, wheel_radius, wheel_base_length,
				ticks_per_rev);
		proximitySensor128 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR128, new Position2D(0, 0, new Double(
						128) * Math.PI / 180));
		proximitySensor75 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR75, new Position2D(0, 0, new Double(
						75) * Math.PI / 180));
		proximitySensor42 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR42, new Position2D(0, 0, new Double(
						42) * Math.PI / 180));
		proximitySensor13 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR13, new Position2D(0, 0, new Double(
						13) * Math.PI / 180));
		proximitySensor_13 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR_13, new Position2D(0, 0, new Double(
						-13) * Math.PI / 180));
		proximitySensor_42 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR_42, new Position2D(0, 0, new Double(
						-42) * Math.PI / 180));
		proximitySensor_75 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR_75, new Position2D(0, 0, new Double(
						-75) * Math.PI / 180));
		proximitySensor_128 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR_128, new Position2D(0, 0,
						new Double(-128) * Math.PI / 180));
		proximitySensor_180 = new K3ProximitySensor(this,
				K3ProximitySensor.NAMES.IR_180, new Position2D(0, 0,
						new Double(-180) * Math.PI / 180));

		whellEncoderSensorList.add(wheelEncoderR);
		whellEncoderSensorList.add(wheelEncoderL);
		proximitySensorList.add(proximitySensor128);
		proximitySensorList.add(proximitySensor75);
		proximitySensorList.add(proximitySensor42);
		proximitySensorList.add(proximitySensor13);
		proximitySensorList.add(proximitySensor_13);
		proximitySensorList.add(proximitySensor_42);
		proximitySensorList.add(proximitySensor_75);
		proximitySensorList.add(proximitySensor_128);
		proximitySensorList.add(proximitySensor_180);

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

		log.debug("Get wheel encoder ticks from the robot. right_ticks="
				+ right_ticks + "-prev=" + prev_right_ticks + ",left_ticks="
				+ left_ticks + "-prev=" + prev_left_ticks);

		// Compute odometry here
		double L = wheel_base_length;

		double r_right = K3WheelEncoder.ticks2distance(right_ticks
				- prev_right_ticks, wheelEncoderR.ticks_per_rev);
		double d_right = r_right * wheel_radius;

		double r_left = K3WheelEncoder.ticks2distance(left_ticks
				- prev_left_ticks, wheelEncoderL.ticks_per_rev);
		double d_left = r_left * wheel_radius;

		double d_center = (d_right + d_left) / 2;

		log.debug("d_right=" + d_right + "m,d_left=" + d_left + "m");

		double x_dt = d_center * Math.cos(((Position2D) centerPosition).theta);
		double y_dt = d_center * Math.sin(((Position2D) centerPosition).theta);

		double theta_dt = (r_right - r_left) / L; // rad
		log.debug("x_dt=" + x_dt + "m,y_dt=" + y_dt + "m,theta_dt=" + theta_dt
				+ "rad");
		double theta_new = Math.atan2(
				Math.sin(((Position2D) centerPosition).theta + theta_dt),
				Math.cos(((Position2D) centerPosition).theta + theta_dt));
		double x_new = ((Position2D) centerPosition).x + x_dt;
		double y_new = ((Position2D) centerPosition).y + y_dt;

		// Update your estimate of (x,y,theta)
		centerPosition = new Position2D(x_new, y_new, theta_new);

		// Update new state (v_right,v_left)
		state = new DifferencialDriveState(r_right / frequency, r_left
				/ frequency);
		unicycleDriveState = ((DifferencialDriveDynamics) dynamics)
				.differential2unicycle(r_right / frequency, r_left / frequency);

		log.info("*** ODOMETRY ***");
		log.info("x=" + centerPosition.x + ",y=" + centerPosition.y + ",theta="
				+ centerPosition.theta);
		log.info("v=" + unicycleDriveState.v + " m/s,w=" + unicycleDriveState.w
				+ "rad/s");
		log.info("w_rigth=" + ((DifferencialDriveState) state).v_right
				+ " rad/s,w_left=" + ((DifferencialDriveState) state).v_left
				+ "rad/s");

		// now refreshing for proximity sensors

		proximitySensor128.getNewValue();
		proximitySensor75.getNewValue();
		proximitySensor42.getNewValue();
		proximitySensor13.getNewValue();
		proximitySensor_13.getNewValue();
		proximitySensor_42.getNewValue();
		proximitySensor_75.getNewValue();
		proximitySensor_128.getNewValue();
		proximitySensor_180.getNewValue();

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
						frequency);

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

	public Behavior<Khepera3> chooseDecision() {

		Behavior<Khepera3> behavior = null;

		if (checkAtGoal(goalPosition)) {
			behavior = new StopBehaviour<Khepera3>();
		} else {
			Position2D obstaclePosition = checkAtObstacle();
			if (obstaclePosition != null) {
				behavior = new AvoidObstacleBehavior<Khepera3>(obstaclePosition);
			} else {
				behavior = new GoToGoalBehavior<Khepera3>(goalPosition, gainKp,
						gainKi, gainKd);
			}
		}

		return behavior;
	}

	private Position2D checkAtObstacle() {
		Position2D obstaclePosition = null;
		double minimumObstacleDistance = minimumDistanceToObject;
		for (K3ProximitySensor sensor : proximitySensorList) {
			if (sensor.isSomethingAround()
					&& sensor.getDistancetoNearestObject() < minimumObstacleDistance) {
				obstaclePosition = Position2D.addPositions(
						sensor.getRelativePositiontoNearestObject(),
						(Position2D) centerPosition);
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

		for (K3ProximitySensor sensor : proximitySensorList) {

			if (sensor.isSomethingAround()
					&& (sensor.getDistancetoNearestObject() < minimumDistanceToObject)) {
				obstaclefound = true;
				break;
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

	@Override
	public void run() {

		log.info("Robot " + name + " is ON");

		// Infinite loop

		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep((int) (frequency * 1000));
			} catch (InterruptedException e) {
				log.warn("Robot " + name + " has stopped", e);
				return;
			}

			log.info("Robot "
					+ name
					+ " is reading data from sensors, refresh position and state");
			updateOdometry();

			log.info("Robot " + name
					+ " is computing input to apply and sending instructions");
			computeAndInstructInputs();

		}
	}

}
