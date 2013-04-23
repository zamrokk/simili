package com.simili.khepera3.mock;

import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.khepera3.K3Command;
import com.simili.khepera3.K3RobotInstructionSet;
import com.simili.khepera3.K3WheelEncoder;
import com.simili.robot.Robot;
import com.simili.robot.command.Command;
import com.simili.robot.sensor.Sensor;
import com.simili.service.Simulator;
import com.simili.world.World;

public class K3RobotInstructionSetMOCK extends K3RobotInstructionSet {

	private static final Logger log = LoggerFactory
			.getLogger(K3RobotInstructionSetMOCK.class);

	@JsonIgnore
	@Inject
	private Simulator simulator;

	@Override
	public String sendInstruction(Robot robot,Command command, String... arguments) {

		World world = simulator.getWorld();

		String argumentsTxt = "";
		for (String string : arguments) {
			argumentsTxt += string + ",";
		}
		log.debug("MOCK has received command :" + command + " with arguments :"
				+ argumentsTxt);

		String output = null;
		K3Command k3Command = (K3Command) command;

		switch (k3Command) {
		case FORCE_SPEED: {
			String side = arguments[0];
			double newV = Double.parseDouble(arguments[1]);
			double realVelocityApplied = 0;
			try {
				// search for correct encoder
				for (Sensor sensor : robot.getSensorList()) {
					if (sensor instanceof K3WheelEncoder
							&& ((K3WheelEncoder) (sensor)).getName().equals(
									side)) {
						K3WheelEncoder kwe = (K3WheelEncoder) sensor;
						realVelocityApplied = newV
								* (1 - Math.pow(
										(Math.abs(kwe.getLastAngularVelocity()
												- newV) / (2 * robot
												.getMaxangularvelocity())), 2))
								* 0.8;
						kwe.updateTicks(realVelocityApplied,
								robot.getFrequency());
					}
				}

				log.debug("MOCK has updated ticks of " + side
						+ " wheel encoder to real angular velocity : "
						+ realVelocityApplied);
			} catch (Exception e) {
				log.error("Cannot force speed", e);
			}
			break;
		}
		case READ_IR: {
			String name = arguments[0];
			// TODO ... for obstacle
			log.debug(" *** TO IMPLEMENT *** MOCK has detected obstacle for IR sensor "
					+ name);
			break;
		}
		case READ_TICKS: {
			// read last ticks
			String side = arguments[0];
			// add new perfect ticks on last velocity

			for (Sensor sensor : robot.getSensorList()) {
				if (sensor instanceof K3WheelEncoder
						&& ((K3WheelEncoder) sensor).getName().equals(side)) {

					output = "" + ((K3WheelEncoder) sensor).currentTick;
					log.debug("MOCK has returned from " + side
							+ " wheel ticks : " + output);
				}
			}

			break;
		}
		}

		return output;
	}

}
