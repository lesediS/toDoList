package com.todolist.model;

public class Task {
	

	private int TaskId;
	private String DateDue;
	private String Description;
	private String Task;
	
	public Task() {
		
	}
	
	public Task(int taskId, String dateDue, String description, String task) {
		super();
		this.TaskId = taskId;
        this.DateDue = dateDue;
        this.Description = description;
        this.Task = task;
	}

	public long getTaskId() {
		return TaskId;
	}

	public void setTaskId(int taskId) {
		TaskId = taskId;
	}

	public String getDateDue() {
		return DateDue;
	}

	public void setDateDue(String dateDue) {
		DateDue = dateDue;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getTask() {
		return Task;
	}

	public void setTask(String task) {
		Task = task;
	}
	
	@Override
	public String toString() {
		return DateDue + "\n" + Description + ": " + Task;
	}
	
}
