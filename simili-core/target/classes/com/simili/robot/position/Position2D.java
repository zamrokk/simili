package com.simili.robot.position;

public class Position2D extends Position {

	public double x;
	public double y;
	public double theta;

	public Position2D() {
		x = 0;
		y = 0;
		theta = 0;
	}

	public Position2D(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	public static Position2D addPositions(Position2D p1, Position2D p2) {
		return new Position2D(p1.x + p2.x, p1.y + p2.y, Math.atan2(p1.theta,
				p2.theta));
	}

	public void addPositions(Position2D p1) {
		x += p1.x;
		y += p1.y;
		theta = Math.atan2(p1.theta, theta);
	}

	@Override
	public String toString() {
		return "(X=" + x + ",Y=" + y + ",theta=" + theta + ")";
	}

}
