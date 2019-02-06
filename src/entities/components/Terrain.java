package entities.components;

import entities.GameObject;
import models.TexturedModel;

import java.util.ArrayList;
import java.util.List;

//TODO make terrain into its own independent type with a model and renderer of its own
public class Terrain extends Model {

    public static List<Terrain> terrains = new ArrayList<>();

    public Terrain(GameObject attachedObject, TexturedModel texturedModel) {
        super(attachedObject, texturedModel);
        terrains.add(this);
    }
}
