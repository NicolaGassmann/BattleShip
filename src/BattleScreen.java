import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.*;

import static javafx.scene.paint.Color.*;

public class BattleScreen {
    private int fieldLength;
    private Ship selectedAiShip;
    private Ship selectedPlayerShip;
    private List<Ship> playerShips;
    private List<Ship>  aiShips;
    private List<Tile> tiles;
    private Set<Position> shotPosition = new HashSet<>();
    private int aliveAiShips;
    private int alivePlayerShips;
    private Group root;

    public BattleScreen(Group root, int fieldLength, List<Ship> ships, List<Ship> aiShips, List<Tile> tiles) {
        this.fieldLength = fieldLength;
        this.root = root;
        this.tiles = tiles;
        this.playerShips = ships;
        this.aiShips = aiShips;
        getBattleField();
    }

    public void getBattleField() {
        GridPane battleField = new GridPane();
        root.getChildren().add(battleField);
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
                        shoot(region, selectedAiShip, true);
                        aiShoot();
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
        int max = fieldLength;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        Position position = new Position(x, y);
        while (shotPosition.contains(position)) {
            x = random.nextInt(max - min) + min;
            y = random.nextInt(max - min) + min;
            position = new Position(x, y);
        }
        shotPosition.add(position);
        for (Tile tile : tiles) {
            if (position.equals(tile.getPosition())) {
                for (Ship playerShip : playerShips) {
                    if (checkIfShipHit(tile.getTile(), playerShip)) {
                        selectedPlayerShip = playerShip;
                    }
                }
                shoot(tile.getTile(), selectedPlayerShip, false);
                selectedPlayerShip = null;
            }
        }
    }

    private void shoot(Rectangle tile, Ship selectedShip, boolean player) {
        if (selectedShip != null) {
            tile.setFill(rgb(255, 0, 0, 0.5));
            selectedShip.isHit();
            if (selectedShip.isDestroyed()) {
                if(player) {
                    System.out.println("You destroyed your opponents ship" + selectedShip.getName() + "!");
                    aliveAiShips = 0;
                    for (Ship ship : aiShips) {
                        if (!ship.isDestroyed()) {
                            aliveAiShips++;
                        }
                    }
                    if (aliveAiShips == 0) {
                        WinningScreen winningScreen = new WinningScreen();
                        NavController.setScene(winningScreen.getWinningScreen("player"));
                    }
                    System.out.println("You need to destroy " + aliveAiShips + " more ships to win.");
                }else{
                    System.out.println("Your opponent destroyed your ship" + selectedShip.getName() + "!");
                    alivePlayerShips = 0;
                    for (Ship ship : playerShips) {
                        if (!ship.isDestroyed()) {
                            alivePlayerShips++;
                        }
                    }
                    if (alivePlayerShips == 0) {
                        WinningScreen winningScreen = new WinningScreen();
                        NavController.setScene(winningScreen.getWinningScreen("ai"));
                    }
                    System.out.println("You have " + alivePlayerShips + " ships left.");
                }
            }
        } else {
            tile.setFill(rgb(100, 100, 100, 0.5));
        }
    }
}
