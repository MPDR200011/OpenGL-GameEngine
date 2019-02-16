package entities.components;

import entities.components.Component;
import entities.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Script extends Component {

    public boolean enabled = true;

    public static List<Script> scipts = new ArrayList<>();

    public Script(GameObject gameObject){
        super(gameObject);
        scipts.add(this);
    }

    public void fixedUpdate(){}

    public void update(){}

}
