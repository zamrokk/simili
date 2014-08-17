package com.simili.khepera3;

import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.ProximitySensor;

/**
 * 
 * @author zam
 * 
 *         A class for reading the Khepera 3's IR sensors. The IR sensors
 *         measure the amount of infrared (IR) light presents at the sensor. The
 *         sensor returns an integer where the smaller the number, the stronger
 *         the IR light is. Values are in the range of [18,3960] instead of the
 *         measured distances. The Khepera 3 has 11 IR sensors. Nine sensors are
 *         outward facing and two are downward facing. Figure 1 below shows the
 *         locations of the sensors and the index of the sensor value.
 * 
 */
public class K3ProximitySensor extends ProximitySensor<Khepera3> {

	public static final int INTERNAL_DISTANCE_MAX = 3960;

	public static final int INTERNAL_DISTANCE_MIN = 0;

	public static final double DISTANCE_MIN = 0.02;

	public static final double DISTANCE_MAX = 0.2;

	public static enum NAMES {
		IR128, IR75, IR13, IR42, IR_13, IR_42, IR_75, IR_128, IR_180
	};

	/**
	 * 
	 * @param robot
	 * @param name
	 * @param position
	 *            of the sensor relative to the robot
	 */
	public K3ProximitySensor(Khepera3 robot, NAMES name, Position2D position) {
		super(robot, name.toString(), position);
		// mean nothing detected
		value = INTERNAL_DISTANCE_MIN;
		// allow importance to sensors
		switch (name) {
		case IR_180:
			importance = 1;
			break;
		case IR_128:
			importance = 1;
			break;
		case IR128:
			importance = 1;
			break;
		case IR75:
			importance = 1;
			break;
		case IR_75:
			importance = 1;
			break;
		case IR42:
			importance = 1;
			break;
		case IR_42:
			importance = 1;
			break;
		case IR13:
			importance = 1;
			break;
		case IR_13:
			importance = 1;
			break;
		}
	}

	@Override
	public Integer getNewValue() {
		String outputResponse = robot.getRobotInstructionSet().sendInstruction(
				robot, K3Command.READ_IR, name);
		return outputResponse == null ? 0 : Integer.parseInt(outputResponse);
	}

	@Override
	public Integer getLastValue() {
		return value;
	}

	public boolean isSomethingAround() {
		return value > 0;
	}

	public double getDistancetoNearestObject() {
		return convertValueToDistance();
	}

	private double convertValueToDistance() {
		return DISTANCE_MIN
				- Math.log(new Double(value) / INTERNAL_DISTANCE_MAX) / 30;
	}

	public Position2D getRelativePositiontoNearestObject() {
		Double objectDistance = getDistancetoNearestObject();
		double angle = ((Position2D) position).theta;
		return new Position2D(objectDistance * Math.cos(angle), objectDistance
				* Math.sin(angle), angle);
	}

	public void updateDistanceValue(int valueToForce) {
		value = valueToForce;
	}

	public static int convertDistance2Value(double distance) {
		if (distance < DISTANCE_MIN) {
			return INTERNAL_DISTANCE_MAX;
		} else if (distance < DISTANCE_MAX) {
			return new Double(Math.exp((distance - DISTANCE_MIN) * -30)
					* INTERNAL_DISTANCE_MAX).intValue();
		} else {
			return 0;
		}
	}

	private static double convertValueToDistance(int value) {
		if (value <= 0) {
			return DISTANCE_MAX;
		} else {
			return DISTANCE_MIN
					- Math.log(new Double(value) / INTERNAL_DISTANCE_MAX) / 30;
		}
	}

	@Override
	public double getDistanceValue() {
		return convertValueToDistance();
	}

}
