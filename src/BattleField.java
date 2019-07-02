import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.scene.paint.Color.*;

public class BattleField {
    private int fieldLength;
    private Ship selectedAiShip;
    private Ship selectedPlayerShip;
    private List<Ship> playerShips = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private List<int[]> shotCords = new ArrayList<>();

    public BattleField(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public GridPane getBattleField(Group root,List<Ship> ships, List<Ship> aiShips, List<Tile> tiles) {
        GridPane battleField = new GridPane();
        this.tiles = tiles;
        this.playerShips = ships;
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
                                selectedAiShip = aiShip;
                            }
                        }
                        aiShoot();
                        if (selectedAiShip != null) {
                            region.setFill(rgb(255, 0, 0, 0.5));
                            selectedAiShip.isHit();
                            if (selectedAiShip.isDestroyed()) {
                                System.out.println("You destroyed your opponents ship" + selectedAiShip.getName() + "!");
                            }
                        } else {
                            region.setFill(rgb(100, 100, 100, 0.5));
                        }
                        selectedAiShip = null;
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

    private void aiShoot() {
        //creates two random numbers between 0 and 9 which will be the shots coordinates
        Random random = new Random();
        int min = 0;
        int max = fieldLength - 1;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        int[] cords = {x, y};
        for(;shotCords.contains(cords);){
            System.out.println("new cords");
            x = random.nextInt(max - min) + min;
            y = random.nextInt(max - min) + min;
            cords = new int[]{x, y};
        }
        shotCords.add(cords);
        for(Tile tile:tiles){
            if(tile.getPosition().getX() == x && tile.getPosition().getY() == y){
                for (Ship playerShip : playerShips) {
                    if (checkIfShipHit(tile.getTile(), playerShip)) {
                        selectedPlayerShip = playerShip;
                    }
                }
                if (selectedPlayerShip != null) {
                    tile.getTile().setFill(rgb(255, 0, 0, 0.5));
                    selectedPlayerShip.isHit();
                    if (selectedPlayerShip.isDestroyed()) {
                        System.out.println("Your opponent destroyed your ship" + selectedPlayerShip.getName() + "!");
                    }
                } else {
                    tile.getTile().setFill(rgb(100, 100, 100, 0.5));
                }
                selectedPlayerShip = null;
            }
        }
    }
}
