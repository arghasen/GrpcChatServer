package com.arghasen.grpcchat.client;

import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc;
import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc.GrpcChatServiceBlockingStub;
import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc.GrpcChatServiceStub;
import com.arghasen.grpcchat.proto.LoginRequest;
import com.arghasen.grpcchat.proto.LoginResponse;
import com.arghasen.grpcchat.proto.Message;
import com.arghasen.grpcchat.proto.MessageRequest;
import com.arghasen.grpcchat.proto.MessageResponse;
import com.arghasen.grpcchat.proto.RecieveRequest;
import com.arghasen.grpcchat.proto.RecieveResponse;
import com.arghasen.grpcchat.server.JWTManager;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class GrpcClient extends Observable {
	private String host;
	private int port;
	private ManagedChannel channel;
	private GrpcChatServiceStub chatService;
	private GrpcChatServiceBlockingStub blockingChatService;
	private String token;
	protected JWTManager jwtManager;
	private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

	public void init(String host, String port) {
		this.host = host;
		this.port = Integer.parseInt(port);
		jwtManager =new JWTManager();
		channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
		chatService = GrpcChatServiceGrpc.newStub(channel);
		blockingChatService = GrpcChatServiceGrpc.newBlockingStub(channel);
	}

	@Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
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
		if (response.getStatus().equals("Success")) {
			token = response.getToken();
			return true;
		} else
			return false;
	}

	public boolean send(String recipient, String msg) {

		Message message = Message.newBuilder().setCounterParty(recipient).setContent(msg)
				.setTimestamp(System.currentTimeMillis()).build();
		MessageRequest request = MessageRequest.newBuilder().setToken(token).setChat(message).build();
		MessageResponse response;
		try {
			response = blockingChatService.sendMessage(request);
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return false;
		}
		logger.info("Message Status: " + response.getStatus());
		if (response.getStatus().equals("Success"))
			return true;
		else
			return false;
	}

	public void receive(String userName) {
		RecieveRequest request = RecieveRequest.newBuilder().setToken(token).build();
		try {
			 chatService.recieveMessage(request, new StreamObserver<RecieveResponse>() {
				
				@Override
				public void onNext(RecieveResponse value) {
					logger.info("Message Status: " + value.getChatsList().toString());
					for (Message chat : value.getChatsList()) {
						String username = jwtManager.decode(chat.getCounterParty());
						String content = chat.getContent();
						notifyObservers("<"+username+"> " +content);
					}
				}
				
				@Override
				public void onError(Throwable t) {
				}
				
				@Override
				public void onCompleted() {
				}
			 });
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return ;
		}
	}

}
