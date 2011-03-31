
package test_Draw;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class cTest_mouseInteractor extends JPanel implements MouseListener{

    public cTest_mouseInteractor(){
        super();
        pointX=20;
        pointY=20;
        oldX=0;
        oldY=0;
        addMouseListener(this);
    }

    int pointX, pointY, oldX, oldY ,initPointX , initPointY , endPointX, endPointY;

    public void paintComponent(Graphics g){
    // Draw a line from the prior mouse click to new one.
//        g.drawLine(oldX,oldY,pointX,pointY);
//        g.
    }

    public void mouseClicked(MouseEvent mouse){
    // Copy the last clicked location into the 'old' variables.
//        oldX=pointX;
//        oldY=pointY;
    // Get the location of the current mouse click.
//        pointX = mouse.getX();
//        pointY = mouse.getY();
    // Tell the panel that we need to redraw things.
//        repaint();
//        g.drawLine(oldX,oldY,pointX,pointY);
//        System.out.print();
    }

/* The following methods have to be here to comply
   with the MouseListener interface, but we don't
   use them, so their code blocks are empty. */
    public void mouseEntered(MouseEvent mouse){ }
    public void mouseExited(MouseEvent mouse){ }
    public void mousePressed(MouseEvent mouse){
        initPointX = mouse.getX();
        initPointY = mouse.getY();
    }
    public void mouseReleased(MouseEvent mouse){
        endPointX = mouse.getX();
        endPointY = mouse.getY();

        double a_root = Math.sqrt(Math.pow((endPointX-initPointX),2)+Math.pow((endPointY-initPointY),2));

        System.out.println("Distancia: " + a_root);
    }

    public static void main(String arg[]){
        JFrame frame = new JFrame("MousePanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250,250);

        cTest_mouseInteractor panel = new cTest_mouseInteractor();
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}