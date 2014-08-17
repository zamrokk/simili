package com.simili.robot.command;

import com.simili.robot.Robot;

public abstract class RobotInstructionSet<ROBOT extends Robot<?, ?, ?>,COMMAND extends Command<ROBOT>> {

	public abstract String sendInstruction(ROBOT robot, COMMAND command,
			String... arguments);

}
