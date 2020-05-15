package engineTester.prefab.script;

import entities.GameObject;
import entities.components.Camera;
import entities.components.Script;
import entities.components.Terrain;
import input.KeyboardHandler;
import input.MouseTracker;
import utils.vector.Vector2f;
import utils.vector.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerCamera extends Script {

    private Camera camera;
    private float cameraSpeed = 6f;
    private float mouseSensitivityMultiplier = 2.0f;

    private boolean inAir = false;
    private boolean moved = false;

    private float playerHeight = 8;

    public PlayerCamera(GameObject attachedObject) {
        super(attachedObject);
        this.camera = gameObject.getComponent(Camera.class);
    }

    public void move(Terrain terr) {
        moved = false;
        Vector2f delta = MouseTracker.getDelta();


        camera.pitch += 0.05 * delta.y * mouseSensitivityMultiplier;
        camera.yaw += 0.05 * delta.x * mouseSensitivityMultiplier;

        if (camera.pitch > 90) {
            camera.pitch = 90;
        } else if (camera.pitch < -90) {
            camera.pitch = -90;
        }

        float q = (float) Math.toRadians(camera.yaw + 90);
        float phi = (float) Math.toRadians(90 - camera.pitch);


        float phi2Sin = (float) Math.sin(phi);
        Vector3f heading = new Vector3f(cameraSpeed * (float) Math.cos(q) * phi2Sin,
                0,
                cameraSpeed * (float) Math.sin(q) * phi2Sin);

        Vector3f side = new Vector3f();
        Vector3f.cross(heading, new Vector3f(0, 1, 0), side);

        if (KeyboardHandler.isKeyDown(GLFW_KEY_W)) {
            Vector3f.sub(camera.position, heading, camera.position);
            moved = true;
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_S)) {
            Vector3f.add(camera.position, heading, camera.position);
            moved = true;
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_A)) {
            Vector3f.add(camera.position, side, camera.position);
            moved = true;
        }
        if (KeyboardHandler.isKeyDown(GLFW_KEY_D)) {
            Vector3f.sub(camera.position, side, camera.position);
            moved = true;
        }

        Vector3f oldPos = transform.position;
        transform.velocity.y -= 2;
        transform.position.y += transform.velocity.y * 0.016;
        float terrainHeight = terr.getTerrainHeight(transform.position.x, transform.position.z);
        if (transform.position.y - playerHeight < terrainHeight) {
            transform.position.y = terrainHeight + playerHeight;
            if (inAir) {
                transform.velocity.y = 0;
            } else {
                if (moved) {
                    Vector3f.sub(transform.position, oldPos, transform.velocity);
                    transform.velocity = new Vector3f(
                            transform.velocity.x / 0.016f,
                            transform.velocity.y / 0.016f,
                            transform.velocity.z / 0.016f);
                } else {
                    transform.velocity.y = 0;
                }
            }
            inAir = false;
        } else {
            inAir = true;
        }
    }

}
