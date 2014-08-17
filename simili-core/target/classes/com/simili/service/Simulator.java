package com.simili.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.simili.robot.Robot;
import com.simili.world.World;
import com.simili.world.World2D;

@Configuration
public class Simulator<ROBOT extends Robot<?,?,?>> {

	private static final Logger log = LoggerFactory.getLogger(Simulator.class);

	private List<Thread> threadList = new ArrayList<Thread>();

	public boolean isAlive;

	private World<ROBOT> world;

	private Timestamp elapseTime;

	private long start = 0;

	@Inject
	private ThreadFactoryWrapper threadFactoryWrapper;

	public ThreadFactoryWrapper getThreadFactoryWrapper() {
		return threadFactoryWrapper;
	}

	public void setThreadFactoryWrapper(
			ThreadFactoryWrapper threadFactoryWrapper) {
		this.threadFactoryWrapper = threadFactoryWrapper;
	}

	public Simulator() {
		clear();
	}

	public Simulator(World<ROBOT> world) {
		this.world = world;
	}

	public synchronized void run() {

		start = System.currentTimeMillis();

		if (world != null) {
			
			log.info("Simulator is starting ...");
			
			for (ROBOT robot : world.getRobotList()) {
				Thread robotThread = threadFactoryWrapper
						.newRequestThread(robot);
				threadList.add(robotThread);
				robotThread.start();
				log.info("Robot "+robot.getName()+" is asking to start ...");
			}
			isAlive = true;
		}

	}

	public synchronized void cancel() {

		for (Thread thread : threadList) {
			thread.interrupt();
		}

		isAlive = false;

		elapseTime = new Timestamp(System.currentTimeMillis() - start);
		log.warn("Simulation interrupted after " + elapseTime.getTime() + "ms");
	}

	public Timestamp getElapseTime() {
		return elapseTime;
	}
	
	public World<ROBOT> getWorld() {
		return world;
	}

	public void setWorld(World<ROBOT> world) {
		this.world = world;
	}

	public void clear() {
		this.world = new World2D<ROBOT>(null, null);
	}

}
