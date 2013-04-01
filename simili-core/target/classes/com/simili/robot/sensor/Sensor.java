package com.simili.robot.sensor;

import com.simili.robot.command.RobotInstructionSet;

public abstract class Sensor<T> {

	protected String name;
	
	protected RobotInstructionSet robotInstructionSet;

	protected T value;

	/**
	 * Read new value coming from the sensor and replace old value by this one
	 * To do this it sends a message to the server requesting the information
	 * from the sensors. This data is then split up and assigned to its
	 * appropriate sensor.
	 * 
	 * @return a generic type depending on the sensor implementation
	 */
	public abstract T getNewValue();

	/**
	 * Return the last value recorded by the sensor
	 * 
	 * @return a generic type depending on the sensor implementation
	 */
	public abstract T getLastValue();

	public Sensor(String name, RobotInstructionSet robotInstructionSet ) {
		this.name = name;
		this.robotInstructionSet = robotInstructionSet;
	}

	public String getName() {
		return name;
	}

}
