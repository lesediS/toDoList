package com.todolist.configs;

public class Constants {
	//tables
	public static final String USERS_TBL = "user";
	public static final String TASKS_TBL = "task";
	
	//columns in -> user table
	public static final String USERS_ID = "userId";
	public static final String USERS_USERNAME = "username";
	public static final String USERS_PASSWORD = "password";
	
	//columns in -> task table
	public static final String TASKS_ID = "taskId";
	public static final String TASKS_USER_ID = "userId";
	public static final String TASKS_DATE = "dateToBeDone";
	public static final String TASKS_DESCRIP = "description";
	public static final String TASKS_TASK = "task";
}
