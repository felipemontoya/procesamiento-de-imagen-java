/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * VisorMain.java
 *
 * Created on 10-feb-2011, 18:41:14
 */

package Visor;

import java.io.IOException;
import javax.swing.*;
import java.io.File;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PrintStream;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import org.omg.PortableInterceptor.USER_EXCEPTION;


/**
 *
 * @author Jhon & Felipe
 */
public class VisorMain extends javax.swing.JFrame {

    private final PipedInputStream pin=new PipedInputStream();
    private final PipedInputStream pin2=new PipedInputStream();


    // Se encarga de escribir los out.println en el programa
    OutputStream out = new OutputStream()
        {
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

    private void ConfigurarAreaMensajes() {
        System.setOut(new PrintStream(out, true));
        jTextArea1.setEditable(false);
        System.out.println("Mensajes:");
    }
    private void ConfigurarEstiloNativo() {
       try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
           System.out.println("No se puede configurar el estilo nativo del sistema operativo: " + ex);
        }
    }
    /** Creates new form VisorMain */
    public VisorMain() {
        initComponents();
        this.ConfigurarAreaMensajes();
        this.ConfigurarEstiloNativo();
        this.setExtendedState(this.getExtendedState()|VisorMain.MAXIMIZED_BOTH);
        this.setTitle("MegaVisor - Procesamiento de Imagenes - Unal");
                //Se encarga de ambientar la aplicación en el estilo del sistema operativo

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
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        boolean seguro = false;

        if(hayArchivoActual){
            JOptionPane optionPane = new JOptionPane(
                "Dialogo\n",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);
            int showConfirmDialog = JOptionPane.showConfirmDialog(optionPane, "Ya hay un archivo abierto. ¿Desea continuar?");
           if ( showConfirmDialog == JOptionPane.YES_OPTION){
               seguro = true;
           }

        }

        if (!hayArchivoActual || seguro){
            JFileChooser dialogoAbrir = new JFileChooser(System.getProperty("user.dir"));

            FileFilter filterImagen = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "bmp");

            dialogoAbrir.addChoosableFileFilter(filterImagen);

            int result = dialogoAbrir.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                archivoActual = dialogoAbrir.getSelectedFile();

                if (archivoActual.canRead() && archivoActual.canWrite() && archivoActual.exists()){
                    System.out.println("Archivo seleccionado: " + archivoActual.getAbsolutePath());
                    VisorMain.getFrames()[0].setTitle("MegaVisor - Procesamiento de Imágenes - Unal : \\"+archivoActual.getName());
                    hayArchivoActual = true;


                    LectorArchivo lector = new LectorArchivo(archivoActual);
                    MegaImagen imagen = lector.leerImagen();

                    imagen.informacion();



                   // ImageReader iReader = new ImageReader();
                   // iReader.getImageData(archivoActual);
                    //System.out.println(imagen.getBpp());
                   // System.out.println(imagen.getHeight());
                    //System.out.println(imagen.getWidth());
                    //FileInputStream   con esto se lee el archivo paso a paso
                    //Se necesita una clase lectora de archivos
                    //Se necesita una clase imagen, para almacenar la imagen en memoria local para trabajar
                    //La clase lectora tambien puede ser escritora

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
                new VisorMain().setVisible(true);
             }
        });
    }

    // Variables del manejo de archivo

     private boolean hayArchivoActual = false;
     private File archivoActual;


    // Variables del manejo de imagen





    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
