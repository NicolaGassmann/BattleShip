import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {
    private static MediaPlayer mediaPlayer;

    public static void stopMusic(){
        mediaPlayer.stop();
    }

    //plays an sound file from the resources/music folder
    public static void playMusic(String music){
        //Initialising path of the media file, replace this with your file path
        String path = "resources\\music\\" + music;

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        mediaPlayer = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(true);
    }
}
