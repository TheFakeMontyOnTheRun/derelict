/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.derelict.graphics2d;

import java.util.HashMap;

import br.odb.gameapp.AbstractMediaPlayer;
import br.odb.libsvg.SVGGraphic;

/**
 *
 * @author monty
 */
class AssetManager {

    private final HashMap< String, SVGGraphic> graphics;
    private final HashMap< String, AbstractMediaPlayer> mediaPlayers;
    private final HashMap< String, Integer> resIdMapping;

    public AssetManager() {
        graphics = new HashMap<>();
        mediaPlayers = new HashMap<>();
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

    public void registerMediaPlayer(String key, AbstractMediaPlayer mediaPlayer) {
        mediaPlayers.put(key, mediaPlayer);
    }

    public AbstractMediaPlayer getMediaPlayer(String uri) {
        return mediaPlayers.get(uri);
    }
}
