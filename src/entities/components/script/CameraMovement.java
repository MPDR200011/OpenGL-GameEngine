package entities.components.script;

import entities.GameObject;
import entities.components.Camera;
import input.KeyboardHandler;
import input.MouseTracker;
import utils.vector.Vector2f;
import utils.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class CameraMovement extends Script {

    private Camera camera;
    private float cameraSpeed = 0.5f;
    private float mouseSensitivityMultiplier = 2.0f;

    public CameraMovement(GameObject attachedObject) {
        super(attachedObject);
        this.camera = gameObject.getComponent(Camera.class);
    }

    public void move(){
        Vector2f delta = MouseTracker.getDelta();

        camera.pitch += 0.05 * delta.y * mouseSensitivityMultiplier;
        camera.yaw += 0.05 * delta.x * mouseSensitivityMultiplier;

        if (camera.pitch > 90){
            camera.pitch = 90;
        } else if (camera.pitch < -90){
            camera.pitch = -90;
        }

        float q = (float) Math.toRadians(camera.yaw+90);
        float phi = (float) Math.toRadians(90-camera.pitch);



        float phi2Sin =  (float) Math.sin(phi);
        Vector3f heading = new Vector3f(cameraSpeed * (float) Math.cos(q) * phi2Sin,
                cameraSpeed * (float) Math.cos(phi),
                cameraSpeed * (float) Math.sin(q) * phi2Sin);

        Vector3f side = new Vector3f();
        Vector3f.cross(heading, new Vector3f(0,1,0), side);

        if (KeyboardHandler.isKeyDown(GLFW_KEY_W)){
            Vector3f.sub(camera.position,heading,camera.position);
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            Vector3f.add(camera.position,heading,camera.position);
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_A)){
            Vector3f.add(camera.position,side,camera.position);
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_D)){
            Vector3f.sub(camera.position,side,camera.position);
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)){
            camera.position.y += cameraSpeed;
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_LEFT_SHIFT)){
            camera.position.y -= cameraSpeed;
        }

    }

}
