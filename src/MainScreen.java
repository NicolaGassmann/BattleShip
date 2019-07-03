import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import static javafx.scene.paint.Color.RED;

public class MainScreen {
    private int fieldLength;
    private int minShips = 1;

    public MainScreen(){}

    //returns the ship placing screen
    Parent getPlacingScreen(boolean longField) {
        int maxSameShips;
        if (!longField) {
            fieldLength = 10;
            maxSameShips = 2;
        } else {
            fieldLength = 12;
            maxSameShips = 3;
        }
        Group root = new Group();
        root.getStyleClass().add("mainScreen");
        PlacingScreen placingScreen = new PlacingScreen(root, fieldLength, maxSameShips);
        GridPane placingField = placingScreen.getPlacingField();
        Label minShipOrNotPlacedWarning = new Label("you need at least one ship to start and make sure the currently selected ship is placed!");
        minShipOrNotPlacedWarning.setTextFill(RED);
        minShipOrNotPlacedWarning.relocate(50, NavController.getScene().getHeight()-50);
        Button finish = new Button("finish");
        finish.relocate(fieldLength * 50 + 100, NavController.getScene().getHeight()-75);
        finish.setPrefWidth(255);
        finish.setOnAction(event -> {
            if (ShipCounter.getAllShips() >= minShips && placingScreen.selectedShip.isPlaced()) {
                Sound.stopMusic();
                Sound.playMusic("Main_Theme.mp3");
                root.getChildren().remove(minShipOrNotPlacedWarning);
                root.getChildren().remove(placingScreen.settings);
                root.getChildren().remove(finish);
                Group placingFieldGroup = new Group();
                placingField.getStyleClass().add("myGridStyle");
                root.getChildren().add(placingFieldGroup);
                placingFieldGroup.getChildren().add(placingField);
                for (Ship ship : placingScreen.playerShips) {
                    placingFieldGroup.getChildren().add(ship.getShip());
                    ship.getShip().toBack();
                }
                ScaleTransition st = new ScaleTransition(Duration.seconds(1), placingFieldGroup);
                st.setToX(0.5);
                st.setToY(0.5);
                st.play();

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1), placingFieldGroup);
                if(longField) {
                    tt.setToX(500);
                    tt.setToY(-150);
                }else{
                    tt.setToX(425);
                    tt.setToY(-125);
                }
                tt.play();
                tt.setOnFinished(event1->{
                    BattleScreen battleScreen = new BattleScreen(root, fieldLength, placingScreen.getPlayerShips(), placingScreen.getAiShips(), placingScreen.getTiles());
                });
            }else{
                if(!root.getChildren().contains(minShipOrNotPlacedWarning)) {
                    root.getChildren().add(minShipOrNotPlacedWarning);
                }
            }
        });
        root.getChildren().add(finish);
        root.getChildren().add(placingField);
        placingScreen.createShipSettings();
        root.getChildren().add(placingScreen.settings);

        return root;
    }
}
