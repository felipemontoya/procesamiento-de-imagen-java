/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Jhon
 */
public class LienzoBW  extends Canvas{
    /**
     * Constructor. Hace que el tamaño del canvas sea 100x100 pixels.
     */
    public LienzoBW(int pos)
    {
        this.setSize (257, 257);
        mayor=255;
        this.pos=pos;
    }

    /**
     * Dibuja la última línea que se le haya pasado.
     */
    public void paint(Graphics g)
    {

        if (datos == null) return;
        Color c;
        c = new Color(0, 0, 0);
        mayor=25;
        g.setColor(c);
        for(int i = 1;i<256;i++)
            g.drawLine (i, 255,i,255-Math.round(datos[i]/mayor));

        c = new Color(255, 0, 0);
        g.setColor(c);
         g.drawLine (pos, 255,pos,0);
    }

    /**
     * Guarda la línea que se le pasa para dibujarla cuando se le indique
     * llamando a paint()
     */
    public void setpos (int c)
    {
        pos = c;
    }
    public void getDatos(int[] data){
        datos=data;
    }


    /** La linea a dibujar */
    private int[] datos=null;
    private float mayor;
    private int pos;

}
