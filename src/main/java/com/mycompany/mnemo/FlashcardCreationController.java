package com.mycompany.mnemo;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FlashcardCreationController implements Initializable {


    @FXML TextArea questionText;
    @FXML TextArea answerText;
    @FXML TextArea readingText;

    @FXML CheckBox bothSides;
    @FXML CheckBox specialReadingCheckBox;
    @FXML ChoiceBox<String> flashcardTypeList;


    @FXML HBox draggableElement;

    private Stage mainStage;
    private String currentDeckName;

    private Utils utils = new Utils();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        questionText.setWrapText(true);
        answerText.setWrapText(true);
        specialReadingCheckBox.setWrapText(true);


        flashcardTypeList.getItems().addAll("Show", "Type & check", "Type & compare");
        flashcardTypeList.getSelectionModel().selectFirst();


        // Set the window to draggable
        utils.makeSceneDraggable(this.draggableElement, 0.8f);
    }



    private String preprocessText(String title) {
        return title.replace("'", "''");
    }


    // Load information from the previous window
    public void initData(Stage stage, String currentDeckName) {
        this.mainStage = stage;
        this.currentDeckName = currentDeckName;
    }

    public void showSpecialReading() {
        if(specialReadingCheckBox.isSelected()) {
            readingText.setVisible(true);
        }

        else {
            readingText.setVisible(false);
        }
    }

    public void saveFlashcard() {
        String question = preprocessText(questionText.getText());
        String answer = preprocessText(answerText.getText());

        double currentTime = System.currentTimeMillis();

        Database.insertIntoTable(currentDeckName,
                "question, answer, lvl, last_review_time",
                "'" + question + "', '" + answer + "', '0', '" + currentTime + "'");

        questionText.setText("");
        answerText.setText("");
    }

    public void closeWindow(Event e) throws Exception{
        // Get the stage and close the stage
        // Refresh main Scene on main Stage
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/deckDashboard.fxml"));
        Parent root = loader.load();

        DeckDashboardController controller = loader.getController();
        controller.initData(currentDeckName);

        mainStage.setScene(new Scene(root, Color.TRANSPARENT));

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
