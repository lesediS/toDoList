package com.todolist.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import com.todolist.configs.Constants;
import com.todolist.configs.DatabaseHandler;
import com.todolist.configs.Shaker;
import com.todolist.model.Session;
import com.todolist.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginSignUpController {

	@FXML
    private Button loginBtn;
    
    @FXML
    private PasswordField passwordTxt;
    
    @FXML
    private Button signupBtn;
    
    @FXML
    private TextField usernameTxt;
    
    private String loginUserName;
    private String loginPass;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private DatabaseHandler databaseHandler;
    
    
    @FXML
    private Button regLoginBtn;
    
    @FXML
    private PasswordField regPasswordTxt;
    
    @FXML
    private Button regSignupBtn;
    
    
    @FXML
    private TextField regUsernameTxt;
    
    public void initialize() {
        try {
            databaseHandler = new DatabaseHandler();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void login(ActionEvent e) {
        loginUserName = usernameTxt.getText().trim();
        loginPass = passwordTxt.getText().trim();

        //Use Callable to asynchronously find (true/false) whether username and password is empty
        Callable<Boolean> emptyFields = () -> loginUserName.isEmpty() || loginPass.isEmpty();

        try { //if one of the fields is empty
            if (emptyFields.call()) {
                System.out.println("Please enter both username and password");
                return;
            }
        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }

        if (databaseHandler.verifyUser(loginUserName, loginPass)) {
            Callable<Boolean> validateUser = () -> {
                try (Connection conn = databaseHandler.getDBCon();
                     PreparedStatement preparedStatement = conn.prepareStatement(
                         "SELECT * FROM " + Constants.USERS_TBL + " WHERE " + Constants.USERS_USERNAME + " = ?")) {
                    preparedStatement.setString(1, loginUserName);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            System.out.println("Login successful");

                            //The verified user is now logged in
                            Session.setLoggedInUser(new User(resultSet.getInt(Constants.USERS_ID),
                            		resultSet.getString(Constants.USERS_USERNAME),
                            		resultSet.getString(Constants.USERS_PASSWORD)));
                            
                            //Going to the landing page once user is verified and logged in
                            root = FXMLLoader.load(getClass().getResource("/com/todolist/view/ToDoLanding.fxml"));
                            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                            return true;
                            
                        } else {
                            System.out.println("Invalid credentials");

                            Shaker shakerUsername = new Shaker(usernameTxt);
                            shakerUsername.shake();
                            Shaker passShaker = new Shaker(passwordTxt);
                            passShaker.shake();

                            usernameTxt.setText("");
                            passwordTxt.setText("");
                            return false;
                        }
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    System.out.println(ex);
                    return false;
                }
            };

            try {
                if (validateUser.call()) {
                    System.out.println("Validating user...");
                }
            } catch (Exception e1) {
            	System.out.println(e1.getMessage());
            }
        } else {
            System.out.println("Invalid credentials");
            Shaker shakerUsername = new Shaker(usernameTxt);
            shakerUsername.shake();
            Shaker passShaker = new Shaker(passwordTxt);
            passShaker.shake();
            usernameTxt.setText("");
            passwordTxt.setText("");
        }
    }

    
    @FXML
    public void goToSignUp(ActionEvent e) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/todolist/view/SignUp.fxml"));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    public void signUp(ActionEvent e) {
        String signUpUsername = regUsernameTxt.getText().trim();
        String signUpPass = regPasswordTxt.getText().trim();

        if (signUpUsername.isEmpty() || signUpPass.isEmpty()) {
            System.out.println("Please enter all sign-up details");
            return;
        }

        // Check if the username is unique
        try {
			if (PasswordUsernameController.isUsernameUnique(databaseHandler.getDBCon(), signUpUsername)) {
			    User user = new User(0, signUpUsername, signUpPass);
			    
			    if (databaseHandler.createSignUpUser(user)) {
			    	//Session.setLoggedInUser(user);
			    	
			    	regUsernameTxt.clear();
			    	regPasswordTxt.clear();
			    	
			        System.out.println("Sign-up successful");
			        
			        goToLogin(e);
			        //login(e);
			    } else {
			    	JOptionPane.showMessageDialog(null, "Error during sign-up");
			    }
			} else {
			    JOptionPane.showMessageDialog(null, "Try again");
			}
		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println(e1.getMessage());
		}
    }
    
	
    //Go to login from Sign Up
	@FXML
    public void goToLogin(ActionEvent e) {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/todolist/view/Login.fxml"));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
