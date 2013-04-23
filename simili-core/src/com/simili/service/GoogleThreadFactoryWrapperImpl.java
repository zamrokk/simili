package com.simili.service;

import com.google.appengine.api.ThreadManager;

public class GoogleThreadFactoryWrapperImpl implements ThreadFactoryWrapper {

	@Override
	public Thread newRequestThread(Runnable runnable) {
		return ThreadManager.createThreadForCurrentRequest(runnable);
	}
}
