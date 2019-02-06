package entities.components;

import entities.GameObject;
import models.RawModel;
import renderEngine.Game;
import utils.OpenSimplexNoise;
import utils.vector.Vector3f;

public class TerrainMeshGenerator extends Component {

    private float SIZE;
    private int VERTEX_COUNT;
    private int count;
    private float[] vertices;
    private float[] normals;
    private float[] textureCoords;
    private int[] indices;


    private OpenSimplexNoise noise;
    private float MAX_HEIGHT = 20;
    private float MIN_HEIGHT = -20;
    private float WAVELENGTH = 8;

    public TerrainMeshGenerator(GameObject gameObject, float terrainSize, int terrainVertexCount) {
        super(gameObject);
        noise = new OpenSimplexNoise();
        this.SIZE = terrainSize;
        this.VERTEX_COUNT = terrainVertexCount;
        this.count = VERTEX_COUNT * VERTEX_COUNT;
        this.vertices = new float[count * 3];
        this.normals = new float[count * 3];
        this.textureCoords = new float[count*2];
        this.indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
    }

    public RawModel generate() {
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                float calculatedX = (float)j/((float)VERTEX_COUNT - 1);
                float calculatedZ = (float)i/((float)VERTEX_COUNT - 1);

                vertices[vertexPointer*3] = calculatedX * SIZE;
                vertices[vertexPointer*3+1] = getHeight(calculatedX * WAVELENGTH, calculatedZ * WAVELENGTH);
                vertices[vertexPointer*3+2] =  calculatedZ * SIZE;

                textureCoords[vertexPointer*2] = calculatedX;
                textureCoords[vertexPointer*2+1] = calculatedZ;
                vertexPointer++;
            }
        }
        vertexPointer = 0;
        for (int i = 0; i <VERTEX_COUNT;i++){
            for (int j = 0; j < VERTEX_COUNT; j++) {
                //TODO terrain normals
                Vector3f normal = calculateNormal(vertexPointer, j, i);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                vertexPointer++;
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

        return Game.loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(float x, float z){
        float noiseValue =  (float) noise.eval(x,z);

        noiseValue *= (MAX_HEIGHT - MIN_HEIGHT);
        noiseValue += MIN_HEIGHT;

        return noiseValue;
    }

    private Vector3f calculateNormal(int vertexPointer, int x, int z) {

        int leftPointer = vertexPointer - 1;
        int rightPointer = vertexPointer + 1;
        int frontPointer = vertexPointer - VERTEX_COUNT;
        int backPointer = vertexPointer + VERTEX_COUNT;

        float heightL = x > 0 ? vertices[leftPointer*3 + 1] : 0;
        float heightR = x < VERTEX_COUNT - 1 ? vertices[rightPointer*3 + 1] : 0;
        float heightD = z > 0 ? vertices[frontPointer*3 + 1] : 0;
        float heightU = z < VERTEX_COUNT - 1 ? vertices[backPointer*3 + 1] : 0;

        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
        normal.normalise();

        // System.out.println(x + " " + z + " - " + normal.x + " " + normal.z + " " + normal.y);

        return normal;

    }

}
