import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

class Battlefield {
    private Ship selectedBoat;
    private int shipCounter = 0;
    private int maxShips = 5;
    private List<Ship> ships = new ArrayList<>();

    //returns the ship placing screen
    Scene getPlacingScreen() {
        Group root = new Group();
        Scene scene = new Scene(root, 850, 600, new ImagePattern(new Image("img/water.jpg")));
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        root.getChildren().add(getPlacingField(root));
        root.getChildren().add(getShipSettings(root));

        return scene;
    }

    //returns the ship placing field as gridPane
    private GridPane getPlacingField(Group root) {
        GridPane battlefield = new GridPane();
        battlefield.setLayoutX(50);
        battlefield.setLayoutY(50);
        int x = 0;
        int y = 0;
        //adds all 100 tiles to the grid and adds the mouse events
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile(x, y);
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            battlefield.setGridLinesVisible(true);
            battlefield.getStyleClass().add("battlefield");

            //move the ship to the current tile when the mouse enters it
            region.setOnMouseEntered(mouse -> {
                double newX = tile.getX();
                double newY = tile.getY();
                if(selectedBoat != null && !selectedBoat.isPlaced()) {
                    selectedBoat.moveShip(newX, newY);
                }
            });

            //places the selected ship if one is selected and only if no other ship is in the way
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if(selectedBoat != null) {
                        selectedBoat.placeHitBox();
                        if (!checkIfPlaceTaken(selectedBoat.getShip(), ships)) {
                            selectedBoat.setPlaced();
                            selectedBoat.setPosition();
                        }
                    }
                }
                //changes the direction of the ship on right click
                if (event.getButton() == MouseButton.SECONDARY) {
                    if(selectedBoat != null && !selectedBoat.isPlaced()) {
                        selectedBoat.changeDirection();
                    }else{
                        int tileX = (int) Math.round(tile.getX());
                        int tileY = (int) Math.round(tile.getY());
                        boolean shipGetsDeleted = false;
                        //checks if any of the ships is there
                        for(Ship ship:ships){
                            if(ship.boatIsThere(tileX, tileY)){
                                shipGetsDeleted = true;
                                selectedBoat = ship;
                            }
                        }
                        //if there was a ship found shipsGetsDeleted will be true and that ship gets removed
                        if(shipGetsDeleted) {
                            ships.remove(selectedBoat);
                            root.getChildren().remove(selectedBoat.getShip());
                            shipCounter--;
                        }
                    }
                }
            });
            battlefield.add(region, x, y);
            x++;
            if (x > 9) {
                x = 0;
                y++;
            }
        }
        return battlefield;
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

    //returns the settings where the player can customize his ships
    private VBox getShipSettings(Group root) {
        VBox settings = new VBox();
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
        ColorPicker cpBoatColor = new ColorPicker(BLUE);
        TextField tfBoatName = new TextField();
        tfBoatName.setMinWidth(200);
        TextField tfBoatLength = new TextField();
        tfBoatLength.setMinWidth(200);
        //makes sure only numbers are in the boatLength field
        tfBoatLength.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfBoatLength.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        Button create = new Button("create");
        //creates a boat when create button is pressed
        create.setOnAction(event->{
            //checks if the length field isn't empty
            if(!tfBoatLength.getText().equals("")) {
                boatLength.getChildren().remove(emptyWarning);
                int intBoatLength = Integer.parseInt(tfBoatLength.getText());
                //checks if boat length is between 2 and 5
                if(intBoatLength < 2 || intBoatLength > 5){
                    if(!boatLength.getChildren().contains(lengthWarning)) {
                        boatLength.getChildren().add(lengthWarning);
                    }
                }else {
                    boatLength.getChildren().remove(lengthWarning);
                    //checks if this is the first ship to be created
                    if (shipCounter != 0) {
                        //checks if the selected boat is already placed
                        if (selectedBoat.isPlaced()) {
                            settings.getChildren().remove(shipNotPlacedWarning);
                            //checks if the maximum amount of ships is reached
                            if (shipCounter < maxShips) {
                                settings.getChildren().remove(maxShipsWarning);
                                createShip(root, tfBoatName.getText(), intBoatLength, cpBoatColor.getValue());
                            } else {
                                if(!settings.getChildren().contains(maxShipsWarning)){
                                    settings.getChildren().add(maxShipsWarning);
                                }
                            }
                        }else{
                            if(!settings.getChildren().contains(shipNotPlacedWarning)) {
                                settings.getChildren().add(shipNotPlacedWarning);
                            }
                        }
                    } else {
                        createShip(root, tfBoatName.getText(), Integer.parseInt(tfBoatLength.getText()), cpBoatColor.getValue());
                    }
                }
            }else {
                if(!boatLength.getChildren().contains(emptyWarning)) {
                    boatLength.getChildren().add(emptyWarning);
                }
            }
        });
        boatName.getChildren().addAll(lblBoatName, tfBoatName);
        boatLength.getChildren().addAll(lblBoatLength, tfBoatLength);
        boatColor.getChildren().addAll(lblBoatColor, cpBoatColor);
        settings.getChildren().addAll(boatName, boatLength, boatColor, create);
        settings.setSpacing(10);
        settings.setLayoutX(600);
        settings.setLayoutY(50);
        return settings;
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
}
