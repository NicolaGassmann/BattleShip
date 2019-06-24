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

        Scene placingScreen = battlefield.getPlacingScreen();

        //Initialising path of the media file, replace this with your file path
        String path = "src\\Main_Theme.mp3";

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);

        stage.setScene(placingScreen);
        stage.setResizable(false);
        stage.show();
    }
}
