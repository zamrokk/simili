package com.simili.robot.sensor;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;

public abstract class WheelEncoder<ROBOT extends Robot<? extends ProximitySensor<ROBOT>,? extends WheelEncoder<ROBOT>,?>>
		extends Sensor<ROBOT,Integer> {

	public WheelEncoder(ROBOT robot, String name, Position position) {
		super(robot, name, position);
	}

}
