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
        Battlefield battlefield = new Battlefield();
        Scene startScreen = startmenu.getStartScreen("style.css", stage);

        Scene gameScreen = battlefield.getGameScreen();

        stage.setScene(gameScreen);
        stage.setResizable(false);
        stage.show();
    }
}
