package com.mycompany.mnemo;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckButtonController implements Initializable {

    // Important strings
    private static final String URL = "jdbc:sqlite:testjava.db";
    private static final String deckListTableName = "deckListTable";

    @FXML Text reviewLabel;
    @FXML Text titleLabel;
    @FXML Text subjectLabel;

    private String deckName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void goToDeckDashboard(Event event) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/deckDashboard.fxml"));
        Parent root = loader.load();

        DeckDashboardController controller = loader.getController();
        controller.initData(deckName);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, Color.TRANSPARENT));

    }


    public void deckButtonOptions(MouseEvent event) throws Exception {


        Node button = ((Node) event.getSource());

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(e -> editDeck(event));

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> deleteDeck(event));


        final ContextMenu menu = new ContextMenu(edit, delete);

        menu.show(button, event.getScreenX(), event.getScreenY());


        // Get the name table from clicked button
        String tableName = ((Node) event.getSource()).getId();

        System.out.println(tableName);
    }


    private void editDeck(Event event) {


        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/deckEdit.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            DeckEditController controller = loader.getController();
            String deckID = ((Node) event.getSource()).getId();
            controller.initData(currentStage, deckID, titleLabel.getText(), subjectLabel.getText());

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root, Color.TRANSPARENT));
            stage.showAndWait();

        } catch (Exception e) {
            System.out.println("Something went wrong while opening deck edit");
        }

    }





    private void deleteDeck(Event event) {
        // Get the name table from clicked button
        String tableName = ((Node) event.getSource()).getId();

        System.out.println(tableName);

        // Delete deck's table
        Database.dropTable(tableName);

        // Delete decks record from index table
        Database.deleteFromTable("WHERE deckID = '" + tableName + "'");

        try {
            // Refresh the stage
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, Color.TRANSPARENT));

        } catch(Exception e) {
            System.out.println("Something went wrong while refreshing deck buttons");
        }
    }

    public void initData(String deckName) {
        this.deckName = deckName;
    }


}
