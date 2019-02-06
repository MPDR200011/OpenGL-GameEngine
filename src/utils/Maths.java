package utils;

import entities.components.Camera;
import utils.vector.Matrix4f;
import utils.vector.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rotX, float rotY, float rotZ, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        matrix.translate(translation);
        matrix.rotate((float) Math.toRadians(rotX), new Vector3f(1,0,0));
        matrix.rotate((float) Math.toRadians(rotY), new Vector3f(0,1,0));
        matrix.rotate((float) Math.toRadians(rotZ), new Vector3f(0,0,1));
        matrix.scale(new Vector3f(scale,scale,scale));
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1));
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        viewMatrix.translate(negativeCameraPos);
        return viewMatrix;
    }

}
