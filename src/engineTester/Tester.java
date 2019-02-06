package engineTester;

import entities.*;
import entities.components.RigibBody;
import entities.components.Terrain;
import entities.prefab.*;
import entities.components.script.CameraMovement;
import entities.components.script.Script;
import input.KeyboardHandler;
import input.MouseTracker;
import org.lwjgl.opengl.GL;
import renderEngine.*;
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

        //gameObjects.add(new Earth(new Vector3f(0,0,0), new Vector3f(0,0,0), 1, 1, new Vector3f(0,0,0)));
        gameObjects.add(new TerrainObject(new Vector3f(0,0,0), 800, 128));

//        for (int i = 0; i < 300; i++){
//            gameObjects.add(new Tree(new Vector3f((float) Math.random() * 800,0,(float) Math.random()*800),
//                    new Vector3f(0,0,0), 6));
//        }
//
//        for (int i = 0; i < 1000; i++) {
//            gameObjects.add(new Grass(new Vector3f((float) Math.random() * 800,0,(float) Math.random()*800),
//                    new Vector3f(0,0,0), 1));
//        }
//
//        for (int i = 0; i < 500; i++) {
//            gameObjects.add(new Fern(new Vector3f((float) Math.random() * 800,0,(float) Math.random()*800),
//                    new Vector3f(0,0,0), 1));
//        }

        LightObject light = new LightObject(new Vector3f(0,100,0), new Vector3f(1,1,1));

        CameraObject cameraObj = new CameraObject(new Vector3f(0,10,0));
        CameraMovement cameraMovement = (CameraMovement) cameraObj.getComponentByIndex(1);

        //Loop thingy
        GL.createCapabilities();

        MasterRenderer renderer = new MasterRenderer(window);

        boolean paused = true;
        boolean escapePressed = false;
        while (!glfwWindowShouldClose(window.ID())) {
            glfwPollEvents();
            if (KeyboardHandler.isKeyDown(GLFW_KEY_ESCAPE)){
                if (!escapePressed) {
                    if (paused) {
                        paused = false;
                        glfwSetInputMode(window.ID(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                    } else {
                        paused = true;
                        glfwSetInputMode(window.ID(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                        Vector2f windowSize = window.getSize();
                        glfwSetCursorPos(window.ID(), windowSize.x/2, windowSize.y/2);
                    }
                }
                escapePressed = true;
            } else {
                escapePressed = false;
            }

            MouseTracker.update(window);

            if (!paused){
                cameraMovement.move();
                for (Script script: Script.scipts){
                    if (script.enabled){
                        script.fixedUpdate();
                    }
                }
                for (RigibBody rb: RigibBody.rigidBodies){
                    rb.update();
                }
            }

            for (GameObject obj: gameObjects){
                renderer.processEntity(obj);
            }

            for (Terrain terr: Terrain.terrains){
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
