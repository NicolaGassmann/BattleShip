import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.scene.paint.Color.RED;

public class MainScreen {
    private int fieldLength;
    private int maxSameShips;
    private int minShips = 1;
    private Stage stage;
    private boolean longField;

    public MainScreen(){}

    //returns the ship placing screen
    Scene getPlacingScreen(boolean longField) {
        if (!longField) {
            fieldLength = 10;
            maxSameShips = 2;
        } else {
            fieldLength = 12;
            maxSameShips = 3;
        }
        Group root = new Group();
        PlacingScreen placingScreen = new PlacingScreen(root, fieldLength, maxSameShips);
        Scene scene = new Scene(root, fieldLength * 50 + 400, fieldLength * 50 + 100, new ImagePattern(new Image("img/water.jpg")));
        GridPane placingField = placingScreen.getPlacingField();
        Label minShipOrNotPlacedWarning = new Label("you need at least one ship to start and make sure the currently selected ship is placed!");
        minShipOrNotPlacedWarning.setTextFill(RED);
        minShipOrNotPlacedWarning.relocate(50, scene.getHeight()-50);
        Button finish = new Button("finish");
        finish.relocate(fieldLength * 50 + 100, scene.getHeight() / 2);
        finish.setOnAction(event -> {
            if (placingScreen.shipCounter >= minShips && placingScreen.selectedShip.isPlaced()) {
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
                ScaleTransition st = new ScaleTransition(Duration.seconds(2), placingFieldGroup);
                st.setToX(0.5);
                st.setToY(0.5);
                st.play();

                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), placingFieldGroup);
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
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        root.getChildren().add(placingField);
        placingScreen.createShipSettings();
        root.getChildren().add(placingScreen.settings);

        return scene;
    }
}
