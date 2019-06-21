import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.*;

public class Battlefield {
    private Ship selectedBoat;
    private int shipCounter = 0;
    private int maxBoats = 10;
    private List<Ship> ships = new ArrayList<>();
    private List<Rectangle> hitboxes = new ArrayList<>();

    public Scene getGameScreen() {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600/*, new ImagePattern(new Image("img/water.jpg"))*/);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getBattlefield(scene, root);
        return scene;
    }

    private boolean checkIfPlaceTaken(Shape block, List<Rectangle> blocks) {
        boolean collisionDetected = false;
        for (Rectangle hitbox : blocks) {
            if (hitbox != block) {
                Shape intersect = Shape.intersect(block, hitbox);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

    public GridPane getBattlefield(Scene scene, Group root) {
        GridPane battlefield = new GridPane();
        root.getChildren().add(battlefield);
        createShip(root, "cruiser", 3, RED);
        int x = 0;
        int y = 0;
        for (int i = 0; i < 100; i++) {
            Tile tile = new Tile(x, y);
            Rectangle region = tile.getTile();
            region.setFill(rgb(0, 0, 0, 0));
            battlefield.setGridLinesVisible(true);
            battlefield.getStyleClass().add("battlefield");

            region.setOnMouseEntered(mouse -> {
                double newX = tile.getX();
                double newY = tile.getY();
                selectedBoat.moveShip(newX, newY);
                selectedBoat.getPosition();
            });
            region.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!checkIfPlaceTaken(selectedBoat.getShip(), hitboxes) && shipCounter <= 10) {
                        switch (shipCounter) {
                            case 1:
                                selectedBoat.placeHitbox(root);
                                createShip(root, "submarine", 4, BLUE);
                                break;
                            case 2:
                                selectedBoat.placeHitbox(root);
                                createShip(root, "flattop", 5, GREEN);
                                break;
                            case 3:
                                selectedBoat.placeHitbox(root);
                                selectedBoat = new Ship("end", 0, WHITE);
                                shipCounter++;
                                break;
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
        scene.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                selectedBoat.changeDirection();
            }
        });
        return battlefield;
    }

    public void createShip(Group root, String name, int length, Paint paint) {
        Ship ship = new Ship(name, length, paint);
        ships.add(ship);
        hitboxes.add(ship.getHitbox());
        ship.getShip().setVisible(true);
        root.getChildren().add(ship.getShip());
        ship.getShip().toBack();
        selectedBoat = ship;
        shipCounter++;
    }
}