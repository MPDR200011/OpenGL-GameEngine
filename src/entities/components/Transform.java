package entities.components;

import entities.GameObject;
import entities.components.Component;
import utils.vector.Vector3f;

public class Transform extends Component {

    public Vector3f position;
    public Vector3f velocity;
    public Vector3f rotation;
    public float scale;

    public Transform(GameObject attachedGameObject, Vector3f position, Vector3f rotation, float scale) {
        super(attachedGameObject);
        this.position = position;
        this.velocity = new Vector3f();
        this.rotation = rotation;
        this.scale = scale;
    }

    public void increaseRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

}
