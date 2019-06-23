import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ship {
    private String name;
    private Rectangle body;
    private Rectangle hitbox;
    private String position;
    private int length;
    private String direction;
    private boolean isPlaced;

    public Ship(String name, int length, Paint fill) {
        this.length = length;
        body = new Rectangle(length * 50, 50, fill);
        body.relocate(50, 50);
        hitbox = new Rectangle(body.getWidth() + 50, body.getHeight() + 50);
        hitbox.relocate(25, 25);
        isPlaced = false;
        this.direction = "horizontal";
        this.name = name;
    }

    //moves the ship to the coordinates needed when given in x 0 to 9 and y 0 to 9
    public void moveShip(double x, double y) {
        x = x * 50;
        y = y * 50;
        //set position of ship if the ship is vertical
        if (direction.equals("vertical")) {
            double xShit = 25 * (length - 1);
            body.setX(x - xShit);
            if (length % 2 == 0) {
                body.setY(y + 25);
            } else {
                body.setY(y);
            }

            //make sure ship stays in battlefield
            if (body.getY() < xShit) {
                body.setY(xShit);
            }
            if (body.getY() > 500 - length * 50 + xShit) {
                body.setY(500 - length * 50 + xShit);
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
            if (body.getX() > 500 - length * 50) {
                body.setX(500 - length * 50);
            }
            if (body.getX() < 0) {
                body.setX(0);
            }
        }
    }

    //change the direction of the boat and hitbox from horizontal to vertical and vice versa
    public void changeDirection() {
        if (this.direction.equals("horizontal")) {
            this.direction = "vertical";
            getShip().setRotate(90);
            hitbox.setRotate(90);
        } else if (this.direction.equals("vertical")) {
            this.direction = "horizontal";
            getShip().setRotate(0);
            hitbox.setRotate(0);
        }

    }

    //returns coordinates from start point to end point of the ship as string (y/x to y/x)
    public String getPosition() {
        //get absolute coordinates of top left point of the ship
        double absoluteX = getShip().getX();
        double absoluteY = getShip().getY();
        //calculate absolute coordinates to position coordinates (0 to 10 and a to j)
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
        String StringPosY = "";
        switch (posY) {
            case 0:
                StringPosY = "a";
                break;
            case 1:
                StringPosY = "b";
                break;
            case 2:
                StringPosY = "c";
                break;
            case 3:
                StringPosY = "d";
                break;
            case 4:
                StringPosY = "e";
                break;
            case 5:
                StringPosY = "f";
                break;
            case 6:
                StringPosY = "g";
                break;
            case 7:
                StringPosY = "h";
                break;
            case 8:
                StringPosY = "i";
                break;
            case 9:
                StringPosY = "j";
                break;
        }
        //combine both strings
        String startPosition = StringPosY + "/" + StringPosX;

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
        String StringEndY = "";
        switch (endY) {
            case 0:
                StringEndY = "a";
                break;
            case 1:
                StringEndY = "b";
                break;
            case 2:
                StringEndY = "c";
                break;
            case 3:
                StringEndY = "d";
                break;
            case 4:
                StringEndY = "e";
                break;
            case 5:
                StringEndY = "f";
                break;
            case 6:
                StringEndY = "g";
                break;
            case 7:
                StringEndY = "h";
                break;
            case 8:
                StringEndY = "i";
                break;
            case 9:
                StringEndY = "j";
                break;
        }
        String endPosition = StringEndY + "/" + StringEndX;
        //make one string to output the position
        position = startPosition + " to " + endPosition;
        return position;
    }

    //places the hitbox under the boat
    public void placeHitbox() {
        hitbox.setX(getShip().getX());
        hitbox.setY(getShip().getY());
    }

    public Rectangle getShip() {
        return this.body;
    }

    public int getLength() {
        return this.length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }
}
