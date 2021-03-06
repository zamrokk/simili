package com.simili.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simili.khepera3.K3RobotInstructionSet;
import com.simili.khepera3.Khepera3;
import com.simili.robot.position.Position2D;
import com.simili.service.Simulator;
import com.simili.world.Obstacle;
import com.simili.world.Obstacle2D;
import com.simili.world.World;
import com.simili.world.World2D;

@Configuration
@Controller
public class SimulatorController {

	private static final Logger log = LoggerFactory
			.getLogger(SimulatorController.class);

	@Inject
	Simulator<Khepera3> simulator;
	@Inject
	K3RobotInstructionSet robotInstructionSet;

	@RequestMapping(value = "/simulator", method = RequestMethod.GET)
	public String home(Model model,
			@RequestParam(value = "robotType", required = true) String robotType) {
		model.addAttribute("robotType", robotType);

		return "simulator";
	}

	@RequestMapping(value = "/simulator/start", method = RequestMethod.POST)
	public String start(Model model) {

		if (!simulator.isAlive) {
			reset();
			simulator.run();
		}
		return "simulator";
	}

	@RequestMapping(value = "/simulator/stop", method = RequestMethod.POST)
	public String stop() {

		if (simulator.isAlive) {
			simulator.cancel();
		}

		return "simulator";
	}

	@RequestMapping(value = "/simulator/reset", method = RequestMethod.POST)
	public String reset() {

		stop();

		simulator.clear();

		// reinit new robot
		Khepera3 robot = new Khepera3(robotInstructionSet, new Position2D(5, 5,
				0));
		List<Khepera3> robotList = new ArrayList<Khepera3>();
		robotList.add(robot);
		((World2D<Khepera3>) simulator.getWorld()).setRobotList(robotList);

		// add obstacles
		Obstacle o1 = new Obstacle2D(new Position2D(0, 5, 0));
		Obstacle o2 = new Obstacle2D(new Position2D(5, 0, 0));
		List<Obstacle> obstacleList = new ArrayList<Obstacle>();
		obstacleList.add(o1);
		obstacleList.add(o2);

		((World2D<Khepera3>) simulator.getWorld())
				.setObstacleList(obstacleList);

		return "simulator";
	}

	@RequestMapping(value = "/simulator/getWorld", method = RequestMethod.POST)
	public @ResponseBody
	World<Khepera3> getWorld(Model model) {

		// check if object is json-able
		ObjectMapper mapper = new ObjectMapper();
		String s = null;
		try {
			s = mapper.writeValueAsString(simulator.getWorld());
		} catch (JsonGenerationException e) {
			log.error("", e);
			stop();
		} catch (JsonMappingException e) {
			log.error("", e);
			stop();
		} catch (IOException e) {
			log.error("", e);
			stop();
		}

		return simulator.getWorld();
	}

}
