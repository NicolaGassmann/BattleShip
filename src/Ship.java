import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ship {
    private String name;
    private Rectangle body;
    private Rectangle hitBox;
    private String position;
    private int length;
    private String direction;
    private boolean isPlaced;
    private int hits;
    private boolean isDestroyed;

    Ship(String name, int length, Paint fill) {
        this.length = length;
        body = new Rectangle(length * 50, 48, fill);
        body.relocate(50, 51);
        hitBox = new Rectangle(body.getWidth() + 50, body.getHeight() + 50);
        hitBox.relocate(25, 25);
        isPlaced = false;
        isDestroyed = false;
        this.direction = "horizontal";
        if(!name.equals("")) {
            this.name = " " + name;
        }else{
            this.name = "";
        }
    }

    //moves the ship to the coordinates needed when given in x 0 to 9 and y 0 to 9
    void moveShip(double x, double y, double gridSize) {
        x = x * 50;
        y = y * 50;
        //set position of ship if the ship is vertical
        if (direction.equals("vertical")) {
            double i = 25 * (length - 1);
            body.setX(x - i);
            if (length % 2 == 0) {
                body.setY(y + 25);
            } else {
                body.setY(y);
            }

            //make sure ship stays in battlefield
            if (body.getY() < i) {
                body.setY(i);
            }
            if (body.getY() > gridSize - length * 50 + i) {
                body.setY(gridSize - length * 50 + i);
            }

            //set position of ship if ship is horizontal
        } else {
            if (length > 3) {
                body.setX(x - 100);
            } else {
                body.setX(x - 50);
            }
            body.setY(y + 0.4);

            //make sure ship stays in battlefield
            if (body.getX() > gridSize - length * 50) {
                body.setX(gridSize - length * 50);
            }
            if (body.getX() < 0) {
                body.setX(0);
            }
        }
    }

    //change the direction of the boat and hitBox from horizontal to vertical and vice versa
    void changeDirection() {
        if (this.direction.equals("horizontal")) {
            this.direction = "vertical";
            getShip().setRotate(90);
            hitBox.setRotate(90);
        } else if (this.direction.equals("vertical")) {
            this.direction = "horizontal";
            getShip().setRotate(0);
            hitBox.setRotate(0);
        }

    }

    //returns coordinates from start point to end point of the ship as string (x/y to x/y)
    void setPosition() {
        //get absolute coordinates of top left point of the ship
        double absoluteX = getShip().getX();
        double absoluteY = getShip().getY();
        //calculate absolute coordinates to position coordinates (0 to 9 and a to j)
        int posX = (int) Math.round(absoluteX / 50);
        int posY = (int) Math.round(absoluteY / 50);
        if (direction.equals("vertical")) {
            posX = posX + 1;
            posY = posY - 2;
            if (length < 3) {
                posX = posX - 1;
                posY = posY + 1;
            }
            if (length > 4) {
                posX = posX + 1;
            }
        }
        //make coordinates to String
        String StringPosX = Integer.toString(posX);
        String StringPosY = Integer.toString(posY);

        //combine both strings
        String startPosition = StringPosX + "/" + StringPosY;

        //get coordinates of end point of the ship
        int endX;
        int endY;
        if (direction.equals("horizontal")) {
            endX = posX + length - 1;
            endY = posY;
        } else {
            endX = posX;
            endY = posY + length - 1;
        }
        //do the same as before with end coordinates
        String StringEndX = Integer.toString(endX);
        String StringEndY = Integer.toString(endY);

        String endPosition = StringEndX + "/" + StringEndY;
        //make one string to output the position
        position = startPosition + " to " + endPosition;
    }

    //checks if a ship is under the given tile
    boolean boatIsThere(int tileX, int tileY) {
        String[] coords = position.split(" to ");
        String[] startCoords = coords[0].split("/");
        String[] endCoords = coords[1].split("/");
        int startX = Integer.parseInt(startCoords[0]);
        int startyY = Integer.parseInt(startCoords[1]);
        int endX = Integer.parseInt(endCoords[0]);
        int endY = Integer.parseInt(endCoords[1]);
        return between(tileX, startX, endX) && between(tileY, startyY, endY);
    }

    //places the hitBox under the boat
    void placeHitBox() {
        hitBox.setX(getShip().getX());
        hitBox.setY(getShip().getY());
    }

    Rectangle getShip() {
        return this.body;
    }

    public String getName() {
        return name;
    }

    Rectangle getHitBox() {
        return hitBox;
    }

    boolean isPlaced() {
        return isPlaced;
    }

    void setPlaced() {
        isPlaced = true;
    }

    //returns true if the first value is equal or between the other values
    private boolean between(int value, int minValue, int maxValue) {
        return value >= minValue && value <= maxValue;
    }

    //counts up in the hitCounter and checks if the ship is destroyed
    public void isHit() {
        hits++;
        if(hits >= length){
            isDestroyed = true;
        }
    }

    public boolean isDestroyed(){
        return isDestroyed;
    }

    public int getLength() {
        return length;
    }
}
