package renderEngine;

import entities.*;
import entities.components.*;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import shaders.staticShader.StaticShader;
import shaders.terrainShader.TerrainShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private TerrainShader terrainShader = new TerrainShader();
    private StandartRenderer standartRenderer;
    private TerrainRenderer terrainRenderer;

    private static final float SKY_RED = 0.5f;
    private static final float SKY_GREEN = 0.5f;
    private static final float SKY_BLUE = 0.5f;

    private Map<TexturedModel, List<Transform>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(Window window){

        enableCulling();

        standartRenderer = new StandartRenderer(window, shader);
        terrainRenderer = new TerrainRenderer(window, terrainShader);
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(GameObject sun, GameObject camera){
        Camera view = camera.getComponent(Camera.class);
        Light light = sun.getComponent(Light.class);
        prepare();

        shader.start();
        shader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(view);
        standartRenderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(view);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        entities.clear();
        terrains.clear();
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
    }

    public void processEntity(GameObject obj){
        Model objModel = obj.getComponent(Model.class);
        if (objModel != null){
            TexturedModel entityModel = objModel.getModel();
            List<Transform> batch = entities.get(entityModel);
            if (batch != null){
                batch.add(objModel.transform);
            } else {
                List<Transform> newBatch = new ArrayList<>();
                newBatch.add(objModel.transform);
                entities.put(entityModel,newBatch);
            }
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public void updateProjection() {
        standartRenderer.updateProjection();
        terrainRenderer.updateProjection();
    }

}
