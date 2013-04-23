package com.simili.service;

/**
 * 
 * Wrapper class hiding the implementation used to creates new threads on
 * servers like GoogleAppEngine or else
 * 
 * @author zam
 * 
 */
public interface ThreadFactoryWrapper {
	
	public Thread newRequestThread(Runnable runnable);

}
