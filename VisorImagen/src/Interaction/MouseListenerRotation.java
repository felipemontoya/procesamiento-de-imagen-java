/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interaction;

import java.awt.event.MouseListener;

//import java.awt.*;
import java.awt.event.MouseEvent;
//import javax.swing.*;

/**
 *
 * @author Arzobispo
 */
public class MouseListenerRotation implements MouseListener {
     public int initPointX , initPointY , endPointX, endPointY;
     
     double[] dist;

     public MouseListenerRotation(double[] distance){
         dist = distance;
     }

    public void mouseClicked(MouseEvent mouse){ System.out.println("Rotation"); }
    public void mouseEntered(MouseEvent mouse){ /*System.out.println("entered");*/ }
    public void mouseExited(MouseEvent mouse){ /*System.out.println("exited");*/}
    public void mousePressed(MouseEvent mouse){
//        System.out.println("pressed");
        initPointX = mouse.getX();
        initPointY = mouse.getY();
    }
    public void mouseReleased(MouseEvent mouse){
//        System.out.println("released");
        endPointX = mouse.getX();
        endPointY = mouse.getY();

        double distance = endPointX-initPointX;
        
        dist[0] = distance;

        double a_root = Math.sqrt(Math.pow((endPointX-initPointX),2)+Math.pow((endPointY-initPointY),2));

        System.out.println("Distancia: " + a_root);

    }

}
