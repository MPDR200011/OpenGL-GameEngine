package engineTester.prefab;

import entities.GameObject;
import entities.components.Light;
import utils.vector.Vector3f;

public class LightObject extends GameObject {

    public LightObject(Vector3f position, Vector3f color) {
        super(position, new Vector3f(0,0,0), 1.0F);
        addComponent(new Light(this, position, color));
    }

}
