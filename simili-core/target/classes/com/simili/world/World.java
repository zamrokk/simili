package com.simili.world;

import java.util.List;

import com.simili.robot.Robot;

public abstract class World {

	private List<Robot> robotList;

	private List<Obstacle> obstacleList;

	public World(List<Robot> robotList, List<Obstacle> obstacleList) {
		this.robotList = robotList;
		this.obstacleList = obstacleList;
	}

	public List<Robot> getRobotList() {
		return robotList;
	}

	public List<Obstacle> getObstacleList() {
		return obstacleList;
	}

}
