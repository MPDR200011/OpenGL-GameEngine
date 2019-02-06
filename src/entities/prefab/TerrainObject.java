package entities.prefab;

import entities.GameObject;
import entities.components.Terrain;
import entities.components.TerrainMeshGenerator;
import models.RawModel;
import models.TexturedModel;
import textures.Texture;
import utils.vector.Vector3f;

public class TerrainObject extends GameObject {

    private TerrainMeshGenerator generator;

    public TerrainObject(Vector3f position, float terrainSize, int terrainVertexCount) {
        super(position, new Vector3f(0,0,0), 1.0F);

        this.generator = new TerrainMeshGenerator(this, terrainSize, terrainVertexCount);
        addComponent(generator);

        Texture texture = new Texture("grass", false, false);
        RawModel terrainRM = generator.generate();
        addComponent(new Terrain(this, new TexturedModel(terrainRM, texture)));

    }
}
