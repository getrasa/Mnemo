package com.mycompany.mnemo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainApp extends Application {

    // Main function - calls launch and sets everything up. When finished calls start() function
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Load main dashboard layout from fxml.
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_dashboard.fxml"));

        // Create a scene and load the layout.
        Scene scene = new Scene(root);

        // Set the background and scene color to transparent to make the illusion of shade going beyond of stage.
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);

        // Set the title, append the scene to stage and display it.
        stage.setTitle("Mnemo");
        stage.setScene(scene);
        stage.show();
    }




}
