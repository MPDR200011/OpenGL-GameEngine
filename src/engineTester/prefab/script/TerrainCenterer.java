package engineTester.prefab.script;

import entities.GameObject;
import entities.components.Script;
import entities.components.Terrain;
import entities.components.Transform;
import utils.vector.Vector2f;

public class TerrainCenterer extends Script {

    Terrain objectTerain;
    Transform playerTransform;

    public TerrainCenterer(GameObject gameObject, Transform playerTransform) {
        super(gameObject);
        this.objectTerain = gameObject.getComponent(Terrain.class);
        this.playerTransform = playerTransform;
    }

    @Override
    public void fixedUpdate() {
        objectTerain.gameObject.setPosition(
                playerTransform.position.x - objectTerain.SIZE/2,
                objectTerain.transform.position.y,
                playerTransform.position.z - objectTerain.SIZE/2);

        objectTerain.setOffset(new Vector2f(
                playerTransform.position.x - objectTerain.SIZE/2,
                playerTransform.position.y - objectTerain.SIZE/2
        ));
        objectTerain.generate();
    }
}
