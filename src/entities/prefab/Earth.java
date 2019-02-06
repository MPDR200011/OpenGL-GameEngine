package entities.prefab;

import entities.GameObject;
import entities.components.Model;
import entities.components.RigibBody;
import entities.components.script.Attractor;
import models.RawModel;
import models.TexturedModel;
import renderEngine.RawModelManager;
import renderEngine.TextureManager;
import textures.Texture;
import utils.vector.Vector3f;

public class Earth extends GameObject {

    public Earth(Vector3f position, Vector3f rotation, float scale, float mass, Vector3f initialVelocity){
        super(position, rotation, scale);

        RawModel model = RawModelManager.getRawModel("sphere");
        Texture texture = new Texture("earth", false, false);
        TexturedModel tm = new TexturedModel(model, texture);
        addComponent(new Model(this, tm));

        addComponent(new RigibBody(this, initialVelocity, mass));

        addComponent(new Attractor(this, mass));
    }

}
