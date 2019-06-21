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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.*;

import static javafx.scene.paint.Color.*;

public class Startmenu {

    public Scene getStartScreen(String styleSheet, Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        Battlefield battlefield = new Battlefield();
        root.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());

        Button start = new Button("Start");
        start.setOnAction(event -> stage.setScene(battlefield.getGameScreen()));
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
}
