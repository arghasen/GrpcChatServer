package com.arghasen.grpcchat.server;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
	private ConcurrentHashMap<String, TreeSet<Long>> last3messageTimestamps;

	public RateLimiter() {
		this.last3messageTimestamps = new ConcurrentHashMap<>();
	}

	public boolean check(String username, long timestamp) {
		TreeSet<Long> timestamps = last3messageTimestamps.putIfAbsent(username, new TreeSet<>());
		
		if (timestamps == null || timestamps.size() < 3)
			return true;
		else if (timestamps.size() == 3) {
			if (timestamps.first() > timestamp - 3000)// timestamp is in millis
				return false;
			return true;
		}
		return false;
	}

	// update the rate limiter to have last 3 values.
	public void update(String username, long timestamp) {

		TreeSet<Long> timestamps = last3messageTimestamps.putIfAbsent(username, new TreeSet<>());
		if (timestamps.size() < 3)
			timestamps.add(timestamp);
		else if (timestamps.size() == 3) {
			timestamps.remove(timestamps.first());
			timestamps.add(timestamp);
		}
	}

}
