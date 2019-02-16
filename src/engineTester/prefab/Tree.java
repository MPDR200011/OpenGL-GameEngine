package engineTester.prefab;

import entities.GameObject;
import entities.components.Model;
import models.TexturedModel;
import renderEngine.RawModelManager;
import textures.Texture;
import utils.vector.Vector3f;

public class Tree extends GameObject {

    public Tree(Vector3f position, Vector3f rotation, float scale){
        super(position, rotation, scale);

        Texture treeTexture = new Texture("tree", false, false);

        TexturedModel treeModel = new TexturedModel(RawModelManager.getRawModel("tree"), treeTexture);

        addComponent(new Model(this, treeModel));
    }

}
