import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        StartMenu startmenu = new StartMenu();
        Scene startScreen = startmenu.getStartScreen();
        Sound.playMusic("Great_Fairy_Fountain.mp3");
        NavController.setScene(startScreen);
        NavController.getStage().setResizable(false);
        NavController.getStage().setFullScreen(true);
        NavController.getStage().show();
    }
}
