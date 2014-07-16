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
public class AssetManager {

    HashMap< String, SVGGraphic> graphics;
    HashMap< String, AbstractMediaPlayer> mediaPlayers;
    HashMap< String, Integer> resIdMapping;

    public AssetManager() {
        graphics = new HashMap< String, SVGGraphic>();
        mediaPlayers = new HashMap< String, AbstractMediaPlayer>();
        resIdMapping = new HashMap< String, Integer>();
    }

    public SVGGraphic getGraphics(String name) {
        return graphics.get(name);
    }

    public void putGraphic(String key, SVGGraphic graphic) {
        graphics.put(key, graphic);
    }

    public int getResIdForUri(String uri) {

        return resIdMapping.get(uri).intValue();
    }

    public void addResId(String string, int click) {
        resIdMapping.put(string, new Integer(click));
    }

    public void registerMediaPlayer(String key, AbstractMediaPlayer mediaPlayer) {
        mediaPlayers.put(key, mediaPlayer);
    }

    public AbstractMediaPlayer getMediaPlayer(String uri) {
        return mediaPlayers.get(uri);
    }
}
