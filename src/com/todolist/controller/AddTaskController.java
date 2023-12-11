package com.todolist.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;

import com.todolist.configs.Constants;
import com.todolist.configs.DatabaseHandler;
import com.todolist.model.Session;
import com.todolist.model.User;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AddTaskController {

	@FXML
    private AnchorPane addItemAnchor;

    @FXML
    private ImageView addItemImageLeft;

    @FXML
    private ImageView addItemImageRight;

    @FXML
    private Button addNewTaskConfirmBtn;

    @FXML
    private DatePicker addTaskDatePick;

    @FXML
    private Button cancleNewTask;

    @FXML
    private Button clearNewTask;

    @FXML
    private MenuItem homeFromAddTaskSubMenu;

    @FXML
    private MenuItem logoutSubMenuItem;

    @FXML
    private Menu menuHomeTask;

    @FXML
    private Menu menuLogoutTask;

    @FXML
    private Menu menuSettingsTask;

    @FXML
    private Label newTaskHeading;

    @FXML
    private MenuItem profileSubMenuItem;

    @FXML
    private TextField taskDescription;

    @FXML
    private TextField taskDetails;

    @FXML
    private MenuBar taskMenuBar;


    private Stage stage;
    private Scene scene;
    private Parent root;

    private DatabaseHandler dbHandler;
    private final User loggedInUser;
    
    public AddTaskController() {
        this.loggedInUser = new User();
		this.dbHandler = new DatabaseHandler();
        
    }
    
    @FXML
    void initialize() {   }

    private User getLoggedInUser() {
    	return Session.getLoggedInUser();    
    }


    @FXML
    void addTaskToDB(ActionEvent event) {
    	User loggedInUser = getLoggedInUser();
        // Add everything to the database

        String getDescrip = taskDescription.getText();
        String getDate = addTaskDatePick.getValue().toString();
        String newTask = taskDetails.getText();
        
        
        String insertTaskQuery = "INSERT INTO " + Constants.TASKS_TBL +
                " (" + Constants.TASKS_USER_ID + ", "
                + Constants.TASKS_DATE + ", "
                + Constants.TASKS_DESCRIP + ", "
                + Constants.TASKS_TASK + ")" +
                "VALUES(?, ?, ?, ?)";

        try {
            Connection dbConn = dbHandler.getDBCon();
            PreparedStatement prepState = dbConn.prepareStatement(insertTaskQuery, Statement.RETURN_GENERATED_KEYS);

            //Check if the handler is not null
            if (dbHandler == null) {
                System.out.println("handler is null");
                return;
            }

            //Set parameters for the prepared statement
            prepState.setLong(1, loggedInUser.getUserId());
            prepState.setString(2, getDate);
            prepState.setString(3, getDescrip);
            prepState.setString(4, newTask);

            int rowsInsert = prepState.executeUpdate();

            //Check if the task insertion was successful ( > 0 rows were inserted)
            if (rowsInsert > 0) {
                JOptionPane.showMessageDialog(null, "Task added! :)");

                ResultSet genKey = prepState.getGeneratedKeys();

                //Check if there are generated keys
                if (genKey.next()) {
                    int genTaskId = genKey.getInt(1);
                    System.out.println("Generated taskId: " + genTaskId);
                } else {
                    //Handle the case where taskId couldn't be generated
                    JOptionPane.showMessageDialog(null, "Oops, try again");
                }
            }
        } catch (Exception e) {
            System.out.println("addTaskToDB try block error : " + e);
        }
        
        //Go back to the landing page once the task is added
        try {
            root = FXMLLoader.load(getClass().getResource("/com/todolist/view/ToDoLanding.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println("addTaskToDB load to do landing page redirect try error : " + ex);
        }
        
        if (getDescrip.isEmpty() || getDate.isEmpty() || newTask.isEmpty()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Field error");
            alert.setHeaderText("You need to enter all the fields!");
            alert.showAndWait();

            return;
        }
    }

    @FXML
    void cancelNewTask(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cancel");
            alert.setHeaderText("About to cancel the task!");
            alert.setContentText("Are you sure you don't need this task?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                Button cancelButton = (Button) event.getSource();
                Scene ownerScene = cancelButton.getScene();
                Stage stage = (Stage) ownerScene.getWindow();
                System.out.println("Task canceled :)");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/todolist/view/ToDoLanding.fxml"));
                Parent root = loader.load();
                Scene loginScene = new Scene(root);

                stage.setScene(loginScene);
                stage.show();
            }
        } catch (IOException e) {
            System.out.println("cancelNewTask catch error : " + e.getMessage());
        }
    }

    @FXML
    void clearTaskDetails(ActionEvent event) {
        addTaskDatePick.setValue(null);
        taskDescription.setText("");
        taskDetails.setText("");
    }

    @FXML
    void homeFromAddTask(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/todolist/view/ToDoLanding.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            System.out.println("homeFromAddTask catch error : " + ex.getMessage());
        }
    }

    @FXML
    void logoutFromAddTask(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You are logging out to do your tasks!");
            alert.setContentText("Good luck!");

            if (alert.showAndWait().get() == ButtonType.OK) {
                MenuItem menuItem = (MenuItem) event.getSource();
                Scene ownerScene = menuItem.getParentPopup().getOwnerWindow().getScene();
                Stage stage = (Stage) ownerScene.getWindow();
                
                Session.clearLoggedInUser(); //Clear logged in user session
                
                System.out.println("Logged out :)");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/todolist/view/Login.fxml"));
                Parent root = loader.load();
                Scene loginScene = new Scene(root);

                stage.setScene(loginScene);
                stage.show();
            }
        } catch (IOException e) {
            System.out.println("logoutFromAddTask catch error : " + e.getMessage());
        }
    }

    @FXML
    void settingsProfileFromAddTask(ActionEvent event) { // TODO: create settings/profile page
        System.out.println("settings profile");
    }

    
}
