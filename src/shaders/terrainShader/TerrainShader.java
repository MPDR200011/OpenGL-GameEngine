package shaders.terrainShader;

import entities.components.Camera;
import entities.components.Light;
import shaders.ShaderProgram;
import utils.Maths;
import utils.vector.Matrix4f;
import utils.vector.Vector3f;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/terrainShader/terrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/shaders/terrainShader/terrainFragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColourLocation;
    private int shineDamperlocation;
    private int reflectivitylocation;
    private int skyColorLocation;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(projectionMatrixLocation, projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(viewMatrixLocation, viewMatrix);
    }

    public void loadLight(Light light){
        super.loadVector(lightPositionLocation,light.getPosition());
        super.loadVector(lightColourLocation,light.getColour());
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(shineDamperlocation, damper);
        super.loadFloat(reflectivitylocation,reflectivity);
    }

    public void loadSkyColor(float red, float green, float blue){
        super.loadVector(skyColorLocation, new Vector3f(red, green, blue));
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
        lightPositionLocation = super.getUniformLocation("lightPosition");
        lightColourLocation = super.getUniformLocation("lightColour");
        shineDamperlocation = super.getUniformLocation("shineDamper");
        reflectivitylocation = super.getUniformLocation("reflectivity");
        skyColorLocation = super.getUniformLocation("skyColor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }
}
