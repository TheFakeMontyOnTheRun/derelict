package br.odb.gamerendering.rendering;

import java.util.HashMap;

import br.odb.libsvg.SVGGraphic;


public class AssetManager {

    private final HashMap<String, SVGGraphic> graphics;
    private final HashMap<String, Integer> resIdMapping;

    public AssetManager() {
        graphics = new HashMap<>();
        resIdMapping = new HashMap<>();
    }

    public SVGGraphic getGraphics(String name) {
        return graphics.get(name);
    }

    public void putGraphic(String key, SVGGraphic graphic) {
        graphics.put(key, graphic);
    }

    public int getResIdForUri(String uri) {

        return resIdMapping.get(uri);
    }

    public void addResId(String string, int click) {
        resIdMapping.put(string, click);
    }
}
