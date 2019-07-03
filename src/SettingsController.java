public class SettingsController {
    private static boolean longField = false;
    private static int difficulty = 3;

    public static boolean isLongField() {
        return longField;
    }

    public static void setLongField(boolean longField) {
        SettingsController.longField = longField;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(int difficulty) {
        SettingsController.difficulty = difficulty;
    }
}
