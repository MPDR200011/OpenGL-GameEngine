package renderEngine;

import models.RawModel;
import renderEngine.OBJParser.OBJLoader;

import java.util.HashMap;
import java.util.Map;

public class RawModelManager {

    private static Map<String, RawModel> rawModelMap = new HashMap<>();

    public static RawModel getRawModel(String fileName){
        RawModel model = rawModelMap.get(fileName);

        if (model != null){
            return model;
        } else {
            model = OBJLoader.loadObjModel(fileName, Game.loader);
            rawModelMap.put(fileName, model);
            return model;
        }
    }

}
