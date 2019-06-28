import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.rgb;

public class BattleField {
    private int fieldLength;

    public BattleField(int fieldLength){
        this.fieldLength = fieldLength;
    }
    public GridPane getBattleField(Group root) {
        GridPane battleField = new GridPane();
        battleField.setLayoutX(50);
        battleField.setLayoutY(50);
        int x = 0;
        int y = 0;
        //adds all 100 tiles to the grid and adds the mouse events
        for (int i = 0; i < Math.pow(fieldLength, 2); i++) {
            Tile tile = new Tile(x, y);
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            battleField.setGridLinesVisible(true);
            battleField.getStyleClass().add("battleField");

            region.setOnMouseEntered(mouse -> {});
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {}
            });
            battleField.add(region, x, y);
            x++;
            if (x > fieldLength - 1) {
                x = 0;
                y++;
            }
        }
        return battleField;
    }
}
