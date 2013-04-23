package com.simili.khepera3;

import com.simili.robot.Robot;
import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.Sensor;

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
public class K3ProximitySensor extends Sensor<Integer> {

	// angle relative to the robot orientation
	private Integer angle = 0;

	private K3Command COMMAND;

	public static enum NAMES {
		IR128, IR75, IR13, IR42, IR_13, IR_42, IR_75, IR_128, IR_180
	};

	public static final Integer maximumDistance = 3960;

	public K3ProximitySensor(Robot robot, NAMES name, Integer angle) {
		super(robot, name.toString());
		// by default value is at max at starting
		value = maximumDistance;
	}

	@Override
	public Integer getNewValue() {
		String outputResponse = robot.getRobotInstructionSet().sendInstruction(
				robot, COMMAND.READ_IR, name);
		return outputResponse == null ? 0 : Integer.parseInt(outputResponse);
	}

	@Override
	public Integer getLastValue() {
		return value;
	}

	public boolean isSomethingAround() {
		return value < maximumDistance;
	}

	public double getDistancetoNearestObject() {
		return convertValueToDistance();
	}

	private double convertValueToDistance() {
		return 0.02 - Math.log(value / 3960) / 30;
	}

	public Position2D getRelativePositiontoNearestObject() {
		Double objectDistance = getDistancetoNearestObject();
		return new Position2D(objectDistance * Math.cos(angle), objectDistance
				* Math.sin(angle), angle);
	}
}
