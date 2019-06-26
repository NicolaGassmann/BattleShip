import javafx.beans.property.SimpleListProperty;

public class Player {

    private SimpleListProperty ownShots;
    private SimpleListProperty enemyShots;
    private Position position = new Position();

    public Position nextShot() {
        int x = position.getX();
        int y = position.getY();

        return position;
    }

    public void addShip(Ship ship) {

    }

    public void removeShip(Ship ship) {

    }

}
