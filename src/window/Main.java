package window;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> GameWindow.launchNewWindow(500, 500));
    }
}
