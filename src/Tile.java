import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;

public class Tile {
    private boolean selected;
    private Rectangle tile;
    private Position position = new Position();

    public Tile(int x, int y){
        tile = new Rectangle(50, 50);
        position.setX(x);
        position.setY(y);
    }
    public Rectangle getTile(){
        return tile;
    }
    public double getX(){
        return position.getX();
    }
    public double getY(){
        return position.getY();
    }

    public Position getPosition() {
        return position;
    }
}
