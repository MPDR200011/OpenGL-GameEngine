package entities.components;

import entities.GameObject;
import utils.vector.Vector3f;

public class Light extends Component {

    private Vector3f position;
    private Vector3f colour;

    public Light(GameObject attachedObject, Vector3f position, Vector3f color){
        super(attachedObject);
        this.position = position;
        this.colour = color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }
}
