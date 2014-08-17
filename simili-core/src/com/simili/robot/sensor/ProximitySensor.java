package com.simili.robot.sensor;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;

public abstract class ProximitySensor<ROBOT extends Robot<? extends ProximitySensor<ROBOT>,? extends WheelEncoder<ROBOT>,?>> extends Sensor<ROBOT, Integer> {

	/**
	 * Define the importance of this sensor compare to others. Default value is
	 * 1 but if this sensor should not be so important the value should between
	 * 0 and 1. It can be use while managing all sensors as a whole
	 */
	public double importance = 1;

	public ProximitySensor(ROBOT robot, String name, Position position) {
		super(robot, name, position);
	}

	/**
	 * To get the real distance form obstacle in meters. Don't matter what is
	 * the internal value of the sensor
	 * 
	 * @return real distance from an obstacle in meters
	 */
	public abstract double getDistanceValue();

}
