package com.todolist.model;

public class Session {
	private static User loggedInUser;

	public static User getLoggedInUser() {
		
		User user = loggedInUser;
		
		if(user == null) {
			System.out.println("Error in loggedInUser -> Session");
		}
		
		return user;
	}

	public static void setLoggedInUser(User user) {
		loggedInUser = user;
	}

	public static void clearLoggedInUser() {
		loggedInUser = null;
	}
}
