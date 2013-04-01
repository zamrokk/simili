package com.simili.gui;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SimulatorController {

	
	@RequestMapping(value = "/simulator", method = RequestMethod.GET)
	public String home(Model model,@RequestParam(value="robotType", required=true) String robotType) {
		model.addAttribute("robotType", robotType);

		return "simulator";
	}
	
	
}
