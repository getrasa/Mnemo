package com.mycompany.mnemo;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckEditController implements Initializable {


    @FXML TextField title;
    @FXML TextField subject;

    @FXML HBox draggableElement;

    private Stage mainStage;
    private String deckID;

    private Utils utils = new Utils();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Listen for enter when in subject text field
        listenForEnterPressed();

        // Set the window to draggable
        utils.makeSceneDraggable(this.draggableElement, 0.8f);

    }

    public void saveDeck(Event e) throws Exception {

        // Initialize variables which will be added to the table.
        String deckTitle = preprocessText(title.getText());
        String deckSubject = preprocessText(subject.getText().toUpperCase());

        Database.updateTable("title, subject", "'" + deckTitle + "', '" + deckSubject + "'",
                             "WHERE deckID = '" + deckID + "'");

        // Refresh main Scene on main Stage
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_dashboard.fxml"));
        mainStage.setScene(new Scene(root, Color.TRANSPARENT));

        // Close the window
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    private String preprocessText(String title) {
        title = title.replace("'", "''");
        if(title.length() >=52) {title = title.substring(0, 50) + "...";}

        return title;
    }

    private void listenForEnterPressed() {

        // Listen for enter key pressed
        subject.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    try {
                        saveDeck(e);
                    } catch(Exception exception) {
                        System.out.println("Something went wrong wile pressing enter");
                    }
                }
            }
        });

        title.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    try {
                        saveDeck(e);
                    } catch(Exception exception) {
                        System.out.println("Something went wrong wile pressing enter");
                    }
                }
            }
        });
    }

    // Load information from the previous window
    public void initData(Stage stage, String deckID, String title, String subject) {
        mainStage = stage;
        this.deckID = deckID;
        this.title.setText(title);
        this.subject.setText(subject);
    }

    public void closeWindow(Event e){
        // Get the stage and close the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
