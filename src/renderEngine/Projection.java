package renderEngine;

import utils.vector.Matrix4f;
import utils.vector.Vector2f;

public class Projection {

    private float FOV = 70;
    private float NEAR_PLANE = 0.1f;
    private float FAR_PLANE = 1000.0f;

    private int windowWidth;
    private int windowHeigth;

    private Matrix4f projectionMatrix;

    public Projection(Window window) {
        Vector2f size = window.getSize();
        this.windowWidth = (int) size.x;
        this.windowHeigth = (int) size.y;

        projectionMatrix = new Matrix4f();
        createProjectionMatrix();
    }

    public void recreateProjectionMatrix() {
        createProjectionMatrix();
    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) windowWidth / (float) windowHeigth;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

    public void setFOV(float newFOV) {
        this.FOV = newFOV;
    }

}
