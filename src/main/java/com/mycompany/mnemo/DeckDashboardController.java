package com.mycompany.mnemo;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class DeckDashboardController implements Initializable {

    private String currentDeckName;

    private List<Row> deckTable;

    // FXML will initialize these buttons by itself

    @FXML HBox draggableElement;

    // Texts
    @FXML public Text reviewCount;
    @FXML public Text lessonCount;
    @FXML public Text nextHourReviews;
    @FXML public Text tomorrowReviews;

    private Utils utils = new Utils();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Make the program draggable
        utils.makeSceneDraggable(this.draggableElement, 0.8f);
    }


    // Initialize data within different controllers;

    public void initData(String currentDeckName) {
        this.currentDeckName = currentDeckName;

        this.deckTable = Database.getTable(currentDeckName);

        // Get review and lesson count
        int[] results = utils.checkReviewReady(deckTable);
        int reviews = results[0];
        int lessons = results[1];

        // Set values to text elements
        reviewCount.setText(String.valueOf(reviews));
        lessonCount.setText(String.valueOf(lessons));


        // Get amount of reviews within an hour

        results = utils.checkReviewReady(deckTable, 1);
        nextHourReviews.setText(String.valueOf(results[0]));

        // Get amount of reviews within a day;
        results = utils.checkReviewReady(deckTable, 24);
        tomorrowReviews.setText(String.valueOf(results[0]));
    }

    public void reviewFlashcards(Event event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/review_show.fxml"));
        Parent root = loader.load();

        List<Row> reviewFlashcards = new ArrayList<Row>();

        for(Row row : deckTable) {
            int lvl = row.getInt("lvl");
            double lastReviewTime = row.getDouble("last_review_time");
            double nextReviewInterval = utils.flashcardLevels.get(lvl);
            double currentTime = System.currentTimeMillis();


            if(lvl >0 && lvl <8 && (currentTime > (lastReviewTime + nextReviewInterval))) {
                reviewFlashcards.add(row);
            }
        }

        if(reviewFlashcards.size() == 0) {return;}

        ReviewController controller = loader.getController();
        controller.initData(currentDeckName, reviewFlashcards, 0);

        stage.setScene(new Scene(root, Color.TRANSPARENT));
    }


    public void learnFlashcards(Event event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/learn_list.fxml"));
        Parent root = loader.load();

        List<Row> learnFlashcards = new ArrayList<Row>();

        for(Row row : deckTable) {
            int lvl = row.getInt("lvl");

            if(lvl == 0) {
                learnFlashcards.add(row);
            }
        }

        if(learnFlashcards.size() == 0) {return;}

        LessonListController controller = loader.getController();
        controller.initData(currentDeckName, learnFlashcards, 0);

        stage.setScene(new Scene(root, Color.TRANSPARENT));
    }


    public void createNewFlashcards(Event event) throws Exception {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/flashcardCreation.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FlashcardCreationController controller = loader.getController();
        controller.initData(currentStage, currentDeckName);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root, Color.TRANSPARENT));
        stage.showAndWait();
    }




    public void goMainDashboard(Event event) throws Exception {

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_dashboard.fxml"));
        window.setScene(new Scene(root, Color.TRANSPARENT));
    }


    public void minimizeProgram(Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeProgram(Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
