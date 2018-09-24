package com.arghasen.grpcchat.server;

public interface DbManager {

	boolean checkLoginCredentials(String userId, String password);

	void closeConnection();

	boolean checkLoginToken(String username, String token);

	void storeUserToken(String username, String token);

	void storeMessage(String token, String counterParty, String content, long timestamp);

	boolean hasMoreMessages(String username);

	String getMessage(String username);

}
