/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.gamelib.swing;

import br.odb.gameapp.AbstractMediaPlayer;
import java.applet.AudioClip;

/**
 *
 * @author monty
 */
public class SwingMediaPlayer extends AbstractMediaPlayer {
    AudioClip clip;

    public SwingMediaPlayer(AudioClip clip) {
        this.clip = clip;
    }

    @Override
    public void play() {
        clip.play();
    }

    @Override
    public void loop() {
        clip.loop();
    }

    @Override
    public void stop() {
        clip.stop();
    }    
}
