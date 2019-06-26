import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {
    private static MediaPlayer mediaPlayer;

    //plays main theme
    public static void playMainTheme(){
        //Initialising path of the media file, replace this with your file path
        String path = "src\\Main_Theme.mp3";

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        mediaPlayer = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
    }
}
