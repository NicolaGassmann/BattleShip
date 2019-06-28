import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Startmenu startmenu = new Startmenu();
        MainScreen mainScreen = new MainScreen(stage);
        Scene startScreen = startmenu.getStartScreen("style.css", stage);
        Sound.playMainTheme();

        //Scene placingScreen = placingField.getPlacingScreen(6);

        stage.setScene(startScreen);
        stage.setResizable(false);
        stage.show();
    }
}
