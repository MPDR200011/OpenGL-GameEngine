package engineTester.prefab;

import entities.GameObject;
import entities.components.Terrain;
import models.RawModel;
import models.TexturedModel;
import textures.Texture;
import utils.vector.Vector3f;

public class TerrainObject extends GameObject {

    public TerrainObject(Vector3f position, float terrainSize, int terrainVertexCount) {
        super(position, new Vector3f(0,0,0), 1.0F);

        Texture texture = new Texture("grass", false, false);
        addComponent(new Terrain(this, texture, terrainSize, terrainVertexCount));
    }
}
