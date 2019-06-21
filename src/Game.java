import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

import static javafx.scene.paint.Color.*;

public class Game {
    private Ship selectedBoat;

    public Scene getStartScreen(String styleSheet, Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        root.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());

        Button start = new Button("Start");
        start.setOnAction(event-> stage.setScene(getBattlefield()));
        Button settings = new Button("Einstellungen");
        Button quit = new Button("Beenden");

        start.setMinSize(120, 50);
        settings.setMinSize(120, 50);
        quit.setMinSize(120, 50);

        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(start, settings, quit);

        root.setCenter(vbox);

        return scene;
    }

    public Scene getBattlefield() {
        GridPane battlefield = new GridPane();
        Ship submarine = new Ship(5, BLUE);
        Group root = new Group();
        root.getChildren().add(submarine.getShip());
        root.getChildren().add(battlefield);
        selectedBoat = submarine;
        Scene scene = new Scene(root, 800, 600, new ImagePattern(new Image("img/water.jpg")));

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        int x = 0;
        int y = 0;
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile(x, y);
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            region.setStrokeWidth(1);
            region.setStroke(BLACK);

            region.setOnMouseEntered(mouse -> {
                double newX = tile.getX()*51;
                double newY = tile.getY()*51;
                selectedBoat.moveShip(newX, newY);
                selectedBoat.getPosition();
            });
            region.setOnMouseClicked(event->{
                if(event.getButton() == MouseButton.PRIMARY) {
                    selectedBoat = new Ship(1, BLACK);
                }
            });
            battlefield.add(region, x, y);
            x++;
            if (x > 9) {
                x = 0;
                y++;
            }
        }
        scene.setOnMouseClicked(event->{
            if (event.getButton() == MouseButton.SECONDARY) {
                submarine.changeDirection();
            }
        });
        return scene;

    }


}
