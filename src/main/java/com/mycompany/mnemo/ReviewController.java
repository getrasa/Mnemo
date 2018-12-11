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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {

    @FXML HBox draggableElement;

    @FXML Text questionText;
    @FXML Text answerText;
    @FXML Text numberLeft;

    @FXML VBox answerSection;
    @FXML VBox disappearSection;

    private String currentDeckName;


    List<Row> reviewFlashcards;
    int flashcardIndex = 0;


    private Utils utils = new Utils();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        utils.makeSceneDraggable(this.draggableElement, 0.8f);

    }

    // Load information from the previous window
    public void initData(String currentDeckName, List<Row> reviewFlashcards, int flashcardIndex) {
        this.currentDeckName = currentDeckName;
        this.reviewFlashcards = reviewFlashcards;
        this.flashcardIndex = flashcardIndex;

        String question = reviewFlashcards.get(flashcardIndex).getString("question");
        String answer = reviewFlashcards.get(flashcardIndex).getString("answer");

        // Set question and answer
        questionText.setText(question);
        answerText.setText(answer);

        // Set number left
        int flashcardsLeftNumber = reviewFlashcards.size() - flashcardIndex;
        numberLeft.setText(String.valueOf(flashcardsLeftNumber));

    }


    public void showAnswer() {
        answerSection.setVisible(true);
        disappearSection.setVisible(false);
    }

    public void correctAnswer(Event event) throws Exception {

        answer(event, "correct");

    }

    public void unsureAnswer(Event event) throws Exception {

        answer(event, "unsure");
    }

    public void incorrectAnswer(Event event) throws Exception {

        answer(event, "incorrect");

    }

    public void answer(Event event, String type) throws Exception {
        // Get stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Row flashcard = reviewFlashcards.get(flashcardIndex);
        int lvl = flashcard.getInt("lvl");
        double currentTime = System.currentTimeMillis();


        if(type == "correct" && lvl != 8) {
            flashcard.setValue("lvl", lvl+1);
        }

        else if(type == "incorrect" && lvl != 1) {
            flashcard.setValue("lvl", lvl-1);
        }

        // Set new values
        flashcard.setValue("last_review_time", currentTime);

        flashcardIndex++;

        System.out.println(reviewFlashcards.size() + " " + flashcardIndex);

        // Load flashcard and get lvl and current time
        if (reviewFlashcards.size() <= flashcardIndex) {
            Database.updateTable(currentDeckName, reviewFlashcards);
            returnToDashboard(event);
        }

        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/review_show.fxml"));
            Parent root = loader.load();

            ReviewController controller = loader.getController();
            controller.initData(currentDeckName, reviewFlashcards, flashcardIndex);

            stage.setScene(new Scene(root, Color.TRANSPARENT));
        }
    }


    public void returnToDashboard(Event event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/deckDashboard.fxml"));
        Parent root = loader.load();

        DeckDashboardController controller = loader.getController();
        controller.initData(currentDeckName);

        stage.setScene(new Scene(root, Color.TRANSPARENT));
    }



}
