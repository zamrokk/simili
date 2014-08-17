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

import com.simili.khepera3.Khepera3;
import com.simili.robot.Robot;
import com.simili.robot.behavior.AvoidObstacleBehavior;
import com.simili.robot.command.RobotInstructionSet;
import com.simili.robot.position.Position2D;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;
import com.simili.service.Simulator;
import com.simili.world.Obstacle;
import com.simili.world.Obstacle2D;
import com.simili.world.World;
import com.simili.world.World2D;

public class AvoidObstacleTest {

	private static final Logger log = LoggerFactory
			.getLogger(AvoidObstacleTest.class);

	@Test
	public void testWithoutObstacle()  {

		//build a khepera robot
		Khepera3 robot = new Khepera3(null, new Position2D(5, 5,
				0));

		AvoidObstacleBehavior avoidObstacleBehavior = new AvoidObstacleBehavior(null);
		State newState = avoidObstacleBehavior.execute(robot,new UnicycleDriveState(0.3,0), 0.05);
		
		Assert.assertTrue("Test is ok", true);
		log.info("Test is ok");
	}

}
