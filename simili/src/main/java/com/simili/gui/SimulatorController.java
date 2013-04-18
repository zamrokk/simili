package com.simili.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simili.khepera3.Khepera3;
import com.simili.khepera3.mock.K3RobotInstructionSetMOCK;
import com.simili.robot.position.Position2D;
import com.simili.service.Simulator;
import com.simili.world.World2D;

@Controller
public class SimulatorController {

	private static final Logger log = LoggerFactory
			.getLogger(SimulatorController.class);

	Simulator simulator;
	Khepera3 robot;
	K3RobotInstructionSetMOCK mock;

	public SimulatorController() {

		reset();

	}

	@RequestMapping(value = "/simulator", method = RequestMethod.GET)
	public String home(Model model,
			@RequestParam(value = "robotType", required = true) String robotType) {
		model.addAttribute("robotType", robotType);

		return "simulator";
	}

	@RequestMapping(value = "/simulator/start", method = RequestMethod.POST)
	public String start(Model model) {

		if (simulator == null || !simulator.isAlive()) {
			log.info(Thread.currentThread().getName()
					+ " is launching new simulator...");
			simulator = new Simulator(robot);
			simulator.start();
		}

		return "simulator";
	}

	@RequestMapping(value = "/simulator/stop", method = RequestMethod.POST)
	public String stop() {

		if (simulator != null && simulator.isAlive()) {
			log.info(Thread.currentThread().getName()
					+ " is stopping simulator...");
			simulator.cancel();
		}

		return "simulator";
	}

	@RequestMapping(value = "/simulator/reset", method = RequestMethod.POST)
	public String reset() {

		stop();
		// reinit new robot
		mock = new K3RobotInstructionSetMOCK();
		robot = new Khepera3(mock, new Position2D(5, 5, 0));

		// inject robot and physics to the mock
		mock.setRobot(robot);
		mock.setWorld(new World2D(null, null));

		return "simulator";
	}

	@RequestMapping(value = "/simulator/getRobot", method = RequestMethod.POST)
	public @ResponseBody
	Khepera3 getRobot(Model model) {

		//check if object is json-able
		ObjectMapper mapper = new ObjectMapper();
		String s = null;
		 try {
			s = mapper.writeValueAsString(robot);
		} catch (JsonGenerationException e) {
			log.error("",e);
		} catch (JsonMappingException e) {
			log.error("",e);
		} catch (IOException e) {
			log.error("",e);
		}
		 
		return robot;
	}

}
