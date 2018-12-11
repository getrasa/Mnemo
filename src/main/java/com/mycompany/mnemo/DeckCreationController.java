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

public class DeckCreationController implements Initializable {


    @FXML TextField title;
    @FXML TextField subject;

    @FXML HBox draggableElement;

    private Stage mainStage;
    private int deckCount;

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
        String deckID;
        String deckTitle = preprocessText(title.getText());
        String deckSubject = preprocessText(subject.getText().toUpperCase());
        String deckColor = utils.color.get(deckCount % utils.color.size());
        String deckColorDark = utils.colorDark.get(deckCount % utils.color.size());

        // Get unique id name.
        deckID = "deck" + System.currentTimeMillis();


        // Create a table for new deck
        Database.createTable(deckID,
                "id INTEGER PRIMARY KEY, " +
                            "question TEXT, " +
                            "answer TEXT, " +
                            "type TEXT, " +
                            "lvl INTEGER, " +
                            "last_review_time DOUBLE, " +
                            "next_review_time DOUBLE");

        // Insert info about new deck into index table
        Database.insertIntoTable("deckID, title, subject, color, color_dark",
                "'" + deckID + "', " + "'" + deckTitle + "', '" + deckSubject + "', '" + deckColor + "', '" + deckColorDark + "'");

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

        // Listen for enter key pressed
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
    public void initData(Stage stage, int deckCount) {
        mainStage = stage;
        this.deckCount = deckCount;
    }

    public void closeWindow(Event e){
        // Get the stage and close the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
