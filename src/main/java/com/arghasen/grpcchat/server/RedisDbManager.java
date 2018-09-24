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
		syncCommands = connection.sync();
	}

	@Override
	public boolean checkLoginCredentials(String userId, String password) {
		String key = "users:login:" + userId;
		String userIdFromRedis = syncCommands.get(key);
		String passwordFromRedis = syncCommands.hget(userIdFromRedis, "password");
		return password.equals(passwordFromRedis);
	}

	@Override
	public void closeConnection() {
		connection.close();
		redisClient.shutdown();
	}

	@Override
	public boolean checkLoginToken(String username, String token) {
		String key = "users:token:" + username;
		String tokenFromRedis = syncCommands.get(key);
		return token.equals(tokenFromRedis);
	}

	@Override
	public void storeUserToken(String username, String token) {
		String key = "users:token:" + username;
		syncCommands.set(key, token);
	}

	@Override
	public void storeMessage(String token, String counterParty, String content, long timestamp) {
		String key = "users:messages:" + counterParty;
		long len = syncCommands.llen(key);
		String message = token + ":" + content + ":" + timestamp;
		if (len < 10)
			syncCommands.rpush(key, message);
	}

	@Override
	public boolean hasMoreMessages(String username) {
		String key = "users:messages:" + username;
		long len = syncCommands.llen(key);
		return len>0;
	}

	@Override
	public String getMessage(String username) {
		String key = "users:messages:" + username;
		return syncCommands.lpop(key);
	}

}
