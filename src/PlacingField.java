import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.scene.paint.Color.*;

class PlacingField {
    public int shipCounter = 0;
    public VBox settings = new VBox();
    public Ship selectedShip;
    public List<Ship> playerShips = new ArrayList<>();
    private List<Ship> aiShips = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private int placeCounter = 0;
    private int twoCounter = 0;
    private int threeCounter = 0;
    private int fourCounter = 0;
    private int fiveCounter = 0;
    private int currentCounter = 0;
    private int maxSameShips;
    private int fieldLength;

    public PlacingField(int fieldLength, int maxSameShips) {
        this.fieldLength = fieldLength;
        this.maxSameShips = maxSameShips;
    }

    //returns the ship placing field as gridPane
    public GridPane getPlacingField(Group root) {
        GridPane placingField = new GridPane();
        placingField.setLayoutX(50);
        placingField.setLayoutY(50);
        int x = 0;
        int y = 0;
        //adds all 100 tiles to the grid and adds the mouse events
        for (int i = 0; i < Math.pow(fieldLength, 2); i++) {
            Tile tile = new Tile(x, y);
            tiles.add(tile);
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            placingField.setGridLinesVisible(true);
            placingField.getStyleClass().add("placingField");

            //move the ship to the current tile when the mouse enters it
            region.setOnMouseEntered(mouse -> {
                if (selectedShip != null && !selectedShip.isPlaced()) {
                    double newX = tile.getX();
                    double newY = tile.getY();
                    selectedShip.moveShip(newX, newY, fieldLength * 50);
                }
            });

            //places the selected ship if one is selected and only if no other ship is in the way
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (selectedShip != null) {
                        selectedShip.placeHitBox();
                        if (!checkIfPlaceTaken(selectedShip.getShip(), playerShips)) {
                            selectedShip.setPlaced();
                            selectedShip.setPosition();
                        }
                    }
                }
                //changes the direction of the ship on right click
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (selectedShip != null && !selectedShip.isPlaced()) {
                        selectedShip.changeDirection();
                    } else {
                        int tileX = (int) Math.round(tile.getX());
                        int tileY = (int) Math.round(tile.getY());
                        boolean shipGetsDeleted = false;
                        //checks if any of the playerShips is there
                        for (Ship ship : playerShips) {
                            if (ship.boatIsThere(tileX, tileY)) {
                                shipGetsDeleted = true;
                                selectedShip = ship;
                            }
                        }
                        //if there was a ship found shipsGetsDeleted will be true and that ship gets removed
                        if (shipGetsDeleted) {
                            deleteShip(root, selectedShip);
                        }
                    }
                }
            });
            placingField.add(region, x, y);
            x++;
            if (x > fieldLength - 1) {
                x = 0;
                y++;
            }
        }
        return placingField;
    }

    //returns the settings where the player can customize his ships
    public void createShipSettings(Group root) {
        settings = new VBox();
        VBox boatName = new VBox();
        VBox boatLength = new VBox();
        VBox boatColor = new VBox();
        Label lblBoatName = new Label("Boat Name:");
        lblBoatName.setTextFill(WHITE);
        Label lblBoatLength = new Label("Boat Length:");
        lblBoatLength.setTextFill(WHITE);
        Label lblBoatColor = new Label("Boat Color:");
        lblBoatColor.setTextFill(WHITE);
        Label emptyWarning = new Label("enter a boat length!");
        emptyWarning.setTextFill(RED);
        Label lengthWarning = new Label("length must be between 2 and 5!");
        lengthWarning.setTextFill(RED);
        Label maxShipsWarning = new Label("max amount of playerShips reached!");
        maxShipsWarning.setTextFill(RED);
        Label shipNotPlacedWarning = new Label("place the current ship first!");
        shipNotPlacedWarning.setTextFill(RED);
        Label maxSameShipsWarning = new Label("you placed all of those ships, try another length!");
        maxSameShipsWarning.setTextFill(RED);
        Label shipsAlreadyPlacedWarning = new Label("you already placed some ships!");
        shipsAlreadyPlacedWarning.setTextFill(RED);
        ColorPicker cpBoatColor = new ColorPicker(BLUE);
        cpBoatColor.setPrefWidth(255);
        TextField tfBoatName = new TextField();
        tfBoatName.setMinWidth(255);
        TextField tfBoatLength = new TextField();
        tfBoatLength.setMinWidth(255);
        tfBoatLength.setText("3");
        //makes sure only numbers are in the boatLength field
        tfBoatLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfBoatLength.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        Button create = new Button("create");
        create.setPrefWidth(255);
        //creates a boat when create button is pressed
        create.setOnAction(event -> {
            //checks if the length field isn't empty
            if (!tfBoatLength.getText().equals("")) {
                boatLength.getChildren().remove(emptyWarning);
                int intBoatLength = Integer.parseInt(tfBoatLength.getText());

                //checks if boat length is between 2 and 5
                if (intBoatLength < 2 || intBoatLength > 5) {
                    if (!boatLength.getChildren().contains(lengthWarning)) {
                        boatLength.getChildren().add(lengthWarning);
                    }
                } else {
                    //sets currentCounter to the amount of playerShips of the selected type
                    switch (intBoatLength) {
                        case 2:
                            currentCounter = twoCounter - 1;
                            break;
                        case 3:
                            currentCounter = threeCounter;
                            break;
                        case 4:
                            currentCounter = fourCounter;
                            break;
                        case 5:
                            currentCounter = fiveCounter + 1;
                            break;
                    }
                    boatLength.getChildren().remove(lengthWarning);
                    //checks if this is the first ship to be created
                    if (shipCounter != 0) {
                        //checks if the user tries to make more than the maximum of the same ship
                        if (currentCounter < maxSameShips) {
                            settings.getChildren().remove(maxSameShipsWarning);
                            //checks if the selected boat is already placed
                            if (selectedShip.isPlaced()) {
                                settings.getChildren().remove(shipNotPlacedWarning);
                                //checks if the maximum amount of playerShips is reached
                                settings.getChildren().remove(maxShipsWarning);
                                createShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                                createAndPlaceAiShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                                //counts how many playerShips of each type there are
                                switch (intBoatLength) {
                                    case 2:
                                        twoCounter++;
                                        break;
                                    case 3:
                                        threeCounter++;
                                        break;
                                    case 4:
                                        fourCounter++;
                                        break;
                                    case 5:
                                        fiveCounter++;
                                        break;
                                }
                            } else {
                                if (!settings.getChildren().contains(shipNotPlacedWarning)) {
                                    settings.getChildren().add(shipNotPlacedWarning);
                                }
                            }
                        } else {
                            if (!settings.getChildren().contains(maxSameShipsWarning)) {
                                settings.getChildren().add(maxSameShipsWarning);
                            }
                        }
                    } else {
                        createShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                        createAndPlaceAiShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                        //counts how many playerShips of each type there are
                        switch (intBoatLength) {
                            case 2:
                                twoCounter++;
                                break;
                            case 3:
                                threeCounter++;
                                break;
                            case 4:
                                fourCounter++;
                                break;
                            case 5:
                                fiveCounter++;
                                break;
                        }
                    }

                }
            } else {
                if (!boatLength.getChildren().contains(emptyWarning)) {
                    boatLength.getChildren().add(emptyWarning);
                }
            }
        });

        Button generateShips = new Button("generate Ships");
        generateShips.setPrefWidth(255);
        generateShips.setOnAction(event -> {
            if (playerShips.isEmpty()) {
                settings.getChildren().remove(shipsAlreadyPlacedWarning);
                generateShips(root, cpBoatColor.getValue());
            } else {
                if (!settings.getChildren().contains(shipsAlreadyPlacedWarning)) {
                    settings.getChildren().add(shipsAlreadyPlacedWarning);
                }
                int count = playerShips.size();
                for(int i = 0; i < count; i++){
                    deleteShip(root, playerShips.get(0));
                }

            }
        });

        boatName.getChildren().addAll(lblBoatName, tfBoatName);
        boatLength.getChildren().addAll(lblBoatLength, tfBoatLength);
        boatColor.getChildren().addAll(lblBoatColor, cpBoatColor);
        settings.getChildren().addAll(boatName, boatLength, boatColor, create, generateShips);
        settings.setSpacing(10);
        settings.setLayoutX(fieldLength * 50 + 100);
        settings.setLayoutY(50);
    }

    //creates an AI ship and uses the placeShip function to place it
    private void createAndPlaceAiShip(Group root, String name, int length, Paint paint) {
        //create ship with the same name, length and color as the users ship, but with isAI set true
        Ship ship = new Ship(name, length, RED);
        placeShip(ship, aiShips);
        root.getChildren().add(ship.getShip());
        ship.getShip().toBack();
    }

    //creates a ship and adds it to the given group and selects the boat
    private void createShip(Group root, String name, int length, Paint paint) {
        Ship ship = new Ship(name, length, paint);
        playerShips.add(ship);
        root.getChildren().add(ship.getShip());
        ship.getShip().toBack();
        selectedShip = ship;
        shipCounter++;
    }

    //checks, if a thing touches a ship
    private boolean checkIfPlaceTaken(Shape block, List<Ship> ships) {
        boolean collisionDetected = false;
        for (Ship ship : ships) {
            if (ship.getHitBox() != block && ship != selectedShip) {
                Shape intersect = Shape.intersect(block, ship.getHitBox());
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

    //places ai ship at a random position in the field
    private void placeShip(Ship ship, List<Ship> ships) {
        //ship gets placed the first time and then replaced if the position isn't correct
        //creates two random numbers between 0 and 9 which will be the playerShips coordinates
        Random random = new Random();
        int min = 0;
        int max = fieldLength-1;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        //creates a random number between 0 and 1 which will decide if the ship lies horizontal or vertical
        int direction = random.nextInt(2);
        if (direction == 1) {
            ship.changeDirection();
        }
        //move the ship to the coordinates
        ship.moveShip(x, y, fieldLength * 50);
        for (; checkIfPlaceTaken(selectedShip.getShip(), ships); ) {
            //if the placing of the current failed more than 100000 times it replaces all ships
            if (placeCounter < 10000) {
                //creates two random numbers between 0 and 9 which will be the playerShips coordinates
                x = random.nextInt(max - min) + min;
                y = random.nextInt(max - min) + min;
                //creates a random number between 0 and 1 which will decide if the ship lies horizontal or vertical
                direction = random.nextInt(2);
                if (direction == 1) {
                    ship.changeDirection();
                }
                //move the ship to the coordinates
                ship.moveShip(x, y, fieldLength * 50);
                placeCounter++;
            } else {
                System.out.println("error");
                /*
                System.out.println("replace all");
                int i = 0;
                placeShip(ship);
                for (Ship ship1 : aiShips) {
                    for (; checkIfPlaceTaken(ship1.getShip(), aiShips); ) {
                        placeShip(ship1);
                    }
                    i++;
                    System.out.println("i = " + i);
                }
                */
                break;
            }
        }
        placeCounter = 0;
        selectedShip.placeHitBox();
        selectedShip.setPlaced();
        selectedShip.setPosition();
        ships.add(ship);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Ship> getAiShips() {
        return aiShips;
    }

    public List<Ship> getPlayerShips() {
        return playerShips;
    }

    //generates all ships and positions them
    private void generateShips(Group root, Paint paint) {
        int length;
        for (int i = 0; i < maxSameShips * 4; i++) {
            if (i < maxSameShips + 1) {
                length = 2;
            } else if (i < maxSameShips * 2 + 1) {
                length = 3;
            } else if (i < maxSameShips * 3 + 1) {
                length = 4;
            } else {
                length = 5;
            }
            createShip(root, "", length, paint);
            playerShips.remove(selectedShip);
            //createAndPlaceAiShip(root, "", length, paint);
            placeShip(selectedShip, playerShips);
        }
    }

    //deletes a ship and it's AI brother
    public void deleteShip(Group root, Ship ship) {
        int removingShip = playerShips.indexOf(ship);
        root.getChildren().remove(ship.getShip());
        //aiShips.remove(removingShip);
        playerShips.remove(ship);
        shipCounter--;
    }
}
