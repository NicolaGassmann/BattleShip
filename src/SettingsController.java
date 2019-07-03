public class SettingsController {
    private static boolean longField;

    public static boolean isLongField() {
        return longField;
    }

    public static void setLongField(boolean longField) {
        SettingsController.longField = longField;
    }
}
