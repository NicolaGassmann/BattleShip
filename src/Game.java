import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class Game {


    public Scene createScene(String styleSheet){
        GridPane start_background = new GridPane();
        Group root = new Group();
        root.getChildren().add(start_background);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());

        return scene;
    }




}
