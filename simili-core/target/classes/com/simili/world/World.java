package com.simili.world;

import java.util.List;

import com.simili.robot.Robot;

public abstract class World<ROBOT extends Robot<?, ?, ?>> {

	private List<ROBOT> robotList;

	private List<Obstacle> obstacleList;

	public World(List<ROBOT> robotList, List<Obstacle> obstacleList) {
		this.robotList = robotList;
		this.obstacleList = obstacleList;
	}

	public List<ROBOT> getRobotList() {
		return robotList;
	}

	public List<Obstacle> getObstacleList() {
		return obstacleList;
	}

	public void setRobotList(List<ROBOT> robotList) {
		this.robotList = robotList;
	}

	public void setObstacleList(List<Obstacle> obstacleList) {
		this.obstacleList = obstacleList;
	}

}
