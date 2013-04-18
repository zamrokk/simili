package com.simili.service;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.position.Position2D;

public class Simulator extends Thread {

	private static final Logger log = LoggerFactory.getLogger(Simulator.class);

	private Robot robot = null;

	private Timestamp elapseTime;

	private long start = 0;

	public Simulator(Robot robot) {
		this.robot = robot;
	}

	@Override
	public void run() {

		start = System.currentTimeMillis();

		log.info("Simulation starting...");

		// Infinite loop

		while (!isInterrupted()) {
			try {
				sleep((int) robot.getFrequency());
			} catch (InterruptedException e) {
				log.error("Simulator has received an interruption.\n", e);
				return;
			}

			log.info(" *** Reading data from sensors, refresh position and state ***");
			robot.updateOdometry();

			log.info(" *** Computing input to apply and send instructions ***");
			robot.computeAndInstructInputs();

		}

	}

	public void cancel() {
		interrupt();
		elapseTime = new Timestamp(System.currentTimeMillis() - start);
		log.warn("Simulation interrupted after " + elapseTime.getTime() + "ms");
	}

	public Robot getRobot() {
		return robot;
	}

	public Timestamp getElapseTime() {
		return elapseTime;
	}

}
