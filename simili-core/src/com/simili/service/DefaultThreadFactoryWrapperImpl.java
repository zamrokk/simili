package com.simili.service;

public class DefaultThreadFactoryWrapperImpl implements ThreadFactoryWrapper {

	@Override
	public Thread newRequestThread(Runnable runnable) {
		return new Thread(runnable);
	}

}
