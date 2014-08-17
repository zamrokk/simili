package com.simili.world;

import java.util.List;

import com.simili.robot.Robot;

public class World2D<ROBOT extends Robot<?, ?, ?>> extends World<ROBOT> {

	public World2D(List<ROBOT> robotList, List<Obstacle> obstacleList) {
		super(robotList, obstacleList);
	}

}
