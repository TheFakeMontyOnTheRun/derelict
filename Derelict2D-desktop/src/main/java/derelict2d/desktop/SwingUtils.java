/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package derelict2d.desktop;

import java.awt.Color;

/**
 *
 * @author monty
 */
class SwingUtils {

    static Color getSwingColor(br.odb.utils.Color color) {
        Color swingColor = new Color( color.getR(), color.getG(), color.getB() );
        
        return swingColor;
    }
    
}
