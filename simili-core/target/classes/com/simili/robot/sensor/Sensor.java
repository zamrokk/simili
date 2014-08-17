package com.simili.robot.sensor;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;

public abstract class Sensor<ROBOT extends Robot<? extends ProximitySensor<ROBOT>, ? extends WheelEncoder<ROBOT>,?>, T> {

	protected String name;

	protected ROBOT robot;

	public Position position;

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

	public Sensor(ROBOT robot, String name, Position position) {
		this.name = name;
		this.robot = robot;
		this.position = position;
	}

	public String getName() {
		return name;
	}

}
