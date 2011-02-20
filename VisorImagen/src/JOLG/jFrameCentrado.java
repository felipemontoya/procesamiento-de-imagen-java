package JOLG;


import javax.swing.JFrame;
import javax.swing.JPanel;
/* Importamos las clases JFrame y JPanel
 * contenidas dentro del paquete SWING */
/* Importamos las clases Container, BorderLayout,
 * Toolkit y Dimension del paquete AWT. */
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

public class jFrameCentrado extends JFrame
{
	/* Necesitaremos un JPanel para introducir
	 * los elementos que se mostrarán en el JFrame. */
	JPanel panel;

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

    // Constructor
    public jFrameCentrado()
    {
    	/* Llamamos a la superclase de JFrame
    	 * la cual colocará un título al mismo. */
    	super("Frame centrado");

    	/* Instanciamos un objeto de Toolkit para obtener
    	 * los datos generales de nuestra computadora. */
    	kit = Toolkit.getDefaultToolkit();

    	/* Obtenemos la dimensión de la pantalla en pixeles */
    	dimensionPantalla = kit.getScreenSize();

    	/* Almacenamos la altura y anchura, en pixeles, de la pantalla.
    	 * Debido a que los métodos "getHeight" y "getWidth" regresan una
    	 * variable de tipo double, necesitaremos obligar
    	 * al programa que convierta dicha variable a int. */
    	altura = (int)dimensionPantalla.getHeight();
    	anchura = (int)dimensionPantalla.getWidth();

    	/* Le decimos a Java que el contenedor será el mismo JFrame */
    	contenedor = getContentPane();

    	/* Instanciamos un objeto de la clase JPanel, en este
    	 * caso, estará vacío. */
    	panel = new JPanel();

    	/* Agregamos el JPanel dentro del JFrame utilizando el
    	 * contenedor creado previamente, situando al JPanel
    	 * en el centro del JFrame utilizando BorderLayout */
    	contenedor.add(panel, BorderLayout.CENTER);

    	/* Ya que tenemos obtenida la altura y anchura, en pixeles
    	 * de la pantalla, definimos el tamaño del JFrame.
    	 * En este caso será del tamaño de la mitad de ancho
    	 * de la pantalla por la mitad de altura de la pantalla. */
    	this.setSize(anchura/2, altura/2);

    	/* Ahora situamos al JFrame exactamente en el centro de la
    	 * pantalla donde se está ejecutando el programa */
    	this.setLocation(anchura/4, altura/4);

    	/* Lo hacemos redimensionable y visible */
    	this.setResizable(true);
    	this.setVisible(true);

    	/* Finalmente, le indicamos a Java que queremos que nuestro
    	 * JFrame se cierre cuando demos click en el botón cerrar
    	 * que aparecerá en la parte superior derecha del mismo. */
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Método principal para iniciar la ejecución de nuestro programa
    public static void main(String[] args)
    {
    	/* Simplemente crearemos un objeto de la clase
    	 * jFrameCentrado, entonces el programa ejecutará
    	 * el código del constructor. */
    	new jFrameCentrado();
    }
}