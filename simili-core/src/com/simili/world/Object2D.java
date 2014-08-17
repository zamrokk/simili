package com.simili.world;

import com.simili.robot.position.Position2D;

public class Object2D {

	protected Position2D centerPosition;

	public Object2D(Position2D centerPosition) {
		this.centerPosition = centerPosition;
	}

	public Object2D() {
	}

	public Position2D getCenterPosition() {
		return centerPosition;
	}

}
