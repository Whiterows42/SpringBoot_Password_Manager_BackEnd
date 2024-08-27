package com.nt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PasswordManager {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String webUrl;
	private String userName;
	private String email;
	private String password;
	private String salt;
	

	public PasswordManager(int id, String webUrl, String userName, String email, String password, String salt) {
		super();
		this.id = id;
		this.webUrl = webUrl;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.salt = salt;
	}
	public PasswordManager() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	  public String getSalt() {
	        return salt;
	    }

	    public void setSalt(String salt) {
	        this.salt = salt;
	    }
	

	
	
	
}
