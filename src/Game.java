import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.*;

public class Game {


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
        Ship submarine = new Ship(4, BLUE);
        Group root = new Group();
        root.getChildren().add(battlefield);
        root.getChildren().add(submarine.getShip());
        Scene scene = new Scene(root, 800, 600, new ImagePattern(new Image("img/water.jpg")));

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        int x = 0;
        int y = 0;
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile();
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            region.setStrokeWidth(1);
            region.setStroke(BLACK);

            region.setOnMouseEntered(mouse -> {
                submarine.moveShip(region.getX(), region.getY());
                region.setFill(rgb(0,0,200,0.5));
            });
            region.setOnMouseExited(event -> {
                region.setFill(rgb(0, 0, 0, 0));
            });
            battlefield.add(region, x, y);
            System.out.println("y = " + region.getY());
            System.out.println("x = " + region.getX());
            x++;
            if (x > 9) {
                x = 0;
                y++;
            }
        }

        return scene;

    }


}
