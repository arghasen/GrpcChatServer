package com.arghasen.grpcchat.server;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
public class JWTManager {
	
	Algorithm algorithm;
	public JWTManager(){
		algorithm = Algorithm.HMAC256("secret");
	}
	public String getToken(String username) {
		return JWT.create().withClaim("UserId", username).sign(algorithm);
	}

}
