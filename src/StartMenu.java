import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.rgb;

public class StartMenu {

    public Parent getStartScreen() {
        MainScreen mainScreen = new MainScreen();
        StackPane root = new StackPane();
        root.getStyleClass().add("battleShip");
        Rectangle glass = new Rectangle(200, 400);
        glass.setFill(rgb(255, 255, 255, 0.5));

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);

        ImageView logo = new ImageView("img/logo.png");
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        Button start = new Button("Start");
        start.setOnAction(event -> {
            NavController.setRoot(mainScreen.getPlacingScreen());
            Sound.stopMusic();
            Sound.playMusic("Tetris_Classic.mp3");
        });

        Button settings = new Button("Options");
        settings.setOnAction(event -> NavController.setRoot(getSettings()));
        Button quit = new Button("Quit");
        quit.setOnAction(event -> NavController.getStage().close());

        start.setMinSize(120, 50);
        settings.setMinSize(120, 50);
        quit.setMinSize(120, 50);
        vbox.getChildren().addAll(logo, start, settings, quit);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(glass, vbox);



        return root;
    }

    private Parent getSettings() {
        BorderPane root = new BorderPane();

        CheckBox longfield = new CheckBox("long field");
        longfield.setOnAction(event->{
            if(longfield.isSelected()){
                SettingsController.setLongField(true);
            }else{
                SettingsController.setLongField(false);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> NavController.setRoot(getStartScreen()));
        back.setMinSize(120, 50);

        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(longfield, back);

        root.setCenter(vbox);

        return root;

    }

}
