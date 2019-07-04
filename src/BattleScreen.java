import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.*;

import static javafx.scene.paint.Color.*;

public class BattleScreen {
    private int fieldLength;
    private Ship selectedAiShip;
    private Ship selectedPlayerShip;
    private Tile selectedAiTile;
    private List<Ship> playerShips;
    private List<Ship> aiShips;
    private List<Tile> tiles;
    private List<Position> shotPositions = new ArrayList<>();
    private Pane root;
    private VBox logContent = new VBox();
    private boolean lastAiShotHit;
    private boolean lastPlayerShotHit;

    public BattleScreen(Pane root, int fieldLength, List<Ship> ships, List<Ship> aiShips, List<Tile> tiles) {
        this.fieldLength = fieldLength;
        this.root = root;
        this.tiles = tiles;
        this.playerShips = ships;
        this.aiShips = aiShips;
        getBattleField();
    }

    public void getBattleField() {
        GridPane battleField = new GridPane();
        ScrollPane log = new ScrollPane();
        log.vvalueProperty().bind(logContent.heightProperty());
        log.setContent(logContent);
        log.setLayoutX(fieldLength * 50 + 100);
        log.setLayoutY(fieldLength * 25 + 75);
        log.setPrefSize(fieldLength * 25, fieldLength * 25 - 25);
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
                        String yOutput = numberToLetter(tile.getPosition().getY() + 1, true);
                        String xOutput = Integer.toString(tile.getPosition().getX());
                        Label playerShot = new Label("Player: " + yOutput + "/" + xOutput);
                        logContent.getChildren().add(playerShot);
                        shoot(region, selectedAiShip, true);
                        selectedAiShip = null;
                        if (SettingsController.getDifficulty() == 1) {
                            if (!lastPlayerShotHit) {
                                randomShot();
                                while (lastAiShotHit) {
                                    randomShot();
                                }
                            }
                        } else if (SettingsController.getDifficulty() == 3) {
                            if (!lastPlayerShotHit) {
                                aimBot();
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
    }

    private boolean checkIfShipHit(Shape block, Ship ship) {
        boolean collisionDetected = false;
        if (ship.getShootingHitBox() != block) {
            Shape intersect = Shape.intersect(block, ship.getShootingHitBox());
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                collisionDetected = true;
            }
        }
        return collisionDetected;
    }

    //shoots at a random tile in the battlefield
    private void randomShot() {
        //creates two random numbers between 0 and 9 which will be the shots coordinates
        Position position = getRandomPosition();
        while (shotPositions.contains(position)) {
            position = getRandomPosition();
        }
        shotPositions.add(position);
        for (Tile tile : tiles) {
            if (position.equals(tile.getPosition())) {
                for (Ship playerShip : playerShips) {
                    if (checkIfShipHit(tile.getTile(), playerShip)) {
                        selectedPlayerShip = playerShip;
                    }
                }
                String yOutput = numberToLetter(tile.getPosition().getY() + 1, true);
                String xOutput = Integer.toString(tile.getPosition().getX());
                Label aiShot = new Label("Computer: " + yOutput + "/" + xOutput);
                logContent.getChildren().add(aiShot);
                shoot(tile.getTile(), selectedPlayerShip, false);
                selectedPlayerShip = null;
            }
        }
    }

    //shoots random until it finds a ship and then destroys the ship
    private void aimBot() {
        //creates new random coordinates until he hits a ship
        Position position = getRandomPosition();
        while (shotPositions.contains(position)) {
            position = getRandomPosition();
        }
        while (selectedPlayerShip == null) {
            for (Tile tile : tiles) {
                if (position.equals(tile.getPosition())) {
                    selectedAiTile = tile;
                    for (Ship playerShip : playerShips) {
                        if (checkIfShipHit(tile.getTile(), playerShip)) {
                            selectedPlayerShip = playerShip;
                        }
                    }
                }
            }
            if (selectedPlayerShip == null) {
                position = getRandomPosition();
                while (shotPositions.contains(position)) {
                    position = getRandomPosition();
                }
            }
        }
        shotPositions.add(position);
        String yOutput = numberToLetter(selectedAiTile.getPosition().getY() + 1, true);
        String xOutput = Integer.toString(selectedAiTile.getPosition().getX());
        Label aiShot = new Label("Computer: " + yOutput + "/" + xOutput);
        logContent.getChildren().add(aiShot);
        shoot(selectedAiTile.getTile(), selectedPlayerShip, false);
        selectedPlayerShip = null;
    }

    private void shoot(Rectangle tile, Ship selectedShip, boolean player) {
        Sound.playShot();
        if (selectedShip != null) {
            Sound.playSound("Explosion.mp3");
            tile.setFill(rgb(255, 0, 0, 0.5));
            selectedShip.isHit();
            if (player) {
                lastPlayerShotHit = true;
            } else {
                lastAiShotHit = true;
            }
            if (selectedShip.isDestroyed()) {
                Sound.playSound("Destroyed.mp3");
                if (player) {
                    Label destroyedShip = new Label("You destroyed your opponents ship" + selectedShip.getName() + "!");
                    destroyedShip.setTextFill(GREEN);
                    logContent.getChildren().add(destroyedShip);
                    int aliveAiShips = 0;
                    for (Ship ship : aiShips) {
                        if (!ship.isDestroyed()) {
                            aliveAiShips++;
                        }
                    }
                    if (aliveAiShips == 0) {
                        WinningScreen winningScreen = new WinningScreen();
                        NavController.setRoot(winningScreen.getWinningScreen("player"));
                    }
                    Label shipsToGo = new Label("You need to destroy " + aliveAiShips + " more ships to win.");
                    shipsToGo.setTextFill(GREEN);
                    logContent.getChildren().add(shipsToGo);
                } else {
                    Label aiDestroyedShip = new Label("Your opponent destroyed your ship" + selectedShip.getName() + "!");
                    aiDestroyedShip.setTextFill(RED);
                    logContent.getChildren().add(aiDestroyedShip);
                    int alivePlayerShips = 0;
                    for (Ship ship : playerShips) {
                        if (!ship.isDestroyed()) {
                            alivePlayerShips++;
                        }
                    }
                    if (alivePlayerShips == 0) {
                        WinningScreen winningScreen = new WinningScreen();
                        NavController.setRoot(winningScreen.getWinningScreen("ai"));
                    }
                    Label shipsLeft = new Label("You have " + alivePlayerShips + " ships left.");
                    shipsLeft.setTextFill(RED);
                    logContent.getChildren().add(shipsLeft);
                }
            }
        } else {
            tile.setFill(rgb(200, 200, 200, 0.7));
            lastPlayerShotHit = false;
            lastAiShotHit = false;
        }
    }

    //makes a number from 1 to 26 in the equivalent letter, in capitals or not
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

    private Position getRandomPosition() {
        Random random = new Random();
        int min = 0;
        int max = fieldLength;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        Position position = new Position(x, y);
        return position;
    }
}
