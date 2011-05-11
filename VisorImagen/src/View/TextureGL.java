
/***************************************************************************************************************/
/*    Especificaciones de la Programación
 *    Patrón
 *    Versión 1.0
 *
 *    General:
 *          - Toda clase debe tener la información de esta especificación.
 *          - Todo procedimiento/variable programado será debidamente comentado dependiendo de su naturaleza.
 *          - Todas las variables, clases y métodos deberán ser nombradas en ingles y dependiendo de su función.
 *    Paquetes:
 *          - Toda clase estará incluida en un paquete.
 *          - El nombre del paquete deberá ser acorde a las clases contenidas en el.
 *          - Si son paquetes de prueba deben estar identificados iniciando con "test_nombredelpaquete".
 *          - Si son paquetes que serán utilizados temporalmente para ser mas tarde eliminador irán identificados
 *            iniciando con "temp_nombredelpaquete".
 *
 *    Clases:
 *          - Toda clase iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: ClaseEjemplo
 *          - El nombre de la clase debe identificar su funcionalidad.
 *          - Antes iniciar la clase debe ser comentado para que sirve dicha clase y como es su constructor.
 *          - Si son clases de prueba irán identificados como "cTest_NombreClase"
 *
 *    Métodos
 *          - Toda método iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: MetodoEjemplo
 *          - El nombre de el método debe identificar su funcionalidad. Si el método es un get o un set ira en minúscula
 *          - Antes iniciar el método debe ser comentado para que sirve dicha clase, sus entradas y salidas si tiene
 *
 *    Variables
 *          - Todo nombre de variable inicia en minúsculas, si el nombre de la variable está compuesta por mas palabras
 *            Las siguientes palabras deberán iniciar con la primera letra en mayúscula ej.: variableEjemplo.
 *          - El nombre de la variable debe tener sentido acorde a su valor y su utilidad.
 *          - si son variables auxiliares o temporal deben ir identificados como a_nombreVariable o t_nombreVariable.
 *          - las variables globales ya están plenamente identificadas con color verde.
 *          - Cuando se declara una variable debe comentarse una breve funcionalidad de dicha variable.
 *
 *   léase y cúmplase
 */
/***************************************************************************************************************/

package View;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;

import Readers.*;
import Data.ImageData;
import java.io.File;
import PipeLine.*;
import Interaction.*;

/**
 *
 * @author Felipe
 */

/* Esta clase es una clase de desarrollo para obtener los métodos correctos que
 cargan una clase ImageData en una textura válida de JOGL*/

public class TextureGL  extends JInternalFrame
    implements GLEventListener, KeyListener, MouseListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  private ImageData image;
  /* Create checkerboard texture */
  private int checkImageWidth;
  private int checkImageHeight;
  private int color;
  //private byte checkImage[][][] = new byte[checkImageWidth][checkImageHeight][color];
  private ByteBuffer checkImageBuf;
  private Painter painter;

 double[] clickDistance = new double[1];
  
  public TextureGL(Painter painter){
        super();
        this.painter = painter;
    }

     public int initPointX , initPointY , endPointX, endPointY;

     double[] dist;

    public void mouseClicked(MouseEvent mouse){ 
        System.out.println("Mouse");
//        System.out.println("Modifiers " + mouse.getModifiers());
//        System.out.println("raton " + mouse.getX() + " " + mouse.getY());
//        painter.Update();
    }
    public void mouseEntered(MouseEvent mouse){ /*System.out.println("entered");*/ }
    public void mouseExited(MouseEvent mouse){ /*System.out.println("exited");*/}
    public void mousePressed(MouseEvent mouse){

        initPointX = mouse.getX();
        initPointY = mouse.getY();
//        System.out.println("y" + initPointY);

        if (mouse.getButton() == MouseEvent.BUTTON3 && mouse.isControlDown())
        {
            PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Crop,"Actual Cropping");
            msg.bValue1 = false;
            msg.bValue2 = false;
            painter.PassMessage(msg);
            painter.Update();
        }


    }
    public void mouseReleased(MouseEvent mouse){
//        System.out.println("released");
        endPointX = mouse.getX();
        endPointY = mouse.getY();

        double distance = endPointX-initPointX;

        if(mouse.isShiftDown())
        {
            System.out.println("Distancia: " + distance);
            PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Rotate,"Rotation");
            msg.dValue1 = distance*(Math.PI/180);
            painter.PassMessage(msg);
            painter.Update();
        }
        else if(mouse.isControlDown())
        {
            Insets insets = this.getInsets();
            int insetwidth = insets.left + insets.right;
//            int insetheight = insets.top + insets.bottom;
            double yRate = ((double)this.getSize().height - insetwidth)/(double)canvas.getSize().height;
            double xRate = ((double)this.getSize().width)/(double)canvas.getSize().width;


            PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Crop,"Marking");
            msg.iValue1 = (int) (xRate*Math.min(initPointX, endPointX));
            msg.iValue2 = (int) (yRate*Math.min(initPointY, endPointY));
            msg.iValue3 = (int) (xRate*Math.abs(initPointX - endPointX));
            msg.iValue4 = (int) (yRate*Math.abs(initPointY - endPointY));
            msg.bValue1 = true;
            msg.bValue2 = true;

            painter.PassMessage(msg);
            painter.Update();
        }
        else
        {
            PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Gamma,"Gamma");
            msg.dValue1 = distance/200;

            painter.PassMessage(msg);
            painter.Update();
        }
    }

  public void TextureGLInit(ImageData i,String name)
  {
    this.setTitle(name);
//    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    image = i;
    caps = new GLCapabilities();
    // caps.setSampleBuffers(true );
    if (canvas!=null){
        getContentPane().remove(canvas);
    }

    canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
    canvas.addMouseListener(this);
    getContentPane().add(canvas);

    this.checkImageHeight=image.getHeight();
    this.checkImageWidth=image.getWidth();
    this.color=image.getnCanales();
    checkImageBuf =   BufferUtil.newByteBuffer(checkImageHeight * checkImageWidth * color);
    this.setSize(checkImageWidth,checkImageHeight + 20);
    
    this.setResizable(false);
    this.setClosable(true);
    	this.setVisible(true);
        canvas.repaint();
  }

  public void run()
  {
    setSize(image.getHeight(), image.getWidth());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();




  }



  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);

    MakeTextureImage();
    
    int colorSpace = 0;
    switch(image.getEspacioColor()){
        case ImageData.RGB:
            colorSpace = GL.GL_RGB;
            break;
        case ImageData.BGR:
            colorSpace = GL.GL_BGR;
            break;
    }
    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, color, checkImageWidth, checkImageHeight, 0,
        colorSpace, GL.GL_UNSIGNED_BYTE, checkImageBuf);// checkImage[0][0][0]);

    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
    gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
    gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL);
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glShadeModel(GL.GL_FLAT);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();

    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    gl.glBegin(GL.GL_QUADS);
    gl.glTexCoord2f(0.0f, 0.0f);
    gl.glVertex3f(-1.0f, 1.0f, 0.0f);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex3f(-1.0f, -1.0f, 0.0f);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex3f(1.0f, -1.0f, 0.0f);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex3f(1.0f, 1.0f, 0.0f);

    gl.glEnd();

    gl.glFlush();



  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL gl = drawable.getGL();

    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  /*
   * 3D array won't be used. I left it here for you to see.
   */
  private void MakeTextureImage()
  {
      // Se busca obtener un ImageData y a partir de el alimentar a la textura

checkImageBuf.clear();
      checkImageBuf.put(image.bytesImage,0,image.getHeight()*image.getWidth()*3);
      
   /* byte c = (byte) 0xFF;

    for (int i = 0; i < checkImageWidth; i++)
    {
      for (int j = 0; j < checkImageHeight; j++)
      {
        // c = ((((i & 0x8) == 0) ^ ((j & 0x8)) == 0)) * 255;
        c = (byte)( ( ((byte)((i & 0x8)==0?0x00:0xff)//
            ^(byte)((j & 0x8)==0?0x00:0xff))));

        checkImageBuf.put((byte) c);
        checkImageBuf.put((byte) c);
        checkImageBuf.put((byte) 255);
      }
    }//*/

    checkImageBuf.rewind();
  }//

  public void keyTyped(KeyEvent key)
  {
    // TODO Auto-generated method stub
  }

  public void keyPressed(KeyEvent key)
  {
    switch (key.getKeyChar()) {
      case KeyEvent.VK_ESCAPE:
        System.exit(0);
        break;

      default:
        break;
    }
  }

  public void keyReleased(KeyEvent key)
  {
    // TODO Auto-generated method stub
  }

}

