package input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65536];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key < keys.length && key > -1){
            keys[key] = action != GLFW_RELEASE;
        }
    }

    public static boolean isKeyDown(int keyCode){
        return keys[keyCode];
    }

}
