package renderEngine;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private static Map<String, Integer> textureMap = new HashMap<>();

    public static Integer getTexture(String fileName) {
        Integer texture = textureMap.get(fileName);

        if (texture != null) {
            return texture;
        } else {
            texture = Game.loader.loadTexture(fileName);
            textureMap.put(fileName, texture);
            return texture;
        }
    }

}
