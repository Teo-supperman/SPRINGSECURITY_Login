package com.company.security.config.JWT;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class Secretkey {
	private JsonWebToken jsonWebToken;

	public Secretkey(JsonWebToken jsonWebToken) {
		this.jsonWebToken = jsonWebToken;
	}

	@Bean
	public SecretKey secretKey() {
		return Keys.hmacShaKeyFor(jsonWebToken.getSecretKey().getBytes());
	}
}
