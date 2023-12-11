package com.todolist.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.todolist.configs.Configuration;
import com.todolist.configs.Constants;
import com.todolist.model.User;

public class PasswordUsernameController extends Configuration {

	//Hash password on sign up
	public static String hashPassword(String password) {
		try {
			MessageDigest mdigest = MessageDigest.getInstance("SHA-1");
			byte[] hashedPass = mdigest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			
			for(byte by : hashedPass) {
				hexString.append(String.format("%02x", by));
			}
			
			return hexString.toString();
			
		} catch(NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public boolean verifyPass(User user) {
		String storedHashPass = getHashedPassFromDB(user.getUserName());
		
		if(storedHashPass == null) {
			JOptionPane.showMessageDialog(null, "Invalid hashed password");
			return false;
		}
		
		String enteredHashPass = hashPassword(user.getPassWord());
		
		return storedHashPass.equals(enteredHashPass);
	}

	private String getHashedPassFromDB(String userName2) {
		String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
		String query = "SELECT " + Constants.USERS_PASSWORD + " FROM " 
				+ Constants.USERS_TBL + " WHERE " + Constants.USERS_USERNAME + " = ?";
		try(Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
				
				/*PreparedStatement preStatement = con.prepareStatement("SELECT password FROM user WHERE username = ?")*/
				PreparedStatement preStatement = con.prepareStatement(query)){
			preStatement.setString(1, userName2);
			
			try(ResultSet resSet = preStatement.executeQuery()){
				if(resSet.next()) {
					return resSet.getString("password");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	public static boolean isUsernameUnique(Connection dbConn, String username) {
        String query = "SELECT COUNT(*) FROM " + Constants.USERS_TBL + " WHERE " + Constants.USERS_USERNAME + " = ?";
        boolean isUnique = false;
        
        try (PreparedStatement preparedStatement = dbConn.prepareStatement(query)) {
            preparedStatement.setString(1, username.toLowerCase());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    isUnique = (count == 0);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error during username validation check: " + ex.getMessage());
        }
        return isUnique; // An error occurred, username is not unique
    }
	
}
