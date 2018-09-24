package com.arghasen.grpcchat.server;

public class RedisDbManager implements DbManager {

	@Override
	public boolean checkLoginCredentials(String userId, String password) {
		return false;
	}

}
