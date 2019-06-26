import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Startmenu startmenu = new Startmenu();
        Battlefield battlefield = new Battlefield();
        Scene startScreen = startmenu.getStartScreen("style.css", stage);
        Sound.playMainTheme();

        Scene placingScreen = battlefield.getPlacingScreen();

        stage.setScene(startScreen);
        stage.setResizable(false);
        stage.show();
    }
}
