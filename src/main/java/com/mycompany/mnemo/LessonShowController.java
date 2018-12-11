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

public class LessonShowController implements Initializable {

    @FXML HBox draggableElement;

    @FXML Text questionText;
    @FXML Text answerText;
    @FXML Text numberLeft;

    @FXML VBox answerSection;
    @FXML VBox disappearSection;

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
        numberLeft.setText(String.valueOf(flashcardsLeftNumber));

    }


    public void showAnswer() {
        answerSection.setVisible(true);
        disappearSection.setVisible(false);
    }

    public void correctAnswer(Event event) throws Exception {
        answer(event, "correct");

    }


    public void incorrectAnswer(Event event) {
        answerSection.setVisible(false);
        disappearSection.setVisible(true);

    }


    public void answer(Event event, String type) throws Exception {
        // Get stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Row flashcard = lessonFlashcards.get(flashcardIndex);
        double currentTime = System.currentTimeMillis();


        if(type == "correct") {
            flashcard.setValue("lvl", 1);
        }

        // Set new values
        flashcard.setValue("last_review_time", currentTime);

        flashcardIndex++;

        System.out.println(lessonFlashcards.size() + " " + flashcardIndex);

        // Load flashcard and get lvl and current time
        if (lessonFlashcards.size() <= flashcardIndex) {
            System.out.println("Save");
            Database.updateTable(currentDeckName, lessonFlashcards);
            returnToDashboard(event);
        }

        else if(flashcardIndex % 5 == 0) {
            Database.updateTable(currentDeckName, lessonFlashcards);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/learn_list.fxml"));
            Parent root = loader.load();

            LessonListController controller = loader.getController();
            controller.initData(currentDeckName, lessonFlashcards, flashcardIndex);

            stage.setScene(new Scene(root, Color.TRANSPARENT));
        }

        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/learn_show.fxml"));
            Parent root = loader.load();

            LessonShowController controller = loader.getController();
            controller.initData(currentDeckName, lessonFlashcards, flashcardIndex);

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
