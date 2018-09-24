package com.arghasen.grpcchat.server;

public interface DbManager {

	boolean checkLoginCredentials(String userId, String password);

	void closeConnection();

	boolean checkLoginToken(String username, String token);

}
