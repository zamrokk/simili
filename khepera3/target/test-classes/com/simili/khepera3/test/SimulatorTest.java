package com.simili.khepera3.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.simili.khepera3.K3RobotInstructionSet;
import com.simili.khepera3.Khepera3;
import com.simili.robot.Robot;
import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.position.Position2D;
import com.simili.service.Simulator;
import com.simili.world.Obstacle;
import com.simili.world.Obstacle2D;
import com.simili.world.World;
import com.simili.world.World2D;

@ContextConfiguration(value = "classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SimulatorTest {

	private static final Logger log = LoggerFactory
			.getLogger(SimulatorTest.class);

	@Resource
	private ApplicationContext context;

	@Test
	public void test() throws InterruptedException {

		Simulator simulator = (Simulator) context.getBean("simulator");
		K3RobotInstructionSet robotInstructionSet = (K3RobotInstructionSet) context
				.getBean("robotInstructionSet");

		Khepera3 robot = new Khepera3(robotInstructionSet, new Position2D(5, 5,
				0));
		List<Khepera3> robotList = new ArrayList<Khepera3>();
		robotList.add(robot);

		// add obstacles
		Obstacle o1 = new Obstacle2D(new Position2D(0, 2, 0));
		Obstacle o2 = new Obstacle2D(new Position2D(2, 0, 0));
		List<Obstacle> obstacleList = new ArrayList<Obstacle>();
		obstacleList.add(o1);
		obstacleList.add(o2);

		World world = new World2D(robotList, obstacleList);

		simulator.setWorld(world);

		log.info(Thread.currentThread().getName()
				+ " is launching simulator...");

		simulator.run();

		// wait 10 s for the simulator then kill it and wait
		Thread.sleep(10000);
		simulator.cancel();

		Assert.assertTrue("Test is ok", true);
		log.info("Test is ok");
	}

}
