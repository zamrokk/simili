package com.simili.world;

import java.util.List;

import com.simili.robot.position.Position2D;

public class Obstacle2D extends Object2D implements Obstacle {

	public Obstacle2D(Position2D centerPosition, List<Position2D> shapePointList) {
		super(centerPosition, shapePointList);
	}

}
