package com.simili.khepera3;

import com.simili.robot.command.RobotInstructionSet;

public class K3RobotInstructionSet extends
		RobotInstructionSet<Khepera3, K3Command> {

	@Override
	public String sendInstruction(Khepera3 robot, K3Command command,
			String... arguments) {
		String output = "dummy";// TODO real api for the robot here
		return output;
	}

}
