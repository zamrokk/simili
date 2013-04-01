package com.simili.khepera3;

import com.simili.robot.command.Command;
import com.simili.robot.command.RobotInstructionSet;

public class K3RobotInstructionSet extends RobotInstructionSet {
	

	@Override
	public String sendInstruction(Command command, String... arguments) {
		String output = "dummy";//TODO real api for the robot here
		return output;
	}

}
