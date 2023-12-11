package com.todolist.model;

public class User {
	private int UserId;
	private String FirstName;
	private String Surname;
	private String UserName;
	private String PassWord;

	public User() {}
	
	
	public User(int userId, String firstName, String surname, String userName, String passWord) {
	    this.UserId = userId;
		this.FirstName = firstName;
		this.Surname = surname;
		this.UserName = userName;
		this.PassWord = passWord;
	}
	public long getUserId() {
		return UserId;
	}
	public void setUserId(int userId) {
		UserId = userId;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getSurname() {
		return Surname;
	}
	public void setSurname(String surname) {
		Surname = surname;
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
