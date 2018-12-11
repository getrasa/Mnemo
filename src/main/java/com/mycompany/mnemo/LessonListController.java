package com.mycompany.mnemo;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LessonListController implements Initializable {

    @FXML HBox draggableElement;

    @FXML Text questionText;
    @FXML Text answerText;
    //@FXML Text numberLeft;


    private String currentDeckName;


    List<Row> lessonFlashcards;
    int flashcardIndex = 0;


    private Utils utils = new Utils();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        utils.makeSceneDraggable(this.draggableElement, 0.8f);

    }

    // Load information from the previous window
    public void initData(String currentDeckName, List<Row> lessonFlashcards, int flashcardIndex) {
        this.currentDeckName = currentDeckName;
        this.lessonFlashcards = lessonFlashcards;
        this.flashcardIndex = flashcardIndex;

        String question = lessonFlashcards.get(flashcardIndex).getString("question");
        String answer = lessonFlashcards.get(flashcardIndex).getString("answer");

        // Set question and answer
        questionText.setText(question);
        answerText.setText(answer);

        // Set number left
        int flashcardsLeftNumber = lessonFlashcards.size();
        //numberLeft.setText(String.valueOf(flashcardsLeftNumber));

    }


    public void goNext(Event event) throws Exception {
        flashcardIndex++;

        if(flashcardIndex % 5 == 0 || flashcardIndex >= lessonFlashcards.size()) {
            // Go lesson
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/learn_show.fxml"));
            Parent root = loader.load();

            LessonShowController controller = loader.getController();

            if(flashcardIndex % 5 == 0) {
                flashcardIndex -= 5;
            }

            else{
                flashcardIndex -= (flashcardIndex % 5);
            }

            controller.initData(currentDeckName, lessonFlashcards, flashcardIndex);

            stage.setScene(new Scene(root, Color.TRANSPARENT));
        }

        else {
            String question = lessonFlashcards.get(flashcardIndex).getString("question");
            String answer = lessonFlashcards.get(flashcardIndex).getString("answer");

            // Set question and answer
            questionText.setText(question);
            answerText.setText(answer);

            if(flashcardIndex+1 % 5 == 0 || flashcardIndex+1 >= lessonFlashcards.size()){
                // if end of the list do ...
            }

        }


    }

    public void goPrevious() {
        if(flashcardIndex > 0) {
            flashcardIndex--;

            String question = lessonFlashcards.get(flashcardIndex).getString("question");
            String answer = lessonFlashcards.get(flashcardIndex).getString("answer");

            // Set question and answer
            questionText.setText(question);
            answerText.setText(answer);

            // if previous and was and of the list do...
        }

    }


    public void minimizeProgram() {

    }

    public void closeProgram() {

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
