package entities.components;

import entities.GameObject;
import utils.vector.Vector3f;

public class Camera extends Component {

    public Vector3f position;
    public float pitch;
    public float yaw;
    public float roll;

    public Camera(GameObject object){
        super(object);
        position = object.getTransform().position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
