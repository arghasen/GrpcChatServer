package com.arghasen.grpcchat.server;

public interface DbManager {

	boolean checkLoginCredentials(String userId, String password);

}
