package com.company.security.config.JWT;

public class AccountAuthReq {
	private String username1;
	private String password;

	public String getUsername() {
		return username1;
	}

	public void setUsername(String username) {
		this.username1 = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public AccountAuthReq() {
	
	}

}
