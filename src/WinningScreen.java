import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.rgb;

public class WinningScreen {
    private Group root = new Group();
    private List<Rectangle> colors = new ArrayList<>();

    public Scene getWinningScreen(String winner){
        int screenSize = 400;
        Scene scene = new Scene(root ,screenSize, screenSize);
        colors.add(new Rectangle(screenSize, screenSize, rgb(255, 0, 0, 0.5)));
        colors.add(new Rectangle(screenSize, screenSize, rgb(0, 255, 0, 0.5)));
        colors.add(new Rectangle(screenSize, screenSize, rgb(0, 0, 255, 0.5)));
        root.getChildren().addAll(colors);
        for(Rectangle rectangle:colors){
            rectangle.toFront();
            rectangle.setVisible(false);
        }

        if(winner.equals("player")){
            Sound.stopMusic();
            Sound.playMusic("Party_Hard.mp3");
            Image image = new Image(getClass().getResource("DeadPool.gif").toString());
            ImageView background = new ImageView(image);
            root.getChildren().add(background);
            background.toBack();
            party();
        }else{
            Sound.stopMusic();
            Sound.playMusic("Lost_Duel.mp3");
            Image image = new Image(getClass().getResource("Pirate_Lose.gif").toString(), screenSize, screenSize, false, false);
            ImageView background = new ImageView(image);
            root.getChildren().add(background);
            background.toBack();
        }

        return scene;
    }

    private void party(){
        Timeline timeline = new Timeline();
        for(int i = 0; i < colors.size(); i++) {
            int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i*0.5+0.5), event -> {
                for(Rectangle color:colors){
                    color.setVisible(false);
                }
                colors.get(index).setVisible(true);
            }));
        }
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
