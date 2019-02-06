package renderEngine;

import entities.components.Transform;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.staticShader.StaticShader;
import textures.Texture;
import utils.Maths;
import utils.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;


public class StandartRenderer {

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public StandartRenderer(Window window, StaticShader shader){
        this.shader = shader;

        projectionMatrix = window.projection.getProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Transform>> entities){
        for (TexturedModel model: entities.keySet()){
            prepareTexturedModel(model);

            List<Transform> batch = entities.get(model);

            for (Transform transform: batch){
                prepareInstance(transform);
                //Rendering
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();

        //Bind the model's VAO and activate its vertex array
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        //Shine Modifiers
        Texture tx = model.getTexture();
        if (tx.isHasTransparency()){
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightBool(tx.isUseFakeLighting());
        shader.loadShineVariables(tx.getShineDamper(),tx.getReflectivity());

        //Binding Textures
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

    }

    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        //Deactivate the vertex array and unbind the VAO
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Transform transform){
        //Load transformation to the shader
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(transform.position,
                transform.rotation.x, transform.rotation.y, transform.rotation.z, transform.scale);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public void updateProjection() {
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

}
