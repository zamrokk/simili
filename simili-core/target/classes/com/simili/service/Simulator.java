package com.simili.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.simili.robot.Robot;
import com.simili.robot.position.Position2D;

public class Simulator extends Thread {

	private static final Log log = LogFactory.getLog(Simulator.class);

	private Robot robot = null;

	public Simulator(Robot robot) {
		this.robot = robot;
	}

	@Override
	public synchronized void start() {

		log.info("Simulation starting...");

		// Infinite loop
		for (int i = 0; i < 4000; i++) {
			try {
				sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info(" *** Reading data from sensors, refresh position and state ***");
			Position2D position = (Position2D) robot.updateOdometry();

			log.info("new position : " + position);

			log.info(" *** Computing input to apply and send instructions ***");
			robot.computeAndInstructInputs();

		}

		log.info("Simulation finished.");

	}

	public Robot getRobot() {
		return robot;
	}

}
