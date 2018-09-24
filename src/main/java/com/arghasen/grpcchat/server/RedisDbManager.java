package com.arghasen.grpcchat.server;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;


public class RedisDbManager implements DbManager {

	RedisClient redisClient;
	StatefulRedisConnection<String, String> connection;
	RedisCommands<String, String> syncCommands;
	
	public RedisDbManager(String url) {
		redisClient = RedisClient.create(url);
		connection = redisClient.connect();
		connection.sync();
	}

	@Override
	public boolean checkLoginCredentials(String userId, String password) {
		return true;
	}

	@Override
	public void closeConnection() {
		connection.close();
		redisClient.shutdown();
	}

	@Override
	public boolean checkLoginToken(String username, String token) {
		return true;
	}

}
