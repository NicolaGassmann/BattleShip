import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {
    private static MediaPlayer music;
    private static MediaPlayer sound;
    private static MediaPlayer shot;

    public static void stopMusic() {
        music.stop();
        if(sound != null) {
            sound.stop();
        }
        if(shot != null){
            shot.stop();
        }
    }

    //plays an sound file from the resources/music folder
    public static void playMusic(String musicName){
        //Initialising path of the media file, replace this with your file path
        String path = "resources\\music\\" + musicName;

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        music = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        music.setVolume(100);
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.setAutoPlay(true);
    }

    public static void playSound(String soundName){
        //Initialising path of the media file, replace this with your file path
        String path = "resources\\music\\" + soundName;

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        sound = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        sound.setVolume(130);
        sound.setCycleCount(1);
        sound.play();
    }

    public static void playShot(){
        //Initialising path of the media file, replace this with your file path
        String path = "resources\\music\\Shot.mp3";

        //Instantiating Media class
        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class
        shot = new MediaPlayer(media);

        //by setting autoPlay to true, the audio will be played
        shot.setVolume(70);
        shot.setCycleCount(1);
        shot.play();
    }
}
