import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.*;

public class Game {


    public Scene getStartScreen(String styleSheet, Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        root.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());

        Button start = new Button("Start");
        start.setOnAction(event -> stage.setScene(getBattlefield()));
        Button settings = new Button("Einstellungen");
        settings.setOnAction(event -> stage.setScene(getSettings(stage)));

        Button quit = new Button("Beenden");
        quit.setOnAction(event -> stage.close());


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
        BorderPane controls = new BorderPane();
        Ship submarine = new Ship(4, BLUE);
        Ship battleship = new Ship(4, BLUE);
        Ship carrier = new Ship(5, BLUE);
        Ship cruiser = new Ship(3, BLUE);
        Ship destroyer = new Ship(2, BLUE);
        Group root = new Group();
        controls.setStyle("-fx-background-image: url(img/metal.jpg);" + "-fx-background-position: left top, center;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover, auto;");
        battlefield.setStyle("-fx-background-image: url(img/water.jpg);" + "-fx-background-position: left top, center;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-size: cover, auto;");
        root.getChildren().add(battlefield);
        root.getChildren().addAll(submarine.getShip(), battleship.getShip(), cruiser.getShip(), carrier.getShip(), destroyer.getShip());
        Scene scene = new Scene(root, 800, 600);

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
                region.setFill(rgb(0, 0, 200, 0.5));
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

    public Scene getSettings(Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        Button back = new Button("Zurück");
        back.setOnAction(event -> stage.setScene(getStartScreen("style.css", stage)));
        back.setMinSize(120, 50);


        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(back);

        root.setCenter(vbox);

        return scene;

    }

}
