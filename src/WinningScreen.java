import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.RED;
import static javafx.scene.paint.Color.rgb;

public class WinningScreen {
    private Pane root = new Pane();
    private List<Rectangle> colors = new ArrayList<>();

    public Parent getWinningScreen(String winner){
        colors.add(new Rectangle(NavController.getScene().getWidth(), NavController.getScene().getHeight(), rgb(255, 0, 0, 0.5)));
        colors.add(new Rectangle(NavController.getScene().getWidth(), NavController.getScene().getHeight(), rgb(0, 255, 0, 0.5)));
        colors.add(new Rectangle(NavController.getScene().getWidth(), NavController.getScene().getHeight(), rgb(0, 0, 255, 0.5)));
        root.getChildren().addAll(colors);
        for(Rectangle rectangle:colors){
            rectangle.toFront();
            rectangle.setVisible(false);
        }
        Label lblResult;
        if(winner.equals("player")){
            Sound.stopMusic();
            Sound.playMusic("Party_Hard.mp3");
            root.getStyleClass().add("deadPool");
            party();
            lblResult = new Label("YOU WIN!");
            lblResult.setScaleX(5);
            lblResult.setScaleY(5);
            lblResult.relocate(NavController.getScene().getWidth()/2-lblResult.getWidth(), NavController.getScene().getHeight()/2-lblResult.getHeight());
            root.getChildren().add(lblResult);
        }else{
            Sound.stopMusic();
            Sound.playMusic("Lost_Duel.mp3");
            Image image = new Image(getClass().getResource("Pirate_Lose.gif").toString(), NavController.getScene().getWidth(), NavController.getScene().getHeight(), false, false);
            ImageView background = new ImageView(image);
            root.getChildren().add(background);
            background.toBack();
            lblResult = new Label("you lose");
            lblResult.setScaleX(5);
            lblResult.setScaleY(5);
            lblResult.relocate(NavController.getScene().getWidth()/2-lblResult.getWidth(), NavController.getScene().getHeight()/2-lblResult.getHeight());
            root.getChildren().add(lblResult);
        }

        MainScreen mainScreen = new MainScreen();
        StartMenu startMenu = new StartMenu();
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        Button restart = new Button("Restart");
        restart.setOnAction(event->{
            NavController.setRoot(mainScreen.getPlacingScreen());
            ShipCounter.reset();
            Sound.stopMusic();
            Sound.playMusic("Tetris_Classic.mp3");
        });
        Button menu = new Button("Menu");
        menu.setOnAction(event -> {
            NavController.setRoot(startMenu.getStartScreen());
            ShipCounter.reset();
            Sound.stopMusic();
            Sound.playMusic("Great_Fairy_Fountain.mp3");
        });
        Button quit = new Button("Quit");
        quit.setOnAction(event -> NavController.getStage().close());
        buttons.getChildren().addAll(restart, menu, quit);
        root.getChildren().add(buttons);
        buttons.relocate(NavController.getScene().getWidth()/2-buttons.getWidth(), NavController.getScene().getHeight()/2+40);

        return root;
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
