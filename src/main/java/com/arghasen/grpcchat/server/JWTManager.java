package com.arghasen.grpcchat.server;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.JWT;
public class JWTManager {
	
	Algorithm algorithm;
	public JWTManager(){
		algorithm = Algorithm.HMAC256("secret");
	}
	public String getToken(String username) {
		return JWT.create().withClaim("userId", username).sign(algorithm);
	}
	public String decode(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getClaim("userId").asString();
		
	}

}
