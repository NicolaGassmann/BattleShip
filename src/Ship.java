import javafx.event.Event;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ship {
    private Rectangle body;
    private Position position;
    private int length;
    private String direction;

    public Ship(int length, Paint fill) {
        this.length = length;
        body = new Rectangle(length * 50, 50, fill);
        this.direction = "horizontal";
    }

    public void moveShip(double x, double y) {
        getShip().setX(x);
        getShip().setY(y);
    }

    public void positionShip(Position position) {
        this.position = position;
    }

    public void changeDirection() {
        if (this.direction.equals("horizontal")) {
            this.direction = "vertical";
        }
        if (this.direction.equals("vertical")) {
            this.direction = "horizontal";
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public Rectangle getShip() {
        return this.body;
    }

    public int getLength() {
        return this.length;
    }
}
