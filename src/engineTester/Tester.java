package engineTester;

import engineTester.prefab.script.PlayerCamera;
import engineTester.prefab.script.TerrainCenterer;
import entities.*;
import entities.components.*;
import engineTester.prefab.*;
import engineTester.prefab.script.CameraMovement;
import input.KeyboardHandler;
import input.MouseTracker;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL;
import renderEngine.*;
import textures.Texture;
import utils.vector.Vector2f;
import utils.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Tester {
    public static void main(String[] args) {

        Window window = new Window("Tester", 1280, 600);
        window.show();

        List<GameObject> gameObjects = new ArrayList<>();

        //CameraObject cameraObj = new CameraObject(new Vector3f(0,0,10));
        //CameraMovement cameraMovement = (CameraMovement) cameraObj.getComponentByIndex(1);
        GameObject cameraObj = new GameObject(new Vector3f(100, 0, 100), new Vector3f(), 1);
        cameraObj.addComponent(new Camera(cameraObj));
        PlayerCamera cameraMovement = new PlayerCamera(cameraObj);
        cameraObj.addComponent(cameraMovement);

        TerrainObject terrain = new TerrainObject(new Vector3f(0, 0, 0), 800, 512);
        gameObjects.add(terrain);
        Terrain terrComp = terrain.getComponent(Terrain.class);

        //gameObjects.add(new Earth(new Vector3f(0,0,0), new Vector3f(0,0,0), 1, 1, new Vector3f(0,0,0)));

        for (int i = 0; i < 300; i++) {
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            gameObjects.add(new Tree(new Vector3f(x, terrComp.getTerrainHeight(x, z), z),
                    new Vector3f(0, 0, 0), 6));
        }

        for (int i = 0; i < 1000; i++) {
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            gameObjects.add(new Grass(new Vector3f(x, terrComp.getTerrainHeight(x, z), z),
                    new Vector3f(0, 0, 0), 1));
        }

        for (int i = 0; i < 500; i++) {
            float x = (float) Math.random() * 800;
            float z = (float) Math.random() * 800;
            gameObjects.add(new Fern(new Vector3f(x, terrComp.getTerrainHeight(x, z), z),
                    new Vector3f(0, 0, 0), 1));
        }

        GameObject dragon = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), 1);
        RawModel dragonRM = RawModelManager.getRawModel("dragon");
        Texture dragonTex = new Texture("purple", false, false);
        TexturedModel dragonModel = new TexturedModel(dragonRM, dragonTex);
        dragon.addComponent(new Model(dragon, dragonModel));
        gameObjects.add(dragon);

        LightObject light = new LightObject(new Vector3f(0, 100, 0), new Vector3f(1, 1, 1));

        //Loop thingy
        GL.createCapabilities();

        MasterRenderer renderer = new MasterRenderer(window);

        boolean paused = true;
        boolean escapePressed = false;
        while (!glfwWindowShouldClose(window.ID())) {
            glfwPollEvents();
            if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE)) {
                if (!escapePressed) {
                    if (paused) {
                        paused = false;
                        glfwSetInputMode(window.ID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    } else {
                        paused = true;
                        glfwSetInputMode(window.ID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                        Vector2f windowSize = window.getSize();
                        glfwSetCursorPos(window.ID(), windowSize.x / 2, windowSize.y / 2);
                    }
                }
                escapePressed = true;
            } else {
                escapePressed = false;
            }

            MouseTracker.update(window);

            if (!paused) {
                cameraMovement.move(terrComp);
                //cameraMovement.move(gameObjects.get(0).getComponent(Terrain.class));
                for (Script script : Script.scipts) {
                    if (script.isEnabled()) {
                        script.fixedUpdate();
                    }
                }
                for (RigibBody rb : RigibBody.rigidBodies) {
                    rb.update();
                }
            }

            for (GameObject obj : gameObjects) {
                renderer.processEntity(obj);
            }

            for (Terrain terr : Terrain.terrains) {
                renderer.processTerrain(terr);
            }

            renderer.render(light, cameraObj);

            glfwSwapBuffers(window.ID());
        }

        Game.loader.cleanUp();
        renderer.cleanUp();
        window.destroy();

    }

}
