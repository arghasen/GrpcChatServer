package com.arghasen.grpcchat.server;

import java.io.IOException;

public class GrpcServerApp {

	public static void main(String[] args) throws IOException, InterruptedException {
		DbManager dbManager = new RedisDbManager("redis://192.168.0.3:7001/0");
		GrpcServer server = new GrpcServer(dbManager);
		System.out.println("Starting server..."); 
		server.start();
		System.out.println("Server started!");
		server.blockUntilShutdown();
	}

}
