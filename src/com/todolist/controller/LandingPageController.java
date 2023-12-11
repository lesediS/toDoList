package com.todolist.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.todolist.configs.DatabaseHandler;
import com.todolist.configs.Shaker;
import com.todolist.model.Session;
import com.todolist.model.Task;
import com.todolist.model.User;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private AnchorPane addItemAnchor;

    @FXML
    private ImageView addTaskBtn;

    @FXML
    private MenuItem addTaskSubMenu;

    @FXML
    private MenuItem goHomeSubMenu;

    @FXML
    private CheckBox taskCheckBx;

    @FXML
    private Label landingHeading;

    @FXML
    private MenuBar landingMenuBar;

    @FXML
    private Menu logoutMenu;

    @FXML
    private MenuItem logoutSubMenu;

    @FXML
    private Label makeTaskLbl;

    @FXML
    private Menu menuAddTask;

    @FXML
    private Menu menuHome;

    @FXML
    private MenuItem menuItemProfile;

    @FXML
    private Menu settingsMenu;

    @FXML
    private ListView<Task> tasksListView;

    private User loggedInUser = Session.getLoggedInUser();

    private DatabaseHandler handler = new DatabaseHandler();

    public void setLoggedUser(User user) {
        loggedInUser = user;
    }

    public void setHandler(DatabaseHandler handler) {
        try {
            this.handler = handler;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadTasks() {
        try {
            if (loggedInUser != null) {
                if (handler != null) {
                    tasksListView.getItems().clear();
                    List<Task> tasks = handler.getTasksForUser(loggedInUser);

                    // Add tasks directly to the ListView
                    tasksListView.getItems().addAll(tasks);
                } else {
                    System.out.println("Error in loadTasks -> handler null");
                }
            } else {
                System.out.println("Error in loadTasks -> logged in user null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addTaskMenu(ActionEvent event) { // Go to add new task from menu bar
        try {
            AnchorPane addPane = FXMLLoader.load(getClass().getResource("/com/todolist/view/NewTask.fxml"));
            FadeTransition rootTrans = new FadeTransition(Duration.millis(2000), addPane);
            rootTrans.setFromValue(0f);
            rootTrans.setToValue(1f);
            rootTrans.setCycleCount(1);
            rootTrans.setAutoReverse(false);
            rootTrans.play();

            addItemAnchor.getChildren().setAll(addPane);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void headHomeMenu(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Already home :)");
    }

    @FXML
    void profileMenu(ActionEvent event) {
        System.out.println("Clicked Profile in menu");
    }

    @FXML
    void settingsMenu(ActionEvent event) {
        System.out.println("Clicked Settings in menu");
    }

    @FXML
    void initialize() {
        loadTasks();
        addTaskBtn.setOnMouseClicked(event -> {
            Shaker btnShake = new Shaker(addTaskBtn);
            btnShake.shake();

            try {
                AnchorPane addPane = FXMLLoader.load(getClass().getResource("/com/todolist/view/NewTask.fxml"));
                FadeTransition rootTrans = new FadeTransition(Duration.millis(2000), addPane);
                rootTrans.setFromValue(0f);
                rootTrans.setToValue(1f);
                rootTrans.setCycleCount(1);
                rootTrans.setAutoReverse(false);
                rootTrans.play();

                addItemAnchor.getChildren().setAll(addPane);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Image add works!");
        });

        tasksListView.setCellFactory(param -> new ListCell<Task>() {
            private CheckBox cbox = new CheckBox();

            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    cbox.setSelected(false);
                    cbox.setText(item.toString()); // Adjust this based on your Task class properties
                    setGraphic(cbox);
                }
            }
        });
    }

    // https://youtu.be/exIQqcQ0lzI
    @FXML
    void logoutMenu(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You are logging out to do your tasks!");
            alert.setContentText("Good luck!");

            if (alert.showAndWait().get() == ButtonType.OK) {
                MenuItem menuItem = (MenuItem) event.getSource();
                Scene ownerScene = menuItem.getParentPopup().getOwnerWindow().getScene();

                Session.clearLoggedInUser();

                Stage stage = (Stage) ownerScene.getWindow();
                System.out.println("Logged out :)");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/todolist/view/Login.fxml"));
                Parent root = loader.load();
                Scene loginScene = new Scene(root);

                stage.setScene(loginScene);
                stage.show();
            }
        } catch (Exception e) {
            System.out.println("Logout error : " + e.getMessage());
        }
    }
}
