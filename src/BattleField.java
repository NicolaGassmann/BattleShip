import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.List;

import static javafx.scene.paint.Color.*;

public class BattleField {
    private int fieldLength;

    public BattleField(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public GridPane getBattleField(Group root, List<Ship> aiShips) {
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

            region.setOnMouseEntered(mouse -> {
                //region.setFill(rgb(255, 0, 0, 0.5));
            });
            region.setOnMouseExited(event -> {
                //region.setFill(rgb(0, 0 ,0, 0));
            });
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (region.getFill().equals(rgb(0, 0, 0, 0))) {
                        for (Ship aiShip : aiShips) {
                            if (checkIfShipHit(region, aiShip)) {
                                region.setFill(rgb(255, 0, 0, 0.5));
                                aiShip.isHit();
                                if(aiShip.isDestroyed()){
                                    System.out.println("You destroyed your opponents ship" + aiShip.getName() + "!");
                                }
                            } else {
                                region.setFill(rgb(100, 100, 100, 0.5));

                            }
                        }
                    }
                }
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

    private boolean checkIfShipHit(Shape block, Ship ship) {
        boolean collisionDetected = false;
        if (ship.getShip() != block) {
            Shape intersect = Shape.intersect(block, ship.getShip());
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                collisionDetected = true;
            }
        }
        return collisionDetected;
    }
}
