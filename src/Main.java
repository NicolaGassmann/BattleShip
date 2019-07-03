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
        Sound.playMusic("Great_Fairy_Fountain.mp3");
        NavController.setScene(new Scene(startmenu.getStartScreen(), 1050, 700));
        NavController.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        NavController.getStage().setResizable(false);
        NavController.getStage().setFullScreen(false);

        NavController.getStage().show();
    }
}
