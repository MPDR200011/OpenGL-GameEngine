package engineTester.prefab;

import entities.GameObject;
import entities.components.Model;
import models.TexturedModel;
import renderEngine.RawModelManager;
import textures.Texture;
import utils.vector.Vector3f;

public class Grass extends GameObject {

    public Grass(Vector3f position, Vector3f rotation, float scale){
        super(position, rotation, scale);

        Texture tx = new Texture("grassTexture", true, true);

        addComponent(new Model(this, new TexturedModel(RawModelManager.getRawModel("grassModel"),
                tx)));
    }

}
