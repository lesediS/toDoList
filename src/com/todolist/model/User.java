package com.todolist.model;

public class User {
	private int UserId;
	private String UserName;
	private String PassWord;

	public User() {}
	
	public User(int userId, String userName, String passWord) {
	    this.UserId = userId;
		this.UserName = userName;
		this.PassWord = passWord;
	}
	public long getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassWord() {
		return PassWord;
	}
	public void setPassWord(String passWord) {
		PassWord = passWord;
	}
	
}
