package entities.components;

import entities.GameObject;
import entities.components.Component;
import models.TexturedModel;

public class Model extends Component {

    private TexturedModel model;

    public Model(GameObject gameObject, TexturedModel model) {
        super(gameObject);
        this.model = model;
    }

    public TexturedModel getModel() {
        return model;
    }
}

