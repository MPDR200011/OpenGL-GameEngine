package entities.components;

import entities.GameObject;

public abstract class Component {

    public GameObject gameObject;
    public Transform transform;

    public Component(GameObject gameObject){
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
    }

}
