import javafx.scene.Group;
import javafx.scene.control.TextArea;
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
    private List<Ship> aiShips;
    private List<Tile> tiles;
    private Set<Position> shotPosition = new HashSet<>();
    private int aliveAiShips;
    private int alivePlayerShips;
    private Group root;
    private TextArea log = new TextArea();

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
        log.setWrapText(true);
        log.setLayoutX(fieldLength * 50 + 100);
        log.setLayoutY(fieldLength * 25 + 75);
        log.setPrefSize(fieldLength * 25, fieldLength * 25 - 25);
        log.setEditable(false);
        root.getChildren().add(battleField);
        root.getChildren().add(log);
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
                        String yOutput = numberToLetter(tile.getPosition().getY()+1, true);
                        String xOutput = Integer.toString(tile.getPosition().getX());
                        log.appendText("Player: " + yOutput + "/" + xOutput + "\r\n");
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
                String yOutput = numberToLetter(tile.getPosition().getY()+1, true);
                String xOutput = Integer.toString(tile.getPosition().getX());
                log.appendText("Computer: " + yOutput + "/" + xOutput + "\r\n");
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
                if (player) {
                    log.appendText("You destroyed your opponents ship" + selectedShip.getName() + "!" + "\r\n");
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
                    log.appendText("You need to destroy " + aliveAiShips + " more ships to win." + "\r\n");
                } else {
                    log.appendText("Your opponent destroyed your ship" + selectedShip.getName() + "!" + "\r\n");
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
                    log.appendText("You have " + alivePlayerShips + " ships left." + "\r\n");
                }
            }
        } else {
            tile.setFill(rgb(200, 200, 200, 0.7));
        }
    }

    //makes a number from 1 to 26 in the equivalent letter, capitalist or not
    private String numberToLetter(int number, boolean capitals) {
        String letter = "";
        if (capitals) {
            switch (number) {
                case 1:
                    letter = "A";
                    break;
                case 2:
                    letter = "B";
                    break;
                case 3:
                    letter = "C";
                    break;
                case 4:
                    letter = "D";
                    break;
                case 5:
                    letter = "E";
                    break;
                case 6:
                    letter = "F";
                    break;
                case 7:
                    letter = "G";
                    break;
                case 8:
                    letter = "H";
                    break;
                case 9:
                    letter = "I";
                    break;
                case 10:
                    letter = "J";
                    break;
                case 11:
                    letter = "K";
                    break;
                case 12:
                    letter = "L";
                    break;
                case 13:
                    letter = "M";
                    break;
                case 14:
                    letter = "N";
                    break;
                case 15:
                    letter = "O";
                    break;
                case 16:
                    letter = "P";
                    break;
                case 17:
                    letter = "Q";
                    break;
                case 18:
                    letter = "R";
                    break;
                case 19:
                    letter = "S";
                    break;
                case 20:
                    letter = "T";
                    break;
                case 21:
                    letter = "U";
                    break;
                case 22:
                    letter = "V";
                    break;
                case 23:
                    letter = "W";
                    break;
                case 24:
                    letter = "X";
                    break;
                case 25:
                    letter = "Y";
                    break;
                case 26:
                    letter = "Z";
                    break;
            }
        } else {
            switch (number) {
                case 1:
                    letter = "a";
                    break;
                case 2:
                    letter = "b";
                    break;
                case 3:
                    letter = "c";
                    break;
                case 4:
                    letter = "d";
                    break;
                case 5:
                    letter = "e";
                    break;
                case 6:
                    letter = "f";
                    break;
                case 7:
                    letter = "g";
                    break;
                case 8:
                    letter = "h";
                    break;
                case 9:
                    letter = "i";
                    break;
                case 10:
                    letter = "j";
                    break;
                case 11:
                    letter = "k";
                    break;
                case 12:
                    letter = "l";
                    break;
                case 13:
                    letter = "m";
                    break;
                case 14:
                    letter = "n";
                    break;
                case 15:
                    letter = "o";
                    break;
                case 16:
                    letter = "p";
                    break;
                case 17:
                    letter = "q";
                    break;
                case 18:
                    letter = "r";
                    break;
                case 19:
                    letter = "s";
                    break;
                case 20:
                    letter = "t";
                    break;
                case 21:
                    letter = "u";
                    break;
                case 22:
                    letter = "v";
                    break;
                case 23:
                    letter = "w";
                    break;
                case 24:
                    letter = "x";
                    break;
                case 25:
                    letter = "y";
                    break;
                case 26:
                    letter = "z";
                    break;
            }
        }
        return letter;
    }
}