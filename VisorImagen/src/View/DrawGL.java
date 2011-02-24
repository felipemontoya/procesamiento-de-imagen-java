/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import Data.ImageData;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* Importamos las clases Container, BorderLayout,
 * Toolkit y Dimension del paquete AWT. */
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

// Importamos la librería de OpenGL
import javax.media.opengl.*;
import javax.swing.JInternalFrame;

/**
 *
 * @author Jhon
 */
public class DrawGL extends JInternalFrame implements GLEventListener{
    /* Necesitaremos un JPanel para introducir
	 * los elementos que se mostrarán en el JFrame. */
	JPanel panelDibujo;

	/* Declaramos un contenedor para introducir
	 * nuestro panel principal, el contenedor
	 * será el JFrame en general. */
	Container contenedor;

	/* El toolkit es útil para obtener
	 * información básica de la computadora, cómo la resolución
	 * o dimensión de la pantalla,
	 * dónde se está ejecutando el programa */
	Toolkit kit;

	/* Esta variable la utilizaremos para almacenar
	 * la dimensión (ancho x alto), en pixeles, de
	 * la pantalla dónde se está ejecutando el programa. */
	Dimension dimensionPantalla;

	// Variable para almacenar la altura, en pixeles, de la pantalla
	int altura;
	// Variable para almacenar la anchura, en pixeles, de la pantalla
    int anchura;

    // La interfaz GL nos proporcionará el acceso a las funciones de OpenGL
    static GL gl;
    /* La clase GLCanvas nos proporciona el soporte para el renderizado
     * de los gráficos de OpenGL, por el momento solamente la utilizaremos
     * para mostrar los cuatro métodos principales de GLEventListener */
    static GLCanvas canvas;
    ImageData im;
   // Constructor
    public DrawGL(){
        super();
    }

    public void DrawGLInit(ImageData i,String nombre)
    {
    	/* Llamamos a la superclase de JFrame
    	 * la cual colocará un título al mismo. */

        this.setTitle(nombre);
    	/* Instanciamos un objeto de Toolkit para obtener
    	 * los datos generales de nuestra computadora. */
    	kit = Toolkit.getDefaultToolkit();
        /* Obtenemos la dimensión de la pantalla en pixeles */
    	dimensionPantalla = kit.getScreenSize();
        this.im = i;
    	/* Almacenamos la altura y anchura, en pixeles, de la pantalla.
    	 * Debido a que los métodos "getHeight" y "getWidth" regresan una
    	 * variable de tipo double, necesitaremos obligar
    	 * al programa que convierta dicha variable a int. */
    	altura = i.getHeight();
    	anchura =  i.getWidth();

    	// Creamos el objeto de la clase GLCanvas
        canvas = new GLCanvas();

        /* Añadimos el oyente de eventos para el renderizado de OpenGL,
         * esto automáticamente llamará a init() y renderizará los
         * gráficos cuyo código haya sido escrito dentro del método display() */
        canvas.addGLEventListener(this);

        /* Inicializamos la interfaz de GL la cual utilizaremos
        * para llamar a las funciones de OpenGL */
        gl = canvas.getGL();

    	panelDibujo = new JPanel(new BorderLayout());

    	/* Agregamos el objeto GLCanvas dentro del JPanel
    	 * para que los gráficos renderizados dentro del
    	 * objeto GLCanvas puedan ser visualizados. */
    	panelDibujo.add(canvas, BorderLayout.CENTER);

    	/* Le decimos a Java que el contenedor será el mismo JFrame */
    	contenedor = getContentPane();

    	/* Agregamos el JPanel dentro del JFrame utilizando el
    	 * contenedor creado previamente, situando al JPanel
    	 * en el centro del JFrame utilizando BorderLayout */
    	contenedor.add(panelDibujo, BorderLayout.CENTER);

    	/* Ya que tenemos obtenida la altura y anchura, en pixeles
    	 * de la pantalla, definimos el tamaño del JFrame.
    	 * En este caso será del tamaño de la mitad de ancho
    	 * de la pantalla por la mitad de altura de la pantalla. */
    	this.setSize(anchura, altura);

    	/* Ahora situamos al JFrame exactamente en el centro de la
    	 * pantalla donde se está ejecutando el programa */
    	this.setLocation(dimensionPantalla.width/4, dimensionPantalla.height/4);

    	/* Lo hacemos redimensionable y visible */
    	this.setResizable(true);
    	this.setVisible(true);

    	/* Finalmente, le indicamos a Java que queremos que nuestro
    	 * JFrame se cierre cuando demos click en el botón cerrar
    	 * que aparecerá en la parte superior derecha del mismo. */
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // A continuación se muestran los métodos utilizados por GLEventListener

    /* Como se explicó, este método es el que inicializará
     * los gráficos de OpenGL que GLCanvas utilizará,
     * para llamar a las funciones de OpenGL utilizaremos el objeto gl
     * creado anteriormente. */
    public void init(GLAutoDrawable drawable)
    {
    	// Escribimos en pantalla la versión del S.O.
        System.out.println (gl.glGetString(GL.GL_VERSION));
        // Escribimos en pantalla la marca de nuestra tarjeta de video
        System.out.println (gl.glGetString(GL.GL_VENDOR));
        /* El fondo de los gráficos mostrados en el objeto
         * GLCanvas será de color negro. Como se puede apreciar, estamos
         * utilizando el método de OpenGL glClearColor(float, float, float, float)
         * mediante la interfaz GL. */
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        /* Este método, como se explicó, se utiliza para que el
         * usuario pueda modificar el "viewport" de los gráficos
         * adecuadamente. En este caso declararemos que nuestro espacio
         * de trabajo será del mismo tamaño que el JFrame. Esto se hace para que
         * el punto que dibujemos aparezca exactamente en el centro del JFrame.*/
        gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();

		/* Se define el punto de vista (viewport) de nuestro objeto GLCanvas
		 * el cual, como se dijo anteriormente, será del mismo tamaño que el
		 * JFrame. De esta manera, si existe un cambio en  el tamaño del
		 * JFrame, el espacio de trabajo se ajustará al tamaño del mismo para
		 * que el punto siga mostrándose en el centro. */
		gl.glOrtho(0, anchura, 0, altura, -1.0, 1.0);

		/* Si el JFrame se dimensiona, se vuelven a dibujar los gráficos. */
        canvas.repaint();
    }

    public void display(GLAutoDrawable drawable)
    {
        /* Este método es utilizado para crear todos los gráficos que
         * se dibujarán dentro del objeto GLCanvas. Primero llamamos al
         * método de OpenGL glClear(GLBitField mask) el cual limpiará
         * todos los buffers para poder dibujar. */
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        /* Ahora llamamos al método glColor3f(float, float, float)
         * el cual definirá el color de los gráficos que se
         * dibujarán. En este caso dibujaremos un punto de color Azul. */


		/* Definimos el tamaño del punto que dibujaremos utilizando
		 * el método glPointSize() de OpenGL, en este caso será de 10 pixeles. */
		gl.glPointSize(1);
		/* Esto es prácticamente igual a una declaración hecha
		 * en C ó C++ utilizando OpenGL. Indicamos que iniciaremos
		 * a dibujar con el método glBegin(GLEnum Mode) y que finalizaremos
		 * con el método glEnd(). Dentro de ambos métodos irán TODOS
		 * los gráficos que dibujaremos. */
		gl.glBegin(GL.GL_POINTS);
		for(int i = 0;i<im.getHeight();i++){
                    for(int j=0;j<im.getWidth();j++){

                       gl.glColor3f(color(im.bytesImage[i][j][2]), color(im.bytesImage[i][j][1]), color(im.bytesImage[i][j][0]));
                       //gl.glColor3b((im.bytesImage[i][j][2]), (im.bytesImage[i][j][1]), (im.bytesImage[i][j][0]));

                       gl.glVertex2i(j, i);

                    }
                }

		gl.glEnd();

		/* Indicamos que dibuje inmediatamente después utilizando el método
		 * glFlush(); */
		gl.glFlush();
    }

     public int color(byte v){
         byte[] valor = new byte[4];
         for(byte bb : valor){
            bb=0;
        }
         valor[0]=v;
         boolean bigEndian = false;
	     if(valor.length < 4){
	          throw new ArrayIndexOutOfBoundsException(valor. length);
	     }
	     int a, b, c, d;
	     if(bigEndian){
	          a = (valor[0] & 0xFF) << 24;
	          b = (valor[1] & 0xFF) << 16;
	          c = (valor[2] & 0xFF) << 8;
	          d =  valor[3] & 0xFF;
	     } else{
	          a = (valor[3] & 0xFF) << 24;
	          b = (valor[2] & 0xFF) << 16;
	          c = (valor[1] & 0xFF) << 8;
	          d =  valor[0] & 0xFF;
	     }
	     return  a | b | c | d;
	}


    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
    {
    	/* Método para el manejo de eventos del cambio de visualizador, este
    	 * tampoco lo utilizaremos ahora. */
    }

    // Finalizan los métodos utilizados por GLEventListener

    // Método principal para iniciar la ejecución de nuestro programa
    /*public static void main(String[] args)
    {
    	 * jFrameCentrado, entonces el programa ejecutará
    	 * el código del constructor.
    	new puntoJOGL();
    } */



}
