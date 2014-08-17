package com.simili.world;

import com.simili.robot.position.Position2D;

public class Obstacle2D extends Object2D implements Obstacle {

	public double radius;

	public Obstacle2D(Position2D centerPosition) {
		super(centerPosition);
	}

}
