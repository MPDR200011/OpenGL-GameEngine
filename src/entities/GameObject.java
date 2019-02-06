package entities;

import entities.components.Component;
import entities.components.Transform;
import utils.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private Transform transform;
    private List<Component> components = new ArrayList<>();

    public GameObject(Vector3f position, Vector3f rotation, float scale) {
        this.transform = new Transform(this, position, rotation, scale);
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    public Component getComponentByIndex(int index) {
        return components.get(index);
    }

    public <T extends Component> T getComponent(Class<T> type){

        for (Component component: components){
            if (type.isAssignableFrom(component.getClass())){
                return type.cast(component);
            }
        }

        return null;
    }

    public Transform getTransform(){
        return this.transform;
    }

}
