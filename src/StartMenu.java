import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class StartMenu {

    private CheckBox longfield = new CheckBox("long field");

    public Scene getStartScreen() {
        MainScreen mainScreen = new MainScreen();
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 900, 600);

        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

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
        start.setOnAction(event -> {
            NavController.setScene(mainScreen.getPlacingScreen(longfield.isSelected()));
            Sound.stopMusic();
            Sound.playMusic("Tetris_Classic.mp3");
        });

        Button settings = new Button("Options");
        settings.setOnAction(event -> NavController.setScene(getSettings()));
        Button quit = new Button("Quit");
        quit.setOnAction(event -> NavController.getStage().close());

        start.setMinSize(120, 50);
        settings.setMinSize(120, 50);
        quit.setMinSize(120, 50);
        vbox.getChildren().addAll(logo, start, settings, quit);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(glass, vbox);



        return scene;
    }

    public Scene getSettings() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 850, 600);

        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        Button back = new Button("Zurück");
        back.setOnAction(event -> NavController.setScene(getStartScreen()));
        back.setMinSize(120, 50);

        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(longfield, back);

        root.setCenter(vbox);

        return scene;

    }

}
