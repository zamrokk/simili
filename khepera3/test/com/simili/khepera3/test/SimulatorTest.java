package com.simili.khepera3.test;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.khepera3.Khepera3;
import com.simili.khepera3.mock.K3RobotInstructionSetMOCK;
import com.simili.robot.position.Position2D;
import com.simili.service.Simulator;
import com.simili.world.World2D;

public class SimulatorTest {

	private static final Logger log = LoggerFactory
			.getLogger(SimulatorTest.class);

	@Test
	public void test() {
		//prepare mock to reply to the robot sensors
		K3RobotInstructionSetMOCK mock = new K3RobotInstructionSetMOCK();
		
		Khepera3 robot = new Khepera3(mock,new Position2D(5, 5, 0));
		
		//inject robot and physics to the mock
		mock.setRobot(robot);
		mock.setWorld(new World2D(null, null));
	
		Simulator simulator = new Simulator(robot);

		log.info(Thread.currentThread().getName()
				+ " is launching simulator...");
		simulator.start();
		try {
			simulator.join();
		} catch (InterruptedException e) {
			Assert.fail("Test is ko" + "\n" + e.getMessage() + "\n"
					+ e.getStackTrace().toString());
		}
		Assert.assertTrue("Test is ok", true);
		log.info("Test is ok");
	}

}
