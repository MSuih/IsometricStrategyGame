package window;

import com.sun.glass.events.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyBindings {
    public static enum KeyAction {
        CAMERA_UP, CAMERA_DOWN, CAMERA_LEFT, CAMERA_RIGHT;
    }

    private final Map<Integer, KeyAction> binds = new HashMap<>();

    public KeyBindings() {}

    static KeyBindings getDefaultBinds() {
        KeyBindings kb = new KeyBindings();
        kb.setBinding(KeyEvent.VK_W, KeyAction.CAMERA_UP);
        kb.setBinding(KeyEvent.VK_S, KeyAction.CAMERA_DOWN);
        kb.setBinding(KeyEvent.VK_A, KeyAction.CAMERA_LEFT);
        kb.setBinding(KeyEvent.VK_D, KeyAction.CAMERA_RIGHT);
        return kb;
    }
    public void setBinding(int keycode, KeyAction action) {
        binds.put(keycode, action);
    }

    public void unbind(int keycode) {
        binds.remove(keycode);
    }

    public KeyAction getActionFor(int keycode) {
        return binds.get(keycode);
    }
}
