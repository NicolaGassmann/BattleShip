import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Game game = new Game();
        Scene startScreen = game.getStartScreen("style.css", stage);

        stage.setScene(startScreen);
        stage.show();
    }
}
