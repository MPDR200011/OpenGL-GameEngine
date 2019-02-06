package renderEngine.OBJParser;

import models.RawModel;
import renderEngine.Loader;
import utils.vector.Vector2f;
import utils.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class OBJLoader {

    public static RawModel loadObjModel(String fileName, Loader loader){
        FileReader reader = null;

        try {
            reader = new FileReader("res/" + fileName + ".obj");
        } catch (FileNotFoundException e) {
            System.err.println(fileName + ": Couldn't load file!");
            e.printStackTrace();
        }

        BufferedReader bf = new BufferedReader(reader);

        String line;

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textureCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        HashMap<Integer, int[]> vertexToTextureMap = new HashMap<>();
        HashMap<String, Integer> vertexPointerChanges = new HashMap<>();

        float[] verticesArray;
        float[] texturesCoordsArray;
        float[] normalsArray;
        int[] indicesArray;

        try {

            while (true) {
                line = bf.readLine();
                String[] currentLine = line.split(" ");

                if (line.startsWith("v ")){
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")){
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textureCoords.add(texture);
                } else if (line.startsWith("vn ")){
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }

            while (line != null){
                if (!line.startsWith("f ")){
                    line = bf.readLine();
                    continue;
                }
                String[] current = line.split(" ");

                for (int i = 1; i < 4; i++) {
                    String[] vertex = current[i].split("/");

                    int pointer = Integer.parseInt(vertex[0]) - 1;
                    int texturePointer = Integer.parseInt(vertex[1]) - 1;
                    int normalPointer = Integer.parseInt(vertex[2]) - 1;

                    int[] mapVertexData = vertexToTextureMap.get(pointer);
                    if (mapVertexData == null){
                        int[] data = {texturePointer, normalPointer};
                        vertexToTextureMap.put(pointer, data);
                    } else {

                        if (mapVertexData[0] != texturePointer || mapVertexData[1] != normalPointer){
                            String pointerTexRef = Integer.toString(pointer) + " "
                                    + Integer.toString(texturePointer) + " "
                                    + Integer.toString(normalPointer);
                            Integer newPointer = vertexPointerChanges.get(pointerTexRef);

                            if (newPointer == null){
                                vertices.add(vertices.get(pointer));
                                pointer = vertices.size() - 1;
                                vertexPointerChanges.put(pointerTexRef, pointer);
                            } else {
                                pointer = newPointer;
                            }

                            int[] data = {texturePointer, normalPointer};
                            vertexToTextureMap.put(pointer, data);

                        }

                    }

                    indices.add(pointer);
                }
                line = bf.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        texturesCoordsArray = new float[vertices.size()*2];
        normalsArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        int texturePointer = 0;
        int normalPointer = 0;
        SortedSet<Integer> keys = new TreeSet<>(vertexToTextureMap.keySet());

        for (Integer key: keys){

            Vector3f currentVertice = vertices.get(key);
            int[] vertexData = vertexToTextureMap.get(key);

            verticesArray[vertexPointer++] = currentVertice.x;
            verticesArray[vertexPointer++] = currentVertice.y;
            verticesArray[vertexPointer++] = currentVertice.z;

            Vector2f currentTexture = textureCoords.get(vertexData[0]);
            texturesCoordsArray[texturePointer++] = currentTexture.x;
            texturesCoordsArray[texturePointer++] = 1 - currentTexture.y;

            Vector3f currentNormal = normals.get(vertexData[1]);
            normalsArray[normalPointer++] = currentNormal.x;
            normalsArray[normalPointer++] = currentNormal.y;
            normalsArray[normalPointer++] = currentNormal.z;
        }

        for (int i = 0; i < indices.size(); i++){
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, texturesCoordsArray, normalsArray, indicesArray);
    }

}
