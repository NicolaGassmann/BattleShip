import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        GridPane battlefield = new GridPane();
        Group root = new Group();
        root.getChildren().add(battlefield);
        Scene scene = new Scene(root, 800, 600);

        int x = 0;
        int y = 0;
        for(int i = 0; i < 100; i++){
            Region region = new Region();
            region.getStyleClass().add("region");
            battlefield.add(region, x, y);
            System.out.println("y = " + y);
            System.out.println("x = " + x);
            x++;
            if(x > 9){
                x = 0;
                y++;
            }
        }
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
