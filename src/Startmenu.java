import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Startmenu {

    public Scene getStartScreen(String styleSheet, Stage stage) {
        Battlefield bf = new Battlefield();
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 850, 600);

        root.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());

        Rectangle glass = new Rectangle(200, 400);
        glass.setStyle("-fx-fill: white;");

        glass.setOpacity(0.5);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        ImageView logo = new ImageView("img/logo.png");
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        Button start = new Button("Start");
        start.setOnAction(event -> stage.setScene(bf.getPlacingScreen()));
        Button settings = new Button("Einstellungen");
        settings.setOnAction(event -> stage.setScene(getSettings(stage)));
        Button quit = new Button("Beenden");
        quit.setOnAction(event -> stage.close());

        start.setMinSize(120, 50);
        settings.setMinSize(120, 50);
        quit.setMinSize(120, 50);
        vbox.getChildren().addAll(logo, start, settings, quit);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(glass, vbox);

        return scene;
    }

    public Scene getSettings(Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 850, 600);

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
