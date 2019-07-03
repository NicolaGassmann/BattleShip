import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static javafx.scene.paint.Color.RED;

public class MainScreen {
    private int fieldLength;
    private int minShips = 1;

    public MainScreen(){}

    //returns the ship placing screen
    Parent getPlacingScreen() {
        int maxSameShips;
        if (!SettingsController.isLongField()) {
            fieldLength = 10;
            maxSameShips = 2;
        } else {
            fieldLength = 12;
            maxSameShips = 3;
        }
        BorderPane root = new BorderPane();
        Pane mainScreen = new Pane();
        mainScreen.setPrefHeight(600);
        mainScreen.setMinWidth(600);
        root.getStyleClass().add("mainScreen");
        root.setCenter(mainScreen);
        Rectangle placeHolder1 = new Rectangle(75, 0);
        Rectangle placeHolder2 = new Rectangle(75, 0);
        Rectangle placeHolder3 = new Rectangle(0, 50);
        Rectangle placeHolder4 = new Rectangle(0, 50);
        if(!SettingsController.isLongField()) {
            root.setLeft(placeHolder1);
            root.setRight(placeHolder2);
            root.setTop(placeHolder3);
            root.setBottom(placeHolder4);
        }
        PlacingScreen placingScreen = new PlacingScreen(mainScreen, fieldLength, maxSameShips);
        GridPane placingField = placingScreen.getPlacingField();
        Label minShipOrNotPlacedWarning = new Label("you need at least one ship to start and make sure the currently selected ship is placed!");
        minShipOrNotPlacedWarning.setTextFill(RED);
        minShipOrNotPlacedWarning.relocate(50, fieldLength*50+50);
        Button finish = new Button("finish");
        finish.relocate(fieldLength * 50 + 100, fieldLength*50+25);
        finish.setPrefWidth(255);
        finish.setOnAction(event -> {
            if (ShipCounter.getAllShips() >= minShips && placingScreen.selectedShip.isPlaced()) {
                Sound.stopMusic();
                Sound.playMusic("Main_Theme.mp3");
                mainScreen.getChildren().remove(minShipOrNotPlacedWarning);
                mainScreen.getChildren().remove(placingScreen.settings);
                mainScreen.getChildren().remove(finish);
                Group placingFieldGroup = new Group();
                placingField.getStyleClass().add("myGridStyle");
                mainScreen.getChildren().add(placingFieldGroup);
                placingFieldGroup.getChildren().add(placingField);
                for (Ship ship : placingScreen.playerShips) {
                    placingFieldGroup.getChildren().add(ship.getShip());
                    ship.getShip().toBack();
                    placingFieldGroup.getChildren().add(ship.getShootingHitBox());
                }


                ScaleTransition st = new ScaleTransition(Duration.seconds(1), placingFieldGroup);
                st.setToX(0.5);
                st.setToY(0.5);
                st.play();

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1), placingFieldGroup);
                if(SettingsController.isLongField()) {
                    tt.setToX(500);
                    tt.setToY(-150);
                }else{
                    tt.setToX(425);
                    tt.setToY(-125);
                }
                tt.play();
                tt.setOnFinished(event1->{
                    BattleScreen battleScreen = new BattleScreen(mainScreen, fieldLength, placingScreen.getPlayerShips(), placingScreen.getAiShips(), placingScreen.getTiles());
                });
            }else{
                if(!mainScreen.getChildren().contains(minShipOrNotPlacedWarning)) {
                    mainScreen.getChildren().add(minShipOrNotPlacedWarning);
                }
            }
        });
        mainScreen.getChildren().add(finish);
        mainScreen.getChildren().add(placingField);
        placingScreen.createShipSettings();
        mainScreen.getChildren().add(placingScreen.settings);

        return root;
    }
}
