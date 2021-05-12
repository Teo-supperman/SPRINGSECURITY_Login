package com.company.security.config.JWT;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JsonWebToken jwtConfig;
	private final SecretKey secretKey;

	public AuthenticationFilter(AuthenticationManager authenticationManager, JsonWebToken jwtConfig,
			SecretKey secretKey) {
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		logger.info("Filter start...");
		try {
			logger.info("try...");
			AccountAuthReq autheticationRequest = new ObjectMapper().readValue(request.getInputStream(), AccountAuthReq.class);
			Authentication authentication = new UsernamePasswordAuthenticationToken(autheticationRequest.getUsername(),
					autheticationRequest.getPassword());
			Authentication authenticatte = authenticationManager.authenticate(authentication);
			return authenticatte;
		} catch (IOException e) {
			logger.info("catch...");
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = Jwts.builder().setSubject(authResult.getName()).claim("authorities", authResult.getAuthorities())
				.setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
				.signWith(secretKey).compact();

		response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
	}
}
