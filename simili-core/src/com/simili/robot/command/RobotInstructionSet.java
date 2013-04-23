package com.simili.robot.command;

import com.simili.robot.Robot;

public abstract class RobotInstructionSet {

	public abstract String sendInstruction(Robot robot,Command command, String... arguments);

}
