package input;

import org.lwjgl.BufferUtils;
import renderEngine.Window;
import utils.vector.Vector2f;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class MouseTracker {

    private static Vector2f prevPos = new Vector2f();
    private static Vector2f currPos = new Vector2f();
    private static Vector2f delta = new Vector2f();

    public static void update(Window window) {
        prevPos = currPos;
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window.ID(),x,y);
        float xpos = (float) x.get();
        float ypos = (float) y.get();
        currPos = new Vector2f(xpos,ypos);
        Vector2f.sub(currPos,prevPos,delta);
    }

    public static Vector2f getDelta() {
        return delta;
    }
}
