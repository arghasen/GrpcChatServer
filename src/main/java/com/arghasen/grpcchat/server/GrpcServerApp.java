package com.arghasen.grpcchat.server;

import java.io.IOException;

public class GrpcServerApp {

	public static void main(String[] args) throws IOException, InterruptedException {
		GrpcServer server = new GrpcServer();
		System.out.println("Starting server...");
		server.start();
		System.out.println("Server started!");
		server.blockUntilShutdown();
	}

}
