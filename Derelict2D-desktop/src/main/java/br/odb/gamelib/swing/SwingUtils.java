/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.gamelib.swing;

import java.awt.Color;

/**
 *
 * @author monty
 */
public class SwingUtils {

    public static Color getSwingColor(br.odb.utils.Color color) {
        Color swingColor = new Color( color.r, color.g, color.b, color.a );
        
        return swingColor;
    }
    
}
