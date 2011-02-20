/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;

import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.texture.*;



/**
 *
 * @author Felipe
 */
public class PintorGL implements GLEventListener {

  private File curDir;

  public void run() {
    JPopupMenu.setDefaultLightWeightPopupEnabled(false);

    JFrame frame = new JFrame("Texture Loader Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final GLCanvas canvas = new GLCanvas();

    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem item = new JMenuItem("Open texture...");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          JFileChooser chooser = new JFileChooser(curDir);
          int res = chooser.showOpenDialog(null);
          if (res == JFileChooser.APPROVE_OPTION) {
            File chosen = chooser.getSelectedFile();
            if (chosen != null) {
              curDir = chosen.getParentFile();
              setTextureFile(chosen);
              canvas.repaint();
            }
          }
        }
      });
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
    menu.add(item);

    item = new JMenuItem("Flush texture");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          flushTexture();
          canvas.repaint();
        }
      });
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
    menu.add(item);

    item = new JMenuItem("Exit");
    item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      });
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
    menu.add(item);

    menuBar.add(menu);

    canvas.addGLEventListener(this);
    frame.getContentPane().add(canvas);
    frame.setJMenuBar(menuBar);
    frame.setSize(800, 600);
    frame.setVisible(true);
  }

  private boolean newTexture;
  private boolean flushTexture;
  private File file;
  private Texture texture;
  private GLU glu = new GLU();

  public void setTextureFile(File file) {
    this.file = file;
    newTexture = true;
  }

  public void flushTexture() {
    flushTexture = true;
  }

  public void init(GLAutoDrawable drawable) {
    drawable.setGL(new DebugGL(drawable.getGL()));

    GL gl = drawable.getGL();
    gl.glClearColor(0, 0, 0, 0);
    gl.glEnable(GL.GL_DEPTH_TEST);
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL gl = drawable.getGL();
    gl.glMatrixMode(GL.GL_PROJECTION);
    gl.glLoadIdentity();
    glu.gluOrtho2D(0, 1, 0, 1);
    gl.glMatrixMode(GL.GL_MODELVIEW);
    gl.glLoadIdentity();
  }

  public void display(GLAutoDrawable drawable) {
    GL gl = drawable.getGL();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    if (flushTexture) {
      flushTexture = false;
      if (texture != null) {
        texture.dispose();
        texture = null;
      }
    }

    if (newTexture) {
      newTexture = false;

      if (texture != null) {
        texture.dispose();
        texture = null;
      }

      try {
        System.err.println("Loading texture...");
        texture = TextureIO.newTexture(file, true);
        System.err.println("Texture estimated memory size = " + texture.getEstimatedMemorySize());
      } catch (IOException e) {
        e.printStackTrace();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bos));
        JOptionPane.showMessageDialog(null,
                                      bos.toString(),
                                      "Error loading texture",
                                      JOptionPane.ERROR_MESSAGE);
        return;
      }
    }

    if (texture != null) {
      texture.enable();
      texture.bind();
      gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
      TextureCoords coords = texture.getImageTexCoords();

      gl.glBegin(GL.GL_QUADS);
      gl.glTexCoord2f(coords.left(), coords.bottom());
      gl.glVertex3f(0, 0, 0);
      gl.glTexCoord2f(coords.right(), coords.bottom());
      gl.glVertex3f(1, 0, 0);
      gl.glTexCoord2f(coords.right(), coords.top());
      gl.glVertex3f(1, 1, 0);
      gl.glTexCoord2f(coords.left(), coords.top());
      gl.glVertex3f(0, 1, 0);
      gl.glEnd();
      texture.disable();
    }
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
}