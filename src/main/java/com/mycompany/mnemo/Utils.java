package com.mycompany.mnemo;

import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Utils {

    Stage stage;

    // Draggable offset
    private static double xOffset;
    private double yOffset;

    // Time Intervals
    protected List<Double> flashcardLevels;

    // Deck colors
    protected final ArrayList<String> color;
    protected final ArrayList<String> colorDark;

    // Strings
    protected String deckIndexTableAttributes = "id INTEGER PRIMARY KEY, deckID TEXT, title TEXT, subject TEXT, color TEXT, color_dark TEXT";
    protected String deckTableAttributes = "id INTEGER PRIMARY KEY, question TEXT, answer TEXT, type TEXT, lvl INTEGER, last_review_time DOUBLE, review_ready_time INTEGER";

    public Utils() {
        // Initialize array list with time intervals for levels
        flashcardLevels = new ArrayList<Double>();
        Collections.addAll(flashcardLevels, 0d, 14400000d, 28800000d, 82800000d, 169200000d, 601200000d,
                1206000000d, 2588400000d, 2588400000d);

        // Initialize array list with color values for decks
        color = new ArrayList<String>();
        colorDark = new ArrayList<String>();
        Collections.addAll(color, "#AD8CF4", "#42CDBD", "#45BFEE", "#FF6C44");
        Collections.addAll(colorDark, "#9E80DE", "#3CBBAC", "#3FAED9", "#E8633E");
    }


    // Makes the program draggable
    public void makeSceneDraggable(Node parent, float opacity) {

        parent.setOnMousePressed(e -> {
            // Get the scene
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            // Get the initial position
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        // Set stage's x and y by calculating drag value minus offset from the original position
        parent.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
            stage.setOpacity(opacity);
        });

        parent.setOnDragDone((e) -> {
            stage.setOpacity(1.0f);
        });

        parent.setOnMouseReleased((e) -> {
            // Get the scene
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });
    }


    // Check how many reviews are ready for review and how many lessons are out there.
    public int[] checkReviewReady(List<Row> deckTable, int hours) {

        double miliHours = hours * 3600000d;
        int[] reviewAndLessonCount = {0, 0};

        for (Row row : deckTable) {
            // Extract the data
            int lvl = row.getInt("lvl");
            double lastReviewTime = row.getDouble("last_review_time");
            double nextReviewInterval = flashcardLevels.get(lvl);
            double currentTime = System.currentTimeMillis();

            // Increase lesson count
            if (lvl == 0) {
                reviewAndLessonCount[1]++;
            }
            // Increase review count

            else if(hours == 0 && (currentTime > (lastReviewTime + nextReviewInterval))) {
                reviewAndLessonCount[0]++;
            }

            else if (hours > 0 && (currentTime + miliHours) > (lastReviewTime + nextReviewInterval)
                    && (currentTime < (lastReviewTime + nextReviewInterval))) {
                reviewAndLessonCount[0]++;
            }
        }

        return reviewAndLessonCount;
    }

    public int[] checkReviewReady(List<Row> deckTable) { return checkReviewReady(deckTable, 0); }



}
