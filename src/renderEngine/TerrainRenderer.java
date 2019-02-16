package renderEngine;

import entities.components.Model;
import entities.components.Terrain;
import entities.components.Transform;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.terrainShader.TerrainShader;
import textures.Texture;
import utils.Maths;
import utils.vector.Matrix4f;

import java.util.List;

public class TerrainRenderer {

    private Matrix4f projectionMatrix;
    private TerrainShader shader;

    public TerrainRenderer(Window window, TerrainShader shader){
        this.shader = shader;

        projectionMatrix = window.projection.getProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains){
        for (Terrain model: terrains){
            prepareTerrain(model.getModel());

            loadModelMatrix(model.transform);
            //Rendering
            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

            unbindTexturedModel();
        }
    }

    private void prepareTerrain(TexturedModel model){
        RawModel rawModel = model.getRawModel();

        //Bind the model's VAO and activate its vertex array
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        //Shine Modifiers
        Texture tx = model.getTexture();
        shader.loadShineVariables(tx.getShineDamper(),tx.getReflectivity());

        //Binding Textures
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

    }

    private void unbindTexturedModel(){
        //Deactivate the vertex array and unbind the VAO
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Transform transform){
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
