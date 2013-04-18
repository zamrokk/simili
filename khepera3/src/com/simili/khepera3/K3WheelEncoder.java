package com.simili.khepera3;


import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.sensor.Sensor;

public class K3WheelEncoder extends Sensor<Integer> {

	private Logger log = LoggerFactory.getLogger(K3WheelEncoder.class);
	
	public enum SIDES {
		RIGHT, LEFT
	};

	private K3Command COMMAND;

	// radius of the wheel
	public final double wheel_radius;
	// distance between the wheels
	public final double wheel_base_length;
	// ticks per revolution for the wheel
	public final int ticks_per_rev;

	// position
	private String side;
	// current tick
	public Integer currentTick;
	//last value
	public Integer lastTick;
	//last angularVelocity
	public double lastAngularVelocity;

	public K3WheelEncoder(SIDES name, RobotInstructionSet robotInstructionSet,
			SIDES side, double wheel_radius, double wheel_base_length,
			int ticks_per_rev) {
		super(name.name(), robotInstructionSet);
		lastTick=0;
		currentTick = 0;
		this.wheel_radius = wheel_radius;
		this.wheel_base_length = wheel_base_length;
		this.ticks_per_rev = ticks_per_rev;
	}

	public String getSide() {
		return side;
	}

	@Override
	public Integer getNewValue() {
		String outputResponse = robotInstructionSet.sendInstruction(
				COMMAND.READ_TICKS, name);
		Integer newValue = Integer.parseInt(outputResponse);
		lastTick = currentTick;
		currentTick = newValue;
		return newValue;
	}

	@Override
	public Integer getLastValue() {
		return lastTick;
	}

	public void forceSpeed(double v) {
		robotInstructionSet.sendInstruction(COMMAND.FORCE_SPEED, name, "" + v);
	}

	public Integer distance2ticks(double distance) {
		// ticks =
		// ceil((distance*wheel_radius*obj.ticks_per_rev)/(2*pi*wheel_radius));
		int value = (new BigDecimal((distance * ticks_per_rev) / (2 * Math.PI)))
				.setScale(0,RoundingMode.UP).intValue();
		return value;

	}

	/**
	 * 
	 * @param ticks
	 * @return distance in radian
	 */
	public Double ticks2distance(Integer ticks) {
		// distance =
		// (ticks*2*pi*wheel_radius)/(wheel_radius*wheel_ticks_per_rev);
		return (ticks * 2 * Math.PI) / ticks_per_rev;

	}

	public static Integer distance2ticks(double distance, int ticks_per_rev) {
		// ticks =
		// ceil((distance*wheel_radius*obj.ticks_per_rev)/(2*pi*wheel_radius));
		return (new Double((distance * ticks_per_rev) / (2 * Math.PI)))
				.intValue();

	}

	/**
	 * 
	 * @param ticks
	 * @param ticks_per_rev
	 * @return radian
	 */
	public static Double ticks2distance(Integer ticks, int ticks_per_rev) {
		// distance =
		// (ticks*2*pi*wheel_radius)/(wheel_radius*wheel_ticks_per_rev);
		return (ticks * 2 * Math.PI) / ticks_per_rev;

	}

	public void updateTicks(double velocity, double delta_t) {
		currentTick += distance2ticks(velocity * delta_t);
		log.debug(name+" tick updated to : "+currentTick+" for effective speed of "+velocity+" rad/s");
		this.lastAngularVelocity=velocity;
	}

	public double getLastAngularVelocity() {
		return this.lastAngularVelocity;
	}
}
