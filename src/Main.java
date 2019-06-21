import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;

import static javafx.scene.paint.Color.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GridPane battlefield = new GridPane();
        Ship submarine = new Ship(4, BLUE);
        Group root = new Group();
        root.getChildren().add(battlefield);
        root.getChildren().add(submarine.getShip());
        Scene scene = new Scene(root, 800, 600);

        int x = 0;
        int y = 0;
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile();
            Rectangle region = tile.getTile();
            /*region.setFill(SKYBLUE);
            region.setStroke(BLACK);
            region.setStrokeWidth(1);
            */
            region.getStyleClass().add("region");

            region.setOnMouseEntered(mouse->{
                submarine.moveShip(region.getX(), region.getY());
                region.getStyleClass().add("selected");
            });
            region.setOnMouseExited(event->{
                region.getStyleClass().remove("selected");
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
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
