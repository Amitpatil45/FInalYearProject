package com.secuity.model;

public class JWTResponse {
	private String token;
	private String name;
	private String username;
	private String password;
	private String email;
	private String phone;
	public JWTResponse(String token, String name, String username,  String email, String phone) {
		super();
		this.token = token;
		this.name = name;
		this.username = username;
		this.email = email;
		this.phone = phone;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	
}
