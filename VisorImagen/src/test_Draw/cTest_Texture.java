/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test_Draw;

import java.awt.event.*;
import javax.swing.*;

import java.nio.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;


/**
 *
 * @author Felipe
 */
public class cTest_Texture  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLCapabilities caps;
  private GLCanvas canvas;
  /* Create checkerboard texture */
  private static final int checkImageWidth = 64;
  private static final int checkImageHeight = 64;
  private static final int color = 3;
  //private byte checkImage[][][] = new byte[checkImageWidth][checkImageHeight][color];
  private ByteBuffer checkImageBuf = //
  BufferUtil.newByteBuffer(checkImageHeight * checkImageWidth * color);


  public cTest_Texture()
  {
    super("Texture loading");

    caps = new GLCapabilities();
    // caps.setSampleBuffers(true );
    canvas = new GLCanvas(caps);
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);

    getContentPane().add(canvas);
  }

  public void run()
  {
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.repaint();

    canvas.requestFocusInWindow();

  }

  public static void main(String[] args)
  {
    new cTest_Texture().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL gl = drawable.getGL();
    glu = new GLU();

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);

    makeCheckImage();

    gl.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, color, checkImageWidth, checkImageHeight, 0,
        GL.GL_RGB, GL.GL_UNSIGNED_BYTE, checkImageBuf);// checkImage[0][0][0]);
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
    gl.glVertex3f(-1.0f, -1.0f, 0.0f);
    gl.glTexCoord2f(0.0f, 1.0f);
    gl.glVertex3f(-1.0f, 1.0f, 0.0f);
    gl.glTexCoord2f(1.0f, 1.0f);
    gl.glVertex3f(1.0f, 1.0f, 0.0f);
    gl.glTexCoord2f(1.0f, 0.0f);
    gl.glVertex3f(1.0f, -1.0f, 0.0f);

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
  private void makeCheckImage()
  {
    byte c = (byte) 0xFF;

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
    }
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

