import javafx.event.Event;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ship {
    private Rectangle body;
    private String position;
    private int length;
    private String direction;

    public Ship(int length, Paint fill) {
        this.length = length;
        body = new Rectangle(length * 51, 50, fill);
        this.direction = "horizontal";
    }

    public void moveShip(double x, double y) {

        //set position of ship if the ship is vertical
        if(direction.equals("vertical") ){
            double xShit = 25.5*(length-1);
            getShip().setX(x-xShit);
            if(length%2 == 0){
                getShip().setY(y+25.5);
            }else{
                getShip().setY(y);
            }


            //make sure ship stays in battlefield
            if(getShip().getY() < xShit){
                getShip().setY(xShit);
            }
            if(getShip().getY() > 510-length*51+xShit){
                getShip().setY(510-length*51+xShit);
            }

        //set position of ship if ship is horizontal
        }else {
            if(length > 3) {
                getShip().setX(x-102);
            }else{
                getShip().setX(x-51);
            }
            getShip().setY(y+0.4);

            //make sure ship stays in battlefield
            if(getShip().getX() > 510-length*51){
                getShip().setX(510-length*51);
            }
            if(getShip().getX() < 0){
                getShip().setX(0);
            }
        }
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

    //returns coordinates from start point to end point of the ship as string (y/x to y/x)
    public String getPosition() {
        //get absolute coordinates of top left point of the ship
        double absoluteX = getShip().getX();
        double absoluteY = getShip().getY();
        //calculate absolute coordinates to position coordinates (0 to 10 and a to j)
        int posX = (int) Math.round(absoluteX/51);
        int posY = (int) Math.round(absoluteY/51);
        if(direction.equals("vertical")) {
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
        switch (posY){
            case 0: StringPosY = "a";break;
            case 1: StringPosY = "b";break;
            case 2: StringPosY = "c";break;
            case 3: StringPosY = "d";break;
            case 4: StringPosY = "e";break;
            case 5: StringPosY = "f";break;
            case 6: StringPosY = "g";break;
            case 7: StringPosY = "h";break;
            case 8: StringPosY = "i";break;
            case 9: StringPosY = "j";break;
        }
        //combine both strings
        String startPosition = StringPosY + "/" + StringPosX;

        //get coordinates of end point of the ship
        int endX;
        int endY;
        if(direction.equals("horizontal")) {
            endX = posX + length-1;
            endY = posY;
        }else{
            endX = posX;
            endY = posY + length-1;
        }
        //do the same as before with end coordinates
        String StringEndX = Integer.toString(endX);
        String StringEndY = "";
        switch (endY){
            case 0: StringEndY = "a";break;
            case 1: StringEndY = "b";break;
            case 2: StringEndY = "c";break;
            case 3: StringEndY = "d";break;
            case 4: StringEndY = "e";break;
            case 5: StringEndY = "f";break;
            case 6: StringEndY = "g";break;
            case 7: StringEndY = "h";break;
            case 8: StringEndY = "i";break;
            case 9: StringEndY = "j";break;
        }
        String endPosition = StringEndY + "/" + StringEndX;
        //make one string to output the position
        position = startPosition + " to " + endPosition;
        System.out.println("position = " + position);
        return position;
    }


    public Rectangle getShip() {
        return this.body;
    }

    public int getLength() {
        return this.length;
    }
}
