import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javafx.scene.paint.Color.*;

class PlacingScreen {
    public VBox settings = new VBox();
    public Ship selectedShip;
    public List<Ship> playerShips = new ArrayList<>();
    private List<Ship> aiShips = new ArrayList<>();
    private List<Tile> tiles = new ArrayList<>();
    private int maxSameShips;
    private int fieldLength;
    private Pane root;
    private boolean errorInPlacing = false;
    private Button cancel = new Button("cancel");

    public PlacingScreen(Pane root, int fieldLength, int maxSameShips) {
        this.fieldLength = fieldLength;
        this.maxSameShips = maxSameShips;
        this.root = root;
    }

    //returns the ship placing field as gridPane
    public GridPane getPlacingField() {
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
                        selectedShip.placePlacingHitBox();
                        selectedShip.placeShootingHitBox();
                        if (!checkIfPlaceTaken(selectedShip.getShip(), playerShips)) {
                            selectedShip.setPlaced();
                            selectedShip.setPosition();
                            cancel.setDisable(true);
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
                            deleteShip(selectedShip);
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
    public void createShipSettings() {
        VBox boatName = new VBox();
        VBox boatLength = new VBox();
        VBox boatColor = new VBox();
        Label lblBoatName = new Label("Boat Name:");
        lblBoatName.setTextFill(WHITE);
        Label lblBoatLength = new Label("Boat Length:");
        lblBoatLength.setTextFill(WHITE);
        Label lblBoatColor = new Label("Boat Color:");
        lblBoatColor.setTextFill(WHITE);
        Label emptyWarning = new Label("Enter a boat length!");
        emptyWarning.setTextFill(RED);
        Label lengthWarning = new Label("Length must be between 2 and 5!");
        lengthWarning.setTextFill(RED);
        Label maxShipsWarning = new Label("Max amount of playerShips reached!");
        maxShipsWarning.setTextFill(RED);
        Label shipNotPlacedWarning = new Label("Place the current ship first!");
        shipNotPlacedWarning.setTextFill(RED);
        Label maxSameShipsWarning = new Label("You placed all of those ships, try another length!");
        maxSameShipsWarning.setTextFill(RED);
        Label shipsAlreadyPlacedWarning = new Label("You already placed some ships!");
        shipsAlreadyPlacedWarning.setTextFill(RED);
        ColorPicker cpBoatColor = new ColorPicker(BLUE);
        cpBoatColor.setPrefWidth(255);
        TextField tfBoatName = new TextField();
        tfBoatName.setPrefWidth(255);
        TextField tfBoatLength = new TextField();
        tfBoatLength.setPrefWidth(255);
        tfBoatLength.setText("3");
        //makes sure only numbers are in the boatLength field
        tfBoatLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfBoatLength.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cancel.setPrefWidth(255);
        cancel.setDisable(true);
        cancel.setOnAction(event -> {
            if (selectedShip != null && !selectedShip.isPlaced()) {
                deleteShip(selectedShip);
                selectedShip.setPlaced();
                cancel.setDisable(true);
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
                    int currentCounter = 0;
                    switch (intBoatLength) {
                        case 2:
                            currentCounter = ShipCounter.getTwoTypes()- 1;
                            break;
                        case 3:
                            currentCounter = ShipCounter.getThreeTypes();
                            break;
                        case 4:
                            currentCounter = ShipCounter.getFourTypes();
                            break;
                        case 5:
                            currentCounter = ShipCounter.getFiveTypes() + 1;
                            break;
                    }
                    boatLength.getChildren().remove(lengthWarning);
                    //checks if this is the first ship to be created
                    if (ShipCounter.getAllShips() > 0) {
                        //checks if the user tries to make more than the maximum of the same ship
                        if (currentCounter < maxSameShips) {
                            settings.getChildren().remove(maxSameShipsWarning);
                            //checks if the selected boat is already placed
                            if (selectedShip.isPlaced()) {
                                settings.getChildren().remove(shipNotPlacedWarning);
                                //checks if the maximum amount of playerShips is reached
                                settings.getChildren().remove(maxShipsWarning);
                                createShip(tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                                createAndPlaceAiShip(tfBoatName.getText(), intBoatLength);
                                cancel.setDisable(false);
                                //counts how many playerShips of each type there are
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
                        createShip(tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                        createAndPlaceAiShip(tfBoatName.getText(), intBoatLength);
                        cancel.setDisable(false);
                        //counts how many playerShips of each type there are
                    }

                }
            } else {
                if (!boatLength.getChildren().contains(emptyWarning)) {
                    boatLength.getChildren().add(emptyWarning);
                }
            }
        });

        ButtonType buttonTypeYes = new ButtonType("yes");
        ButtonType buttonTypeNo = new ButtonType("no");
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Your ships will be deleted!");
        dialog.setHeaderText("Your about to generate all your ships which includes deleting the currently placed ones!");
        dialog.setContentText("Are you sure you want to generate random positions for your ships? (you can still change them later)");
        dialog.getDialogPane().getButtonTypes().add(buttonTypeYes);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeNo);
        Button generateShips = new Button("generate Ships");
        generateShips.setPrefWidth(255);
        generateShips.setOnAction(event -> {
            if (playerShips.isEmpty()) {
                generateShips(cpBoatColor.getValue());
                while (errorInPlacing) {
                    System.out.println("replace all ships");
                    generateShips(cpBoatColor.getValue());
                }
            } else {
                dialog.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeYes) {
                        int count = playerShips.size();
                        for (int i = 0; i < count; i++) {
                            deleteShip(playerShips.get(0));
                        }
                        generateShips(cpBoatColor.getValue());
                        while (errorInPlacing) {
                            System.out.println("replace all ships");
                            int count1 = playerShips.size();
                            for (int i = 0; i < count1; i++) {
                                deleteShip(playerShips.get(0));
                            }
                            generateShips(cpBoatColor.getValue());
                        }
                    }
                });
            }
        });

        boatName.getChildren().addAll(lblBoatName, tfBoatName);
        boatLength.getChildren().addAll(lblBoatLength, tfBoatLength);
        boatColor.getChildren().addAll(lblBoatColor, cpBoatColor);
        settings.getChildren().addAll(boatName, boatLength, boatColor, create, generateShips, cancel);
        settings.setSpacing(10);
        settings.setLayoutX(fieldLength * 50 + 100);
        settings.setLayoutY(50);
    }

    //creates an AI ship and uses the placeShip function to place it
    private void createAndPlaceAiShip(String name, int length) {
        //create ship with the same name, length and color as the users ship, but with isAI set true
        Ship ship = new Ship(name, length, RED);
        aiShips.add(ship);
        ship.getShip().setVisible(false);
        root.getChildren().add(ship.getShip());
        root.getChildren().add(ship.getPlacingHitBox());
        ship.getPlacingHitBox().setVisible(false);
        root.getChildren().add(ship.getShootingHitBox());
        ship.getShootingHitBox().setVisible(false);
        placeShip(ship, aiShips);
        while (errorInPlacing) {
            System.out.println("replace ai ships");
            replaceAiShips();
        }
    }

    //creates a ship and adds it to the given group and selects the boat
    private void createShip(String name, int length, Paint paint) {
        Ship ship = new Ship(name, length, paint);
        playerShips.add(ship);
        root.getChildren().add(ship.getShip());
        ship.getShip().toBack();
        selectedShip = ship;
        root.getChildren().add(ship.getPlacingHitBox());
        ship.getPlacingHitBox().setVisible(false);
        root.getChildren().add(ship.getShootingHitBox());
        ship.getShootingHitBox().setVisible(false);
        countShip(length);
    }

    //checks, if a thing touches a ship
    private boolean checkIfPlaceTaken(Shape block, List<Ship> ships) {
        boolean collisionDetected = false;
        for (Ship ship : ships) {
            if (ship.getPlacingHitBox() != block && ship != selectedShip) {
                Shape intersect = Shape.intersect(block, ship.getPlacingHitBox());
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
        int max = fieldLength;
        int x = random.nextInt(max - min) + min;
        int y = random.nextInt(max - min) + min;
        //creates a random number between 0 and 1 which will decide if the ship lies horizontal or vertical
        int direction = random.nextInt(2);
        if (direction == 1) {
            ship.changeDirection();
        }
        //move the ship to the coordinates
        ship.moveShip(x, y, fieldLength * 50);
        int placeCounter = 0;
        for (; checkIfPlaceTaken(ship.getShip(), ships); ) {
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
                break;
            }
        }
        if (placeCounter >= 10000) {
            System.out.println("error");
            errorInPlacing = true;
        } else {
            errorInPlacing = false;
        }
        ship.placePlacingHitBox();
        ship.placeShootingHitBox();
        ship.setPlaced();
        ship.setPosition();
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
    private void generateShips(Paint paint) {
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
            createShip("", length, paint);
            createAndPlaceAiShip("", length);
            placeShip(selectedShip, playerShips);
        }
    }

    //deletes and recreates all ai ships in a different place
    private void replaceAiShips() {
        clearAiShips();

        for (int i = 0; i < ShipCounter.getTwoTypes(); i++) {
            createAndPlaceAiShip("", 2);
        }
        for (int i = 0; i < ShipCounter.getThreeTypes(); i++) {
            createAndPlaceAiShip("", 3);
        }
        for (int i = 0; i < ShipCounter.getFourTypes(); i++) {
            createAndPlaceAiShip("", 4);
        }
        for (int i = 0; i < ShipCounter.getFiveTypes(); i++) {
            createAndPlaceAiShip("", 5);
        }
    }

    //deletes a ship and it's AI brother
    private void deleteShip(Ship ship) {
        int removingShip = playerShips.indexOf(ship);
        Ship aiShip = aiShips.get(removingShip);
        root.getChildren().remove(aiShip.getShip());
        root.getChildren().remove(ship.getShip());
        root.getChildren().remove(ship.getPlacingHitBox());
        root.getChildren().remove(ship.getShootingHitBox());
        root.getChildren().remove(aiShip.getPlacingHitBox());
        root.getChildren().remove(aiShip.getShootingHitBox());
        aiShips.remove(removingShip);
        playerShips.remove(ship);
        switch (ship.getLength()) {
            case 2:
                ShipCounter.twoDown();
                break;
            case 3:
                ShipCounter.threeDown();
                break;
            case 4:
                ShipCounter.fourDown();
                break;
            case 5:
                ShipCounter.fiveDown();
                break;
        }
    }

    private void clearAiShips(){
        for(Ship aiShip:aiShips){
            root.getChildren().remove(aiShip.getShootingHitBox());
            root.getChildren().remove(aiShip.getPlacingHitBox());
            root.getChildren().remove(aiShip.getShip());
        }
        aiShips.clear();
    }

    private void countShip(int length) {
        switch (length) {
            case 2:
                ShipCounter.twoUp();
                break;
            case 3:
                ShipCounter.threeUp();
                break;
            case 4:
                ShipCounter.fourUp();
                break;
            case 5:
                ShipCounter.fiveUp();
                break;
        }
    }
}
