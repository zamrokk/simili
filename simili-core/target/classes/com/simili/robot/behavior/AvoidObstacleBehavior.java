package com.simili.robot.behavior;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simili.robot.Robot;
import com.simili.robot.position.Position;
import com.simili.robot.position.Position2D;
import com.simili.robot.sensor.ProximitySensor;
import com.simili.robot.sensor.WheelEncoder;
import com.simili.robot.state.State;
import com.simili.robot.state.UnicycleDriveState;

public class AvoidObstacleBehavior<ROBOT extends Robot<? extends ProximitySensor<ROBOT>, ? extends WheelEncoder<ROBOT>, ?>>
		implements Behavior<ROBOT> {

	private static final Logger log = LoggerFactory
			.getLogger(AvoidObstacleBehavior.class);

	private Position obstaclePosition;

	// memory banks
	private double E_k;
	private double e_k_1;

	// gains
	public double Kp;
	public double Ki;
	public double Kd;

	public AvoidObstacleBehavior(Position position) {
		obstaclePosition = position;
		E_k = 0;
		e_k_1 = 0;
		Kp = 5;
		Ki = 0.01;
		Kd = 0.1;
	}

	@Override
	public BEHAVIORS getName() {
		return BEHAVIORS.AVOID_OBSTACLE;
	}

	@Override
	public void setPIDGains(double Kp, double Ki, double Kd) {
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
	}

	@Override
	public State execute(ROBOT robot, State desiredState, double delta_t) {

		double v, w;

		// Unpack state estimate
		Position2D robotPosition = robot.getCenterPosition();

		// Interpret the IR sensor measurements geometrically
		RealMatrix irDistances = applySensorGeometry(robot);

		// Compute the heading vector
		List<Double> gainsList = new ArrayList<Double>();
		for (ProximitySensor<ROBOT> sensor : robot.getProximitySensorList()) {
			gainsList.add(sensor.importance);
		}

		int sensorCount = gainsList.size();

		Double[] ds = gainsList.toArray(new Double[sensorCount]);
		double[] d = ArrayUtils.toPrimitive(ds);
		RealMatrix sensor_gains_matrix = new DiagonalMatrix(d);

		// prepare substract robot position matrix
		double[][] position = new double[][] { { robot.getCenterPosition().x },
				{ robot.getCenterPosition().y } };
		RealMatrix positionMatrix = MatrixUtils.createRealMatrix(position);
		positionMatrix = positionMatrix.multiply(MatrixUtils.createRealMatrix(1,sensorCount)
				.scalarAdd(1));

		// get only positions for all vectors
		RealMatrix u_i = irDistances.getSubMatrix(0, 1, 0, sensorCount - 1);
		// substract robot position,to recenter on (0,0)
		u_i = u_i.subtract(positionMatrix);
		// multiply by factor gains
		RealMatrix u_a = u_i.multiply(sensor_gains_matrix);

		// sum all x and all y to get a vector [1,2] using identity matrix
		/*RealMatrix u_a = u_i.multiply(new DiagonalMatrix(sensorCount)
				.scalarAdd(1));*/

		// Compute the heading and error for the PID controller
		double theta_o = Math.atan2(u_a.getEntry(1, 0), u_a.getEntry(0, 0));
		double e_k = theta_o - robotPosition.theta;
		e_k = Math.atan2(Math.sin(e_k), Math.cos(e_k));

		double e_P = e_k;
		double e_I = E_k + e_k * delta_t;
		double e_D = (e_k - e_k_1) / delta_t;

		// PID control on w

		w = Kp * e_P + Ki * e_I + Kd * e_D;

		// Save errors for next time step
		E_k = e_I;
		e_k_1 = e_k;

		// just has to go at max if possible in function of w
		v = ((UnicycleDriveState) desiredState).v;// (Math.log(Math.abs(w)+2)+1);

		log.info(" *** new velocity should be : " + v
				+ " and angular velocity : " + w);

		return new UnicycleDriveState(v, w);
	}

	private RealMatrix applySensorGeometry(ROBOT robot) {

		int i = 0;
		// row 3
		// column 9
		RealMatrix ir_distances_sf = MatrixUtils.createRealMatrix(3, 9);
		
		RealMatrix ir_distances_sfMatrix = MatrixUtils.createRealMatrix(3, 9);

		for (ProximitySensor<ROBOT> sensor : robot.getProximitySensorList()) {

			double x_s = ((Position2D) sensor.position).x;
			double y_s = ((Position2D) sensor.position).y;
			double theta_s = ((Position2D) sensor.position).theta;

			RealMatrix r = getRotationMatrix(x_s, y_s, theta_s);

			RealVector sensorVector = new ArrayRealVector(new double[] {
					sensor.getDistanceValue(), 0, 1 });
			RealMatrix sensorMatrix = MatrixUtils.createRealMatrix(3, 1);
			sensorMatrix.setColumnVector(0, sensorVector);

			ir_distances_sf.setColumnVector(i,
					new ArrayRealVector(r.multiply(sensorMatrix).getColumn(0)));
			i++;
		}

		// Apply the transformation to world frame.
		RealMatrix r = getRotationMatrix(robot.getCenterPosition().x,
				robot.getCenterPosition().y, robot.getCenterPosition().theta);
		ir_distances_sfMatrix = r.multiply(ir_distances_sf);

		return ir_distances_sfMatrix;

	}

	public RealMatrix getRotationMatrix(double x, double y, double angle) {

		RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
		matrix.setColumn(0,
				new double[] { Math.cos(angle), -Math.sin(angle), x });
		matrix.setColumn(1,
				new double[] { Math.sin(angle), Math.cos(angle), y });
		matrix.setColumn(2, new double[] { 0, 0, 1 });
		log.error("MATRIX" + matrix.getColumnDimension());

		return matrix;
	}

}
