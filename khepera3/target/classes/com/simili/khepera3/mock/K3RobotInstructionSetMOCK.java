package com.simili.khepera3.mock;

import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.khepera3.K3Command;
import com.simili.khepera3.K3ProximitySensor;
import com.simili.khepera3.K3RobotInstructionSet;
import com.simili.khepera3.K3WheelEncoder;
import com.simili.khepera3.Khepera3;
import com.simili.service.Simulator;
import com.simili.world.Obstacle;
import com.simili.world.Obstacle2D;
import com.simili.world.World;

public class K3RobotInstructionSetMOCK extends K3RobotInstructionSet {

	private static final Logger log = LoggerFactory
			.getLogger(K3RobotInstructionSetMOCK.class);

	@JsonIgnore
	@Inject
	private Simulator<Khepera3> simulator;

	@Override
	public String sendInstruction(Khepera3 robot, K3Command command,
			String... arguments) {

		World<Khepera3> world = simulator.getWorld();

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
				for (K3WheelEncoder sensor : robot.getWhellEncoderSensorList()) {
					if (sensor.getName().equals(side)) {

						realVelocityApplied = newV
								* (1 - Math.pow(
										(Math.abs(sensor
												.getLastAngularVelocity()
												- newV) / (2 * robot
												.getMaxangularvelocity())), 2))
								* 0.8;
						sensor.updateTicks(realVelocityApplied,
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

			for (K3ProximitySensor sensor : robot.getProximitySensorList()) {
				if (sensor.getName().equals(name)) {
					// check for each obstacle if someone is close
					for (Obstacle obstacle : world.getObstacleList()) {
						Obstacle2D o = (Obstacle2D) obstacle;
						double x = Math.abs(o.getCenterPosition().x
								- robot.getCenterPosition().x)
								- o.radius;
						double y = Math.abs(o.getCenterPosition().y
								- robot.getCenterPosition().y)
								- o.radius;
						double distance = Math.hypot(x, y);

						double perfectAngle = Math.atan2(y, x);
						double actualAngle = 0; /*
												 * FIXME
												 * robot.getCenterPosition(
												 * ).theta + kps.getAngle();
												 */
						actualAngle = Math.atan2(Math.sin(actualAngle),
								Math.cos(actualAngle));
						// correction reduced in fonction of the position of the
						// sensor [0% -100%]
						distance = correctDistanceFromAngle(distance,
								perfectAngle, actualAngle);

						int newValue = K3ProximitySensor
								.convertDistance2Value(distance);

						// get closest obstacle distance
						if (newValue > sensor.getLastValue()) {
							sensor.updateDistanceValue(newValue);

							log.debug("MOCK has updated proximity sensor "
									+ name + " with value : " + newValue);
						}

					}

				}
			}

			break;
		}
		case READ_TICKS: {
			// read last ticks
			String side = arguments[0];
			// add new perfect ticks on last velocity

			for (K3WheelEncoder sensor : robot.getWhellEncoderSensorList()) {
				if (sensor.getName().equals(side)) {

					output = "" + sensor.currentTick;
					log.debug("MOCK has returned from " + side
							+ " wheel ticks : " + output);
				}
			}

			break;
		}
		}

		return output;
	}

	/**
	 * Reduce distance in function of the REAL angle of the sensor. I use power
	 * 4 factor to increase difference
	 * 
	 * @param distance
	 *            the perfect distance to the obstacle
	 * @param perfectAngle
	 *            the perfect angle to the obstacle
	 * @param actualAngle
	 *            the angle of the sensor
	 * @return Return the reduced distance
	 */
	private double correctDistanceFromAngle(double distance,
			double perfectAngle, double actualAngle) {
		double difference = Math.abs(perfectAngle - actualAngle) / Math.PI;
		return Math.pow(difference, 4) * distance;
	}

}
