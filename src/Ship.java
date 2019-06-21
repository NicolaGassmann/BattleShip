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
        body = new Rectangle(length * 51, 50, fill);
        this.direction = "horizontal";
    }

    public void moveShip(double x, double y) {
        if(direction.equals("vertical") ){
            getShip().setX(x-76.5);
            getShip().setY(y-25.2);
            if(getShip().getY() < 76.5){
                getShip().setY(76.5);
            }
            if(getShip().getY() > 510-length*51+76.5){
                getShip().setY(510-length*51+76.5);
            }
        }else {
            getShip().setX(x-51);
            getShip().setY(y+0.4);
            if(getShip().getX() > 510-length*51){
                getShip().setX(510-length*51);
            }
            if(getShip().getX() < 0){
                getShip().setX(0);
            }
        }
    }

    public void positionShip(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public void changeDirection() {
        if (this.direction.equals("horizontal")) {
            this.direction = "vertical";
            getShip().setRotate(90);
        }else if (this.direction.equals("vertical")) {
            this.direction = "horizontal";
            getShip().setRotate(0);
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
