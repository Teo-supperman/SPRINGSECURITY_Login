package com.company.security.config.JWT;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class TokenVerifier extends OncePerRequestFilter {
	private final SecretKey secretkey;
	private final JsonWebToken jsonWebToken;

	public TokenVerifier(SecretKey secretkey, JsonWebToken jsonWebToken) {

		this.secretkey = secretkey;
		this.jsonWebToken = jsonWebToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String authorizationHeader = request.getHeader(jsonWebToken.getAuthorizationHeader());

		if (Strings.isNullOrEmpty(authorizationHeader)
				|| !authorizationHeader.startsWith(jsonWebToken.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authorizationHeader.replace(jsonWebToken.getTokenPrefix(), "");
		try {

			Jws<Claims> claimjws = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
			Claims body = claimjws.getBody();
			String username = body.getSubject();

			List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

			Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
					.map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException e) {
			throw new IllegalStateException(String.format("token %s can not be truest", token));
		}
		filterChain.doFilter(request, response);

	}
}
