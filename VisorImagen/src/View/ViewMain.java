/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VisorMain.java
 *
 * Created on 10-feb-2011, 18:41:14
 */


/***************************************************************************************************************/
/*    Especificaciones de la Programación
 *    Patrón
 *    Versión 1.1
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

import Readers.FileReader;
import Data.ImageData;
import test_Draw.cTest_puntoJOGL;
import java.io.IOException;
import javax.swing.*;
import java.io.File;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;

import Filters.*;

/**
 *
 * @author Jhon & Felipe
 */

import javax.media.opengl.*;
/*   Clase:ViewMain
 *   clase que inicia el programa, tiene main propio.
 *   configura y pinta la interfaz grafica del usuario.
 */

public class ViewMain extends javax.swing.JFrame{

    private final PipedInputStream pin=new PipedInputStream();
    private final PipedInputStream pin2=new PipedInputStream();
    ImageData image;
    // Variables del manejo de archivo

     private boolean isFileOpen = false;
     private File currentFile;
     

     //pintor
     DrawGL draw;
     TextureGL texture;
    // Se encarga de escribir los out.println en el programa
    OutputStream out = new OutputStream(){
            public void write(int b) throws IOException
            {
                jTextArea1.append(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len)
            {
                jTextArea1.append(new String(b, off, len));
            }
        };

    //Se encarga de configurar el area donde se imprimen los datos
    private void ConfigTextArea() {
        System.setOut(new PrintStream(out, true));
        jTextArea1.setEditable(false);
        System.out.println("Mensajes:");
    }

    //Se encarga de ambientar la aplicación en el estilo del sistema operativo
    private void ConfigNativeStyle() {
       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
           System.out.println("No se puede configurar el estilo nativo del sistema operativo: " + ex);
        }
    }

    /** Creates new form VisorMain */
    public ViewMain() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}
        initComponents();
        this.ConfigTextArea();
        this.ConfigNativeStyle();
        this.setExtendedState(this.getExtendedState()|ViewMain.MAXIMIZED_BOTH);
        this.setTitle("MegaVisor - Procesamiento de Imagenes - Unal");
        draw = new DrawGL();
//        this.add(draw);
        texture = new TextureGL();
//        this.add(texture);
        //PintorGL pintor = new PintorGL();
       //pintor.run();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(334, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //variable que indica que desea abrir un archivo
        boolean a_isSafe = false;
        
        if(isFileOpen){
            JOptionPane optionPane = new JOptionPane(
                "Dialogo\n",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);
            int showConfirmDialog = JOptionPane.showConfirmDialog(optionPane, "Ya hay un archivo abierto. ¿Desea continuar?");
           if ( showConfirmDialog == JOptionPane.YES_OPTION){
               a_isSafe = true;
           }

        }

        if (!isFileOpen || a_isSafe){
            //chooserFile:para escojer el archivo que se quiere abrir
            JFileChooser chooserFile = new JFileChooser(System.getProperty("user.dir"));
            //filtro para solo leer imagenes
            FileFilter filterImage = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "bmp");
            chooserFile.addChoosableFileFilter(filterImage);
            //variable que nos indica que se puede abrir el archivo elegido
            int a_result = chooserFile.showOpenDialog(null);
            if(a_result == JFileChooser.APPROVE_OPTION){
                currentFile = chooserFile.getSelectedFile();
                //impresion de los datos del archivo, modificacion del titulo
                if (currentFile.canRead() && currentFile.canWrite() && currentFile.exists()){
                    System.out.println("Archivo seleccionado: " + currentFile.getAbsolutePath());
                    ViewMain.getFrames()[0].setTitle("MegaVisor - Procesamiento de Imágenes - Unal : \\"+currentFile.getName());
                    isFileOpen = true;

                   //crea el lector de imagenes y le pasa el archivo actual.
                   FileReader read = new FileReader(currentFile);
                   //crea la clase con los datos de la imgen.
                    //image = read.readImage();

                    

                   // draw.DrawGLInit(image,currentFile.getName());
                    //texture.TextureGLInit(image,currentFile.getName());
                    //image.informacion();
                    //imagen.data();
                    
                   //Creación del pipeline

                    BlankFilter filter1 = new BlankFilter("filtro1");
                    BlankFilter filter2 = new BlankFilter("filtro2");
                    BlankFilter filter3 = new BlankFilter("filtro3");
                    Painter painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
                    Painter painterPoint = new Painter(currentFile.getName(), Painter.Type.PointToPoint);

                    filter1.setLastElement(read);
                    filter2.setLastElement(filter1);
                    filter3.setLastElement(filter2);
                    painterPoint.setLastElement(filter3);
                    painterTexture.setLastElement(filter3);

                    this.add(painterTexture.getInternalFrame());
                    this.add(painterPoint.getInternalFrame());
                    painterPoint.Update();
                    painterTexture.Update();


                }
                 else{
                    System.out.println("Error abriendo el archivo");
                 }

            }
        }


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewMain().setVisible(true);

             }
        });
    }

    





    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

   

}
