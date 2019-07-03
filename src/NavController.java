import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavController {
    private static Stage stage = new Stage();

    public static Stage getStage() {
        return stage;
    }

    public static void setScene(Scene scene){
        stage.setScene(scene);
    }

    public static Scene getScene() { return stage.getScene(); }

    public static void setRoot(Parent root) { stage.getScene().setRoot(root); }
}
