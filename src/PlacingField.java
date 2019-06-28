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
    public Ship selectedBoat;
    public List<Ship> ships = new ArrayList<>();
    private List<Ship> aiShips = new ArrayList<>();
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
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            placingField.setGridLinesVisible(true);
            placingField.getStyleClass().add("placingField");

            //move the ship to the current tile when the mouse enters it
            region.setOnMouseEntered(mouse -> {
                if (selectedBoat != null && !selectedBoat.isPlaced()) {
                    double newX = tile.getX();
                    double newY = tile.getY();
                    selectedBoat.moveShip(newX, newY, fieldLength * 50);
                }
            });

            //places the selected ship if one is selected and only if no other ship is in the way
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (selectedBoat != null) {
                        selectedBoat.placeHitBox();
                        if (!checkIfPlaceTaken(selectedBoat.getShip(), ships)) {
                            selectedBoat.setPlaced();
                            selectedBoat.setPosition();
                        }
                    }
                }
                //changes the direction of the ship on right click
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (selectedBoat != null && !selectedBoat.isPlaced()) {
                        selectedBoat.changeDirection();
                    } else {
                        int tileX = (int) Math.round(tile.getX());
                        int tileY = (int) Math.round(tile.getY());
                        boolean shipGetsDeleted = false;
                        //checks if any of the ships is there
                        for (Ship ship : ships) {
                            if (ship.boatIsThere(tileX, tileY)) {
                                shipGetsDeleted = true;
                                selectedBoat = ship;
                            }
                        }
                        //if there was a ship found shipsGetsDeleted will be true and that ship gets removed
                        if (shipGetsDeleted) {
                            int removingShip = ships.indexOf(selectedBoat);
                            root.getChildren().remove(selectedBoat.getShip());
                            aiShips.remove(removingShip);
                            ships.remove(selectedBoat);
                            shipCounter--;
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
        Label maxShipsWarning = new Label("max amount of ships reached!");
        maxShipsWarning.setTextFill(RED);
        Label shipNotPlacedWarning = new Label("place the current ship first!");
        shipNotPlacedWarning.setTextFill(RED);
        Label maxSameShipsWarning = new Label("only place " + maxSameShips + " of the same ship +1 two sized ship!");
        maxSameShipsWarning.setTextFill(RED);
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
                    //sets currentCounter to the amount of ships of the selected type
                    switch (intBoatLength) {
                        case 2:
                            currentCounter = twoCounter-1;
                            break;
                        case 3:
                            currentCounter = threeCounter;
                            break;
                        case 4:
                            currentCounter = fourCounter;
                            break;
                        case 5:
                            currentCounter = fiveCounter;
                            break;
                    }
                    boatLength.getChildren().remove(lengthWarning);
                    //checks if this is the first ship to be created
                    if (shipCounter != 0) {
                        //checks if the user tries to make more than the maximum of the same ship
                        if (currentCounter < maxSameShips) {
                            settings.getChildren().remove(maxSameShipsWarning);
                            //checks if the selected boat is already placed
                            if (selectedBoat.isPlaced()) {
                                settings.getChildren().remove(shipNotPlacedWarning);
                                //checks if the maximum amount of ships is reached
                                settings.getChildren().remove(maxShipsWarning);
                                createShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                                createAndPlaceAiShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                                //counts how many ships of each type there are
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
                        //counts how many ships of each type there are
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
        boatName.getChildren().addAll(lblBoatName, tfBoatName);
        boatLength.getChildren().addAll(lblBoatLength, tfBoatLength);
        boatColor.getChildren().addAll(lblBoatColor, cpBoatColor);
        settings.getChildren().addAll(boatName, boatLength, boatColor, create);
        settings.setSpacing(10);
        settings.setLayoutX(fieldLength * 50 + 100);
        settings.setLayoutY(50);
    }

    //creates an AI ship and uses the placeAiShip function to place it
    private void createAndPlaceAiShip(Group root, String name, int length, Paint paint) {
        //create ship with the same name, length and color as the users ship, but with isAI set true
        Ship ship = new Ship(name, length, RED);
        ship.setAiShip(true);
        //ship gets placed the first time and then replaced if the position isn't correct
        placeAiShip(ship);
        for (; checkIfPlaceTaken(ship.getShip(), aiShips); ) {
            //if the placing of the current failed more than 100000 times it replaces all ships
            if (placeCounter < 100000) {
                placeAiShip(ship);
                placeCounter++;
            } else {
                System.out.println("replace all");
                int i = 0;
                placeAiShip(ship);
                for (Ship ship1 : aiShips) {
                    for (; checkIfPlaceTaken(ship1.getShip(), aiShips); ) {
                        placeAiShip(ship1);
                    }
                    i++;
                    System.out.println("i = " + i);
                }
            }
        }
        placeCounter = 0;
        ship.setPlaced();
        ship.setPosition();
        aiShips.add(ship);
        ship.placeHitBox();
        ship.setPlaced();
        ship.setPosition();
    }

    //creates a ship and adds it to the given group and selects the boat
    private void createShip(Group root, String name, int length, Paint paint) {
        Ship ship = new Ship(name, length, paint);
        ships.add(ship);
        ship.getShip().setVisible(true);
        root.getChildren().add(ship.getShip());
        ship.getShip().toBack();
        selectedBoat = ship;
        shipCounter++;
    }

    //checks, if a thing touches a ship
    private boolean checkIfPlaceTaken(Shape block, List<Ship> ships) {
        boolean collisionDetected = false;
        for (Ship ship : ships) {
            if (ship.getHitBox() != block && ship != selectedBoat) {
                Shape intersect = Shape.intersect(block, ship.getHitBox());
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

    //places ai ship at a random position in the field
    private void placeAiShip(Ship ship) {
        //creates two random numbers between 0 and 9 which will be the ships coordinates
        Random random = new Random();
        int min = 0;
        int max = fieldLength - 1;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        //creates a random number between 0 and 1 which will decide if the ship lies horizontal or vertical
        int direction = random.nextInt(2);
        if (direction == 1) {
            ship.changeDirection();
        }
        //move the ship to the coordinates
        ship.moveShip(x, y, fieldLength * 50);
    }
}
