import javafx.scene.Scene;
import javafx.scene.Group;

public class WinningScreen {
    public Scene getWinningScreen(String winner){
        Group root = new Group();
        Scene scene = new Scene(root ,400, 400);
        return scene;
    }
}
