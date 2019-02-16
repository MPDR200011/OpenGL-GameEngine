package engineTester.prefab;

import entities.GameObject;
import entities.components.Camera;
import engineTester.prefab.script.CameraMovement;
import utils.vector.Vector3f;

public class CameraObject extends GameObject {

    public CameraObject(Vector3f position) {
        super(position, new Vector3f(0,0,0), 1.0F);
        addComponent(new Camera(this));
        addComponent(new CameraMovement(this));
    }

}
