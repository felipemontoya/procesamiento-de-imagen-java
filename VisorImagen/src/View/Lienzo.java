/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

/**
 *
 * @author Jhon
 */
/*
 * Javier Abellán. 13 de febrero de 2004
 *
 * Lienzo.java
 *
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

/**
 * Clase que hereda de Canvas y sirve para dibujar una linea.
 */
public class Lienzo extends Canvas{
    /**
     * Constructor. Hace que el tamaño del canvas sea 100x100 pixels.
     */
    public Lienzo()
    {
        this.setSize (257, 257);
        mayor=255;
    }

    /**
     * Dibuja la última línea que se le haya pasado.
     */
    public void paint(Graphics g)
    {
       
        if (datos == null) return;
        Color c;
        if(color==0)
        c = new Color(255, 0, 0);
        else if(color == 1)
        c = new Color(0,255, 0);
        else
        c = new Color(0,0, 255);
        g.setColor(c);
        mayor/=255;
        for(int i = 1;i<256;i++)
            g.drawLine (i-1, 255-Math.round(datos[i-1]/mayor),i,255-Math.round(datos[i]/mayor));
        
    }

    /**
     * Guarda la línea que se le pasa para dibujarla cuando se le indique
     * llamando a paint()
     */
    public void color (int c)
    {
        color = c;
    }
    public void getDatos(int[] data){
        datos=data;
    }

    public void setMayor(int mayor){
        this.mayor=mayor;

    }
    /** La linea a dibujar */
    private int[] datos=null;
    private int color;
    private float mayor;

}