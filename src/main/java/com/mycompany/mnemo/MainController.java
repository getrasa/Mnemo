package com.mycompany.mnemo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {


    @FXML HBox draggableElement;
    @FXML ScrollPane scrollBar;
    @FXML GridPane addHere;

    @FXML TextField searchBar;

    private Utils utils = new Utils();

    private int deckCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Create deck index table if one doesn't yet exist.
        Database.createTable(utils.deckIndexTableAttributes);

        // Load deck index table containing all information about decks.
        List<Row> deckIndexTable = Database.getTable();

        // Count decks
        deckCount = deckIndexTable.size();

        // Using index table add buttons to the scrollPane.
        addDeckButtons(deckIndexTable);


        // Make the program draggable
        utils.makeSceneDraggable(this.draggableElement, 0.8f);


        // Turn off auto focus for search bar
        searchBar.setFocusTraversable(false);
    }


    public void addDeckButtons(List<Row> deckIndexTable) {
        // Get the amount of decks
        int deckSize = deckIndexTable.size();

        try {
            // Delete the scroll bar in scrollPane
            scrollBar.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            // Set padding to the Grid Pane
            addHere.setPadding(new Insets(20, 40, 50, 55));

            // Add padding to each grids frame
            addHere.setHgap(30);
            addHere.setVgap(35);

            // Add deck buttons
            for(int i=0; i<deckSize; i++) {
                // Get row and column index
                int rows = (int) i/3;
                int columns = i % 3;

                // Load the button
                Parent button = getFXMLButton(deckIndexTable.get(i));
                GridPane.setConstraints(button, columns, rows);
                addHere.getChildren().add(button);
            }

        }catch (Exception e) {
            System.out.println("Something went wrong while adding the buttons");
        }
    }


    public Parent getFXMLButton(Row deck) throws Exception {
        // Load FXML file
        //Parent deckButton = FXMLLoader.load(getClass().getResource("fxml/deckButton.fxml"));

        // Save data to variables from deck row containing basic information about deck
        String deckName = deck.getString("deckID");
        String title = deck.getString("title");
        String subject = deck.getString("subject");
        String color = deck.getString("color");
        String colorDark = deck.getString("color_dark");

        // Load the deck table from db
        List<Row> deckTable = Database.getTable(deckName);

        // Check review and lesson ready flashcards
        int[] results = utils.checkReviewReady(deckTable);
        int reviewCount = results[0];
        int lessonCount = results[1];

        // Load FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/deckButton.fxml"));
        Parent deckButton = loader.load();


        // Initialize the controller
        DeckButtonController controller = loader.getController();
        controller.initData(deckName);

        // Get the text labels
        Text titleLabel = (Text) deckButton.lookup("#titleLabel");
        Text subjectLabel = (Text) deckButton.lookup("#subjectLabel");

        // Get the color bars
        HBox colorBar = (HBox) deckButton.lookup("#buttonColorBar");
        HBox colorBarDark = (HBox) deckButton.lookup("#buttonColorBarDark");

        // Get the review labels
        Text reviewLabel = (Text) deckButton.lookup("#reviewLabel");
        Text lessonLabel = (Text) deckButton.lookup("#lessonLabel");

        //Set text
        titleLabel.setText(title);
        titleLabel.setWrappingWidth(215);

        subjectLabel.setText(subject);
        reviewLabel.setText(reviewCount + " Reviews");
        lessonLabel.setText(lessonCount + " Lessons");


        // Set color color bars
        colorBar.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color:" + color);



        colorBarDark.setStyle("-fx-background-radius: 0 0 5 0; -fx-background-color:" + colorDark);


        // Set id for color bars to make it distinct from other buttons
        colorBarDark.setId(deckName);


        // Return whole deck button
        return deckButton;
    }


    public void createNewDeck(Event e) throws Exception{
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/deckCreation.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        DeckCreationController controller = loader.getController();
        controller.initData(currentStage, deckCount);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(root, Color.TRANSPARENT));
        stage.showAndWait();
    }

    public void minimizeProgram(Event e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeProgram(Event e)  {
        // Get the stage and close the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
}
