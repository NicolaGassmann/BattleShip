import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
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

        Slider difficulty = new Slider();
        difficulty.setMin(1);
        difficulty.setMax(3);
        difficulty.setValue(2);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(0);
        difficulty.setBlockIncrement(1);
        difficulty.setMaxWidth(100);
        difficulty.valueProperty().addListener((obs, oldVal, newVal) -> {
            difficulty.setValue(Math.round(newVal.doubleValue()));
            SettingsController.setDifficulty((int) difficulty.getValue());
            System.out.println(difficulty.getValue());
        } );

        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(difficulty, longfield, back);

        root.setCenter(vbox);

        return root;

    }

}
