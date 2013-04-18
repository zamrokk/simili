package com.simili.world;

import java.util.List;

import com.simili.robot.position.Position2D;

public class Object2D {

	protected Position2D centerPosition;

	protected List<Position2D> shapePointList;

	public Object2D(Position2D centerPosition, List<Position2D> shapePointList) {
		this.centerPosition = centerPosition;
		this.shapePointList = shapePointList;
	}

	public Object2D() {
	}

	public Position2D getCenterPosition() {
		return centerPosition;
	}

	public List<Position2D> getShapePointList() {
		return shapePointList;
	}

}
