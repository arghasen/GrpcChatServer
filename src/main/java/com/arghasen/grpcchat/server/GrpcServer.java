package com.arghasen.grpcchat.server;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

//import com.arghasen.grpcchat.proto.*;
import com.arghasen.grpcchat.proto.GrpcChatServiceGrpc;
import com.arghasen.grpcchat.proto.LoginRequest;
import com.arghasen.grpcchat.proto.LoginResponse;
import com.arghasen.grpcchat.proto.MessageRequest;
import com.arghasen.grpcchat.proto.MessageResponse;
import com.arghasen.grpcchat.proto.RecieveRequest;
import com.arghasen.grpcchat.proto.RecieveResponse;

public class GrpcServer {
	private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());
	private Server server;
	private DbManager dbManager;
	private JWTManager jwtManager;

	public GrpcServer(DbManager dbManager) {
		this.dbManager = dbManager;
	}

	public void start() throws IOException {
		/* The port on which the server should run */
		int port = 50051;
		server = ServerBuilder.forPort(port).addService(new GrpcServerImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown
				// hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				GrpcServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
	
	public class GrpcServerImpl extends GrpcChatServiceGrpc.GrpcChatServiceImplBase {

		private final Logger logger = Logger.getLogger(GrpcServer.class.getName());
		
		@Override
		public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
			logger.info(request.toString());
			LoginResponse response;
			String username = request.getUserId();
			String password = request.getPassword();
			if(dbManager.checkLoginCredentials(username,password))
			{
				String token = jwtManager.getToken(username);
				response = LoginResponse.newBuilder().setStatus("Success").setToken(token).build();
			}
			else
			{
				response = LoginResponse.newBuilder().setStatus("Failed").build();
			}
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}

		@Override
		public void sendMessage(MessageRequest request, StreamObserver<MessageResponse> responseObserver) {
			logger.info(request.toString());
		}

		@Override
		public void recieveMessage(RecieveRequest request, StreamObserver<RecieveResponse> responseObserver) {
			logger.info(request.toString());
		}

	  }
}
