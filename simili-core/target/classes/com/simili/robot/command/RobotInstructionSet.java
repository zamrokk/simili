package com.simili.robot.command;

public abstract class RobotInstructionSet {

	public abstract String sendInstruction(Command command, String... arguments);

}
