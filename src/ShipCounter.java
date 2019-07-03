public class ShipCounter {
    private static int allShips;
    private static int twoTypes;
    private static int threeTypes;
    private static int fourTypes;
    private static int fiveTypes;

    static int getAllShips() {
        return allShips;
    }
    static int getTwoTypes() {
        return twoTypes;
    }
    static int getThreeTypes() {
        return threeTypes;
    }
    static int getFourTypes() {
        return fourTypes;
    }
    static int getFiveTypes() {
        return fiveTypes;
    }

    static void twoUp(){
        twoTypes++;
        allShips++;
    }
    static void threeUp(){
        threeTypes++;
        allShips++;
    }
    static void fourUp(){
        fourTypes++;
        allShips++;
    }
    static void fiveUp(){
        fiveTypes++;
        allShips++;
    }

    static void twoDown(){
        twoTypes--;
        allShips--;
    }
    static void threeDown(){
        threeTypes--;
        allShips--;
    }
    static void fourDown(){
        fourTypes--;
        allShips--;
    }
    static void fiveDown(){
        fiveTypes--;
        allShips--;
    }
}
