package com.todolist.configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import com.todolist.controller.PasswordUsernameController;
import com.todolist.model.Task;
import com.todolist.model.User;

public class DatabaseHandler extends Configuration {
	private DataSource dataSource;
	
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Database connection
    public Connection getDBCon() throws ClassNotFoundException, SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        } else {
            String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(connectionString, dbUser, dbPassword);
        }
    }

	//Add tasks to database
	public void addTaskDeetsToDB(Task task) {		
		String insertTask = "INSERT INTO " + Constants.TASKS_TBL + " ("
	            + Constants.TASKS_DATE + ", " + Constants.TASKS_DESCRIP + ", "
	            + Constants.TASKS_TASK + ")" + "VALUES(?, ?, ?)";
		try {
			Connection dbcon = getDBCon();
			PreparedStatement prepStat = dbcon.prepareStatement(insertTask, Statement.RETURN_GENERATED_KEYS);
			
			//Save task... 
			prepStat.setString(1, task.getDateDue()); //... date
			prepStat.setString(2, task.getDescription()); //... task description
			prepStat.setString(3, task.getTask()); //... the actual task itself
			
			int rowInserted = prepStat.executeUpdate();
			
			if(rowInserted > 0) {
				System.out.println("New task added to db");
				JOptionPane.showMessageDialog(null, "New task added!");
				ResultSet genKey = prepStat.getGeneratedKeys(); //Auto generate task ID. Column in MySQL is auto-generated
				if(genKey.next()) {
					int genTaskId = genKey.getInt(1); //Get generated ID and...
					System.out.println("Generated taskId: " + genTaskId); //... print new ID in console
				}
				
			} else {
				System.out.println("Couldn't add task details");
			}
			
		} catch(SQLException | ClassNotFoundException e) {
			System.out.println("Error in dbhandler: addTaskDeetsToDB() -> " + e.getMessage());
		} 
	}
	
	
	//create user on sign up
	public boolean createSignUpUser(User user) {
		//insert into mysql db statement
		String insert = "INSERT INTO " + Constants.USERS_TBL + "(" + Constants.USERS_USERNAME + ", "
				+ Constants.USERS_PASSWORD + ")" + "VALUES(?, ?)";
		
		try {
			
			Connection conn = getDBCon();
			PreparedStatement prepStatement = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			
			//Save user details
			prepStatement.setString(1, user.getUserName());
			prepStatement.setString(2, PasswordUsernameController.hashPassword(user.getPassWord())); //Hash password entered			
			int rowsInserted = prepStatement.executeUpdate();
			
			if(rowsInserted > 0) {
				System.out.println("Success");
				ResultSet genKey = prepStatement.getGeneratedKeys(); //Auto generate user ID. Column in MySQL is auto-generated
				if(genKey.next()) {
					int genUserId = genKey.getInt(1);
					System.out.println("Generated userId: " + genUserId);
				}
				return true;
			} else {
				System.out.println("Error inserting user information");
			}
			
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		return false;
	}
	
	//Verify user exists
	public boolean verifyUser(String username, String password) {
		try {
			Connection conn = getDBCon();
			//Select statement to find username in database
	        String query = "SELECT * FROM " + Constants.USERS_TBL + " WHERE " + Constants.USERS_USERNAME + " = ?";
	        PreparedStatement preparedStatement = conn.prepareStatement(query);
	        preparedStatement.setString(1, username);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        
	        if(resultSet.next()) { //Match password with hashed password in database
	        	String storedPass = resultSet.getString(Constants.USERS_PASSWORD);
	        	String hashedPass = PasswordUsernameController.hashPassword(password);
	        	
	        	if(storedPass.equals(hashedPass)) {
	        		return true;
	        	}
	        }
		} catch(SQLException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	
	public ResultSet getUser(User user) {
		
		ResultSet res = null;
		if(!user.getUserName().equals("") || !user.getPassWord().equals("")) {
			//Query to get username and password from database to find the user
			String query = "SELECT * FROM " + Constants.USERS_TBL + " WHERE"
					+ Constants.USERS_USERNAME + "=?" + " AND" + Constants.USERS_PASSWORD + "=?";
			
			try {
				PreparedStatement preparedStatement = getDBCon().prepareStatement(query);
				preparedStatement.setString(1, user.getUserName());
				preparedStatement.setString(2, user.getPassWord());
				
				res = preparedStatement.executeQuery();
			
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Enter your details");
		}
		return res;
	}

	public List<Task> getTasksForUser(User user) {
        List<Task> tasks = new ArrayList<>(); //Task are in a list

        if (user != null) {
            try (Connection connection = getDBCon()) {
                String query = "SELECT * FROM " + Constants.TASKS_TBL
                        + " WHERE " + Constants.TASKS_USER_ID + " = ?"; //Get tasks based on currently logged in user (ID)

                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, (int) user.getUserId());

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                        	int taskID = resultSet.getInt(Constants.TASKS_ID);
                        	String dateTask = resultSet.getString(Constants.TASKS_DATE);
                        	String descrip = resultSet.getString(Constants.TASKS_DESCRIP);
                            String task = resultSet.getString(Constants.TASKS_TASK);
                            
                            Task taskObj = new Task(taskID, dateTask, descrip, task);
                            tasks.add(taskObj);
                        }
                    } catch(SQLException y) {
                    	System.err.println("Error while doing resultSet business: " + y.getMessage());
                    }
                } catch(SQLException e) {
                	System.err.println("Error while doing preparedStatement business: " + e.getMessage());
                }
            } catch (SQLException | ClassNotFoundException ex) {
                System.err.println("Error while getting tasks: " + ex.getMessage());
            }
        } else {
        	System.err.println("Error while getting logged in user tasks");
        }

        return tasks;
    }

}
