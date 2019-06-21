import javafx.scene.shape.Rectangle;

public class Tile {
    private boolean selected;
    public Rectangle tile;

    public Tile(){
        tile = new Rectangle(50, 50);
    }
    public Rectangle getTile(){
        return tile;
    }
}
