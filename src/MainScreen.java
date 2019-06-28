import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainScreen {
    private int fieldLength;
    private int maxShips;
    private int minShips = 1;
    private Stage stage;
    private boolean longField = false;

    public MainScreen(Stage stage){
        this.stage = stage;
    }

    //returns the ship placing screen
    Scene getPlacingScreen(int maxShips) {
        this.maxShips = maxShips;
        if (maxShips <= 5) {
            fieldLength = 10;
        } else {
            fieldLength = 12;
            longField = true;
        }
        PlacingField placingField = new PlacingField(fieldLength, maxShips);
        BattleField battleField = new BattleField(fieldLength);
        Group root = new Group();
        Scene scene = new Scene(root, fieldLength * 50 + 400, fieldLength * 50 + 100, new ImagePattern(new Image("img/water.jpg")));
        GridPane placingField1 = placingField.getPlacingField(root);
        Button finish = new Button("finish");
        finish.relocate(fieldLength * 50 + 100, scene.getHeight() / 2);
        finish.setOnAction(event -> {
            if (placingField.shipCounter >= minShips) {
                //makes the grid small
                root.getChildren().add(battleField.getBattleField(root));
                root.getChildren().remove(placingField.settings);
                root.getChildren().remove(finish);
                Group placingFieldGroup = new Group();
                placingField1.getStyleClass().add("myGridStyle");
                root.getChildren().add(placingFieldGroup);
                placingFieldGroup.getChildren().add(placingField1);
                for (Ship ship : placingField.ships) {
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
