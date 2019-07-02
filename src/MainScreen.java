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

    public MainScreen(){ }

    //returns the ship placing screen
    Scene getPlacingScreen(boolean longField) {
        if (!longField) {
            fieldLength = 10;
            maxSameShips = 2;
        } else {
            fieldLength = 12;
            maxSameShips = 3;
        }
        PlacingField placingField = new PlacingField(fieldLength, maxSameShips);
        BattleField battleField = new BattleField(fieldLength);
        Group root = new Group();
        Scene scene = new Scene(root, fieldLength * 50 + 400, fieldLength * 50 + 100, new ImagePattern(new Image("img/water.jpg")));
        GridPane placingField1 = placingField.getPlacingField(root);
        GridPane battleField1 = battleField.getBattleField(root, placingField.getPlayerShips(), placingField.getAiShips(), placingField.getTiles());
        Label minShipOrNotPlacedWarning = new Label("you need at least one ship to start and make sure the currently selected ship is placed!");
        minShipOrNotPlacedWarning.setTextFill(RED);
        minShipOrNotPlacedWarning.relocate(50, scene.getHeight()-50);
        Button finish = new Button("finish");
        finish.relocate(fieldLength * 50 + 100, scene.getHeight() / 2);
        finish.setOnAction(event -> {
            if (placingField.shipCounter >= minShips && placingField.selectedShip.isPlaced()) {
                root.getChildren().remove(minShipOrNotPlacedWarning);
                root.getChildren().add(battleField1);
                root.getChildren().remove(placingField.settings);
                root.getChildren().remove(finish);
                Group placingFieldGroup = new Group();
                placingField1.getStyleClass().add("myGridStyle");
                root.getChildren().add(placingFieldGroup);
                placingFieldGroup.getChildren().add(placingField1);
                for (Ship ship : placingField.playerShips) {
                    placingFieldGroup.getChildren().add(ship.getShip());
                    ship.getShip().toBack();
                }
                ScaleTransition st = new ScaleTransition(Duration.seconds(2), placingFieldGroup);
                st.setToX(0.5);
                st.setToY(0.5);
                st.play();

                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), placingFieldGroup);
                if(longField) {
                    tt.setToX(475);
                    tt.setToY(-150);
                }else{
                    tt.setToX(400);
                    tt.setToY(-125);
                }
                tt.play();
            }else{
                if(!root.getChildren().contains(minShipOrNotPlacedWarning)) {
                    root.getChildren().add(minShipOrNotPlacedWarning);
                }
            }
        });
        root.getChildren().add(finish);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        root.getChildren().add(placingField1);
        placingField.createShipSettings(root);
        root.getChildren().add(placingField.settings);

        return scene;
    }
}
