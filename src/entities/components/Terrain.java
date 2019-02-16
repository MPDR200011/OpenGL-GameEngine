package entities.components;

import renderEngine.Game;
import entities.GameObject;
import models.TexturedModel;
import models.RawModel;
import textures.Texture;
import utils.Maths;
import utils.OpenSimplexNoise;
import utils.vector.Vector2f;
import utils.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

//TODO make terrain into its own independent type with a model and renderer of its own
public class Terrain extends Component {

    public static List<Terrain> terrains = new ArrayList<>();

    private TexturedModel model;

    //Properties
    private float SIZE;
    private int VERTEX_COUNT;
    private int count;
    private Vector3f[][] vertices;
    private Vector3f[][] normals;
    private Vector2f[][] textureCoords;
    private int[] indices;

    //Noise Properties
    private OpenSimplexNoise noise;
    private float MAX_HEIGHT = 10;
    private float MIN_HEIGHT = -10;
    private float WAVELENGTH = 8;
    private float xOffset;
    private float zOffset;
    private float xStretch = 1.5f;
    private float zStretch = 1.5f;

    public Terrain(GameObject attachedObject, Texture texture, float terrainSize, int terrainVertexCount) {
        super(attachedObject);

        noise = new OpenSimplexNoise();
        this.SIZE = terrainSize;
        this.VERTEX_COUNT = terrainVertexCount;
        this.count = VERTEX_COUNT * VERTEX_COUNT;
        this.vertices = new Vector3f[VERTEX_COUNT][VERTEX_COUNT];
        this.normals = new Vector3f[VERTEX_COUNT][VERTEX_COUNT];
        this.textureCoords = new Vector2f[VERTEX_COUNT][VERTEX_COUNT];
        this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];

        this.xOffset = this.transform.position.x/terrainSize;
        this.zOffset = this.transform.position.z/terrainSize;

        System.out.printf("%f %f\n", xOffset, zOffset);

        this.model = new TexturedModel(generate(), texture);
        terrains.add(this);
    }

    public TexturedModel getModel() {
        return model;
    }

    public RawModel generate() {
        for(int z=0;z<VERTEX_COUNT;z++){
            for(int x=0;x<VERTEX_COUNT;x++){
                float calculatedX = (float)x/((float)VERTEX_COUNT - 1);
                float calculatedZ = (float)z/((float)VERTEX_COUNT - 1);

                vertices[z][x]= new Vector3f(calculatedX * SIZE,
                        calculateVertexHeight(calculatedX, calculatedZ),
                        calculatedZ * SIZE);

                textureCoords[z][x] = new Vector2f(calculatedX, calculatedZ);
            }
        }
        for (int z = 0; z <VERTEX_COUNT;z++){
            for (int x = 0; x < VERTEX_COUNT; x++) {
                normals[z][x] = calculateNormal(x, z);
            }
        }

        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        pointer = 0;
        float vertexArray[] = new float[this.count * 3];
        for (int z = 0; z < VERTEX_COUNT; z++) {
            for (int x = 0; x < VERTEX_COUNT; x++) {
                vertexArray[pointer++] = vertices[z][x].x;
                vertexArray[pointer++] = vertices[z][x].y;
                vertexArray[pointer++] = vertices[z][x].z;
            }
        }

        pointer = 0;
        float normalsArray[] = new float[this.count * 3];
        for (int z = 0; z < VERTEX_COUNT; z++) {
            for (int x = 0; x < VERTEX_COUNT; x++) {
                normalsArray[pointer++] = normals[z][x].x;
                normalsArray[pointer++] = normals[z][x].y;
                normalsArray[pointer++] = normals[z][x].z;
            }
        }

        pointer =0;
        float texArray[] = new float[this.count * 2];
        for (int z = 0; z < VERTEX_COUNT; z++) {
            for (int x = 0; x < VERTEX_COUNT; x++) {
                texArray[pointer++] = textureCoords[z][x].x;
                texArray[pointer++] = textureCoords[z][x].y;
            }
        }

        return Game.loader.loadToVAO(vertexArray, texArray, normalsArray, indices);
    }

    private float calculateVertexHeight(float x, float z){
        float noiseValue =  (float) noise.eval((xOffset + x)/xStretch * WAVELENGTH,
                (zOffset + z)/zStretch * WAVELENGTH);

        noiseValue *= (MAX_HEIGHT - MIN_HEIGHT);
        noiseValue += MIN_HEIGHT;

        return noiseValue;
    }

    private Vector3f calculateNormal(int x, int z) {

        int leftPointer = x - 1;
        int rightPointer = x + 1;
        int frontPointer = z + 1;
        int backPointer = z-1;

        float heightL = x > 0 ? vertices[z][leftPointer].y : 0;
        float heightR = x < VERTEX_COUNT - 1 ? vertices[z][rightPointer].y : 0;
        float heightD = z > 0 ? vertices[backPointer][x].y : 0;
        float heightU = z < VERTEX_COUNT - 1 ? vertices[frontPointer][x].y : 0;

        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
        normal.normalise();

        return normal;

    }

    public float getTerrainHeight(float worldX, float worldZ) {
        float terrainX = worldX - this.transform.position.x;
        float terrainZ = worldZ - this.transform.position.z;
        float gridSquareSize = SIZE / (VERTEX_COUNT -1 );
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= VERTEX_COUNT-1 || gridX < 0 || gridZ >= VERTEX_COUNT-1 || gridZ < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
        float answer;
        if (xCoord <= (1 - zCoord)) {
            answer = Maths.barryCentricInterpolation(
                    new Vector3f(0,vertices[gridZ][gridX].y,0),
                    new Vector3f(1,vertices[gridZ][gridX+1].y,0),
                    new Vector3f(0,vertices[gridZ+1][gridX].y,1),
                    new Vector2f(xCoord, zCoord)
            );
        } else {
            answer = Maths.barryCentricInterpolation(
                    new Vector3f(1,vertices[gridZ][gridX+1].y,0),
                    new Vector3f(1,vertices[gridZ+1][gridX+1].y,1),
                    new Vector3f(0,vertices[gridZ+1][gridX].y,1),
                    new Vector2f(xCoord, zCoord)
            );
        }

        return answer;

    }

    public void setOffset(Vector2f offset) {
        this.xOffset = offset.x;
        this.zOffset = offset.y;
    }
}
