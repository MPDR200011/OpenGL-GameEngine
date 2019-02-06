package entities.prefab;

import entities.GameObject;
import entities.components.Model;
import models.TexturedModel;
import renderEngine.RawModelManager;
import renderEngine.TextureManager;
import textures.Texture;
import utils.vector.Vector3f;

public class Fern extends GameObject {

    public Fern(Vector3f position, Vector3f rotation, float scale){
        super(position, rotation, scale);

        Texture tx = new Texture("fern", true, true);

        addComponent(new Model(this, new TexturedModel(RawModelManager.getRawModel("fern"),
                tx)));
    }

}
