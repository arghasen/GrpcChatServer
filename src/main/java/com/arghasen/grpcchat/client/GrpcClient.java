package com.arghasen.grpcchat.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc;
import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc.GrpcChatServiceBlockingStub;
import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc.GrpcChatServiceStub;
import com.arghasen.grpcchat.proto.LoginRequest;
import com.arghasen.grpcchat.proto.LoginResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


public class GrpcClient {
	private String host;
	private int port;
	private ManagedChannel channel;
	private GrpcChatServiceStub chatService;
	private GrpcChatServiceBlockingStub blockingChatService;
	private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());
	
	public void init(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
		channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
		chatService = GrpcChatServiceGrpc.newStub(channel);
		blockingChatService  = GrpcChatServiceGrpc.newBlockingStub(channel);
	}

	public boolean login(String username, String password) {
		LoginRequest request = LoginRequest.newBuilder().setUserId(username).setPassword(password).build();
		LoginResponse response;
		try {
			response = blockingChatService.login(request);
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return false;
		}
		logger.info("Login Status: " + response.getStatus());
		logger.info("Login Message: " + response.getToken());
		if(response.getStatus()=="Success")
			return true;
		else
			return false;
	}

}
