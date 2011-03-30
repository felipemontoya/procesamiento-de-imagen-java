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
import Filters.RGBtoYIQFilter;
import PSNR.PSNR;

/**
 *
 * @author Jhon & Felipe
 */import Test_Compressor.LZW;


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
    FileReader read;
     private boolean isFileOpen = false;
     private File currentFile;
     Painter painterTexture;

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
        Filtros.setEnabled(false);
        Compresor.setEnabled(false);
        Comparar.setEnabled(false);
        ModelosColor.setEnabled(false);
        Tranformadas.setEnabled(false);
        this.add(draw);
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

        FiltrosBase = new javax.swing.ButtonGroup();
        FiltorsModelos = new javax.swing.ButtonGroup();
        RotarFrame = new javax.swing.JFrame();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Grados = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        Filtros = new javax.swing.JMenu();
        RadioFilterNone = new javax.swing.JRadioButtonMenuItem();
        RadioFilterG = new javax.swing.JRadioButtonMenuItem();
        RadioFilterR = new javax.swing.JRadioButtonMenuItem();
        RadioFilterB = new javax.swing.JRadioButtonMenuItem();
        RadioFilterGray = new javax.swing.JRadioButtonMenuItem();
        ModelosColor = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        RadioFilterYUV = new javax.swing.JRadioButtonMenuItem();
        RadioFilterHSV = new javax.swing.JRadioButtonMenuItem();
        RadioFilterYIQ = new javax.swing.JRadioButtonMenuItem();
        RadioFilterHSL = new javax.swing.JRadioButtonMenuItem();
        RadioFilterCMY = new javax.swing.JRadioButtonMenuItem();
        RadioFilterXYZ = new javax.swing.JRadioButtonMenuItem();
        RadioFilterLMS = new javax.swing.JRadioButtonMenuItem();
        RadioFlterYDbDr = new javax.swing.JRadioButtonMenuItem();
        Compresor = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        Tranformadas = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        Comparar = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        RotarFrame.setMinimumSize(new java.awt.Dimension(311, 100));
        RotarFrame.setResizable(false);

        jSlider1.setMaximum(360);
        jSlider1.setMinimum(-360);
        jSlider1.setValue(0);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jButton1.setText("Rotar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Grados: ");

        Grados.setText("0");

        javax.swing.GroupLayout RotarFrameLayout = new javax.swing.GroupLayout(RotarFrame.getContentPane());
        RotarFrame.getContentPane().setLayout(RotarFrameLayout);
        RotarFrameLayout.setHorizontalGroup(
            RotarFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RotarFrameLayout.createSequentialGroup()
                .addGroup(RotarFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RotarFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(RotarFrameLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(Grados)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        RotarFrameLayout.setVerticalGroup(
            RotarFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RotarFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RotarFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addGroup(RotarFrameLayout.createSequentialGroup()
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(RotarFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(Grados))))
                .addContainerGap())
        );

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

        Filtros.setText("Filtros");

        FiltrosBase.add(RadioFilterNone);
        RadioFilterNone.setSelected(true);
        RadioFilterNone.setText("Ninguno");
        RadioFilterNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterNoneActionPerformed(evt);
            }
        });
        Filtros.add(RadioFilterNone);

        FiltrosBase.add(RadioFilterG);
        RadioFilterG.setText("Verde");
        RadioFilterG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterGActionPerformed(evt);
            }
        });
        Filtros.add(RadioFilterG);

        FiltrosBase.add(RadioFilterR);
        RadioFilterR.setText("Rojo");
        RadioFilterR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterRActionPerformed(evt);
            }
        });
        Filtros.add(RadioFilterR);

        FiltrosBase.add(RadioFilterB);
        RadioFilterB.setText("Azul");
        RadioFilterB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterBActionPerformed(evt);
            }
        });
        Filtros.add(RadioFilterB);

        FiltrosBase.add(RadioFilterGray);
        RadioFilterGray.setText("Escala de Gris");
        RadioFilterGray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterGrayActionPerformed(evt);
            }
        });
        Filtros.add(RadioFilterGray);

        jMenuBar1.add(Filtros);

        ModelosColor.setText("Modelos Color");
        ModelosColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModelosColorActionPerformed(evt);
            }
        });

        jMenu2.setText("RGB to");

        FiltorsModelos.add(RadioFilterYUV);
        RadioFilterYUV.setText("YUV");
        RadioFilterYUV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterYUVActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterYUV);

        FiltorsModelos.add(RadioFilterHSV);
        RadioFilterHSV.setText("HSV");
        RadioFilterHSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterHSVActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterHSV);

        FiltorsModelos.add(RadioFilterYIQ);
        RadioFilterYIQ.setText("YIQ");
        RadioFilterYIQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterYIQActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterYIQ);

        FiltorsModelos.add(RadioFilterHSL);
        RadioFilterHSL.setText("HSL");
        RadioFilterHSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterHSLActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterHSL);

        FiltorsModelos.add(RadioFilterCMY);
        RadioFilterCMY.setText("CMY");
        RadioFilterCMY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterCMYActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterCMY);

        FiltorsModelos.add(RadioFilterXYZ);
        RadioFilterXYZ.setText("XYZ");
        RadioFilterXYZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterXYZActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterXYZ);

        FiltorsModelos.add(RadioFilterLMS);
        RadioFilterLMS.setText("LMS");
        RadioFilterLMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFilterLMSActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFilterLMS);

        FiltorsModelos.add(RadioFlterYDbDr);
        RadioFlterYDbDr.setText("YDbDr");
        RadioFlterYDbDr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioFlterYDbDrActionPerformed(evt);
            }
        });
        jMenu2.add(RadioFlterYDbDr);

        ModelosColor.add(jMenu2);

        jMenuBar1.add(ModelosColor);

        Compresor.setText("Compresores");

        jMenuItem2.setText("LZW");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        Compresor.add(jMenuItem2);

        jMenuBar1.add(Compresor);

        Tranformadas.setText("Transformadas");
        Tranformadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TranformadasActionPerformed(evt);
            }
        });

        FiltorsModelos.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setText("DCT");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        Tranformadas.add(jRadioButtonMenuItem1);

        FiltorsModelos.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("Rotar");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        Tranformadas.add(jRadioButtonMenuItem2);

        jMenuBar1.add(Tranformadas);

        Comparar.setText("Comparar");

        jMenuItem3.setText("PSNR");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        Comparar.add(jMenuItem3);

        jMenuBar1.add(Comparar);

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
                .addContainerGap(323, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            FileFilter filterImage = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "bmp", "tif", "tiff");
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
                     Filtros.setEnabled(true);
                     Compresor.setEnabled(true);
                     Comparar.setEnabled(true);
                     ModelosColor.setEnabled(true);
                    Tranformadas.setEnabled(true);
                   //crea el lector de imagenes y le pasa el archivo actual.
                   //FileReader read = new FileReader(currentFile);
                     read = new FileReader(currentFile);
                   //Creación del pipeline

//                    BlankFilter filter1 = new BlankFilter("filtro1");
//                    ChannelFilter filter2 = new ChannelFilter("filtro canal",2);
//                    DCTFilter8x8 dct = new DCTFilter8x8("dct", 1);
//                    YUV filter3 = new BlankFilter("filtro3");
                   // Painter painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
                       painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);

                    //Painter painterPoint = new Painter(currentFile.getName(), Painter.Type.PointToPoint);
//                    RotateFilter a = new RotateFilter("rotar",0);
//                    filter1.setLastElement(read);
//                    dct.setLastElement(read);
//                    filter3.setLastElement(filter2);
//                    painterPoint.setLastElement(filter3);
//                    a.setLastElement(read);
                   painterTexture.setLastElement(read);
                       
//                    draw.DrawGLInit(read.readImage(),"");
//                    draw.setVisible(true);
                    this.add(painterTexture.getInternalFrame());
//                    this.add(painterPoint.getInternalFrame());
//                    painterPoint.Update();
                    painterTexture.Update();

                    

                }
                 else{
                    System.out.println("Error abriendo el archivo");
                 }

            }
        }


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void RadioFilterGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterGActionPerformed
        this.remove(painterTexture.getInternalFrame());
        ChannelFilter filter2 = new ChannelFilter("filtro canal",1);
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();

    }//GEN-LAST:event_RadioFilterGActionPerformed

    private void RadioFilterNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterNoneActionPerformed
       this.remove(painterTexture.getInternalFrame());
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        painterTexture.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
//      
       painterTexture.Update();
    }//GEN-LAST:event_RadioFilterNoneActionPerformed

    private void RadioFilterRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterRActionPerformed
       this.remove(painterTexture.getInternalFrame());
        ChannelFilter filter2 = new ChannelFilter("filtro canal",0);
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterRActionPerformed

    private void RadioFilterBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterBActionPerformed
        this.remove(painterTexture.getInternalFrame());
        ChannelFilter filter2 = new ChannelFilter("filtro canal",2);
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterBActionPerformed

    private void RadioFilterGrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterGrayActionPerformed
        this.remove(painterTexture.getInternalFrame());
        GrayScaleFilter filter2 = new GrayScaleFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterGrayActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        LZW l = new LZW(read.readImage());
        l.CompressionLZW();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void RadioFilterYUVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYUVActionPerformed
        this.remove(painterTexture.getInternalFrame());
        RGBtoYUVFilter filter2 = new RGBtoYUVFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYUVActionPerformed

    private void ModelosColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModelosColorActionPerformed
      
    }//GEN-LAST:event_ModelosColorActionPerformed

    private void RadioFilterHSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSVActionPerformed
       this.remove(painterTexture.getInternalFrame());
        RGBtoHSVFilter filter2 = new RGBtoHSVFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSVActionPerformed

    private void TranformadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TranformadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TranformadasActionPerformed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
      this.remove(painterTexture.getInternalFrame());
        
        RGBtoYUVFilter filter3 = new RGBtoYUVFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter3.setLastElement(read);


       DCTFilter filter2 = new DCTFilter("filtro canal",1);
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(filter3);
        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void RadioFilterCMYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterCMYActionPerformed
       this.remove(painterTexture.getInternalFrame());
        RGBtoCMYFilter filter2 = new RGBtoCMYFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterCMYActionPerformed

    private void RadioFilterHSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSLActionPerformed
        this.remove(painterTexture.getInternalFrame());
        RGBtoHSLFilter filter2 = new RGBtoHSLFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSLActionPerformed

    private void RadioFlterYDbDrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFlterYDbDrActionPerformed
       this.remove(painterTexture.getInternalFrame());
        RGBtoYDbDrFilter filter2 = new RGBtoYDbDrFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFlterYDbDrActionPerformed

    private void RadioFilterXYZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterXYZActionPerformed
       this.remove(painterTexture.getInternalFrame());
        RGBtoXYZFilter filter2 = new RGBtoXYZFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterXYZActionPerformed

    private void RadioFilterYIQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYIQActionPerformed
        this.remove(painterTexture.getInternalFrame());
        RGBtoYIQFilter filter2 = new RGBtoYIQFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYIQActionPerformed

    private void RadioFilterLMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterLMSActionPerformed
       this.remove(painterTexture.getInternalFrame());
        RGBtoLMSFilter filter2 = new RGBtoLMSFilter("filtro canal");
        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
        filter2.setLastElement(read);
//        ChannelFilter filter3 = new ChannelFilter("filtro canal",2);
//        painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
//        filter3.setLastElement(filter2);


        this.add(painterTexture.getInternalFrame());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterLMSActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        FileReader reader;
        Painter p;
        ImageData i,k;
        PSNR psnr;
        JFileChooser chooserFile = new JFileChooser(System.getProperty("user.dir"));
            //filtro para solo leer imagenes
            FileFilter filterImage = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "bmp", "tif", "tiff");
            chooserFile.addChoosableFileFilter(filterImage);
            //variable que nos indica que se puede abrir el archivo elegido
            int a_result = chooserFile.showOpenDialog(null);
            if(a_result == JFileChooser.APPROVE_OPTION){
                File currentFile = chooserFile.getSelectedFile();
                //impresion de los datos del archivo, modificacion del titulo
                if (currentFile.canRead() && currentFile.canWrite() && currentFile.exists()){
                reader = new FileReader(currentFile);
                i=read.readImage();
                k=reader.readImage();
                psnr=new PSNR(i,k);
                if(psnr.isDimentionEquals()){
                    p= new Painter(currentFile.getName(), Painter.Type.Texture);
                    p.setLastElement(reader);
                    this.add(p.getInternalFrame());
                    p.Update();
                    System.out.println("****************************");
                    System.out.println("PSNR: "+psnr.result());
                    System.out.println("****************************");
                    }
                else{
                   System.out.println("****************************");
                   System.out.println("Dimenciones de Imagen Distintas");
                   System.out.println("****************************");
                }

                }
            }
        
        
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
    RotarFrame.setVisible(true);
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
      Grados.setText(String.valueOf(jSlider1.getValue()));
    }//GEN-LAST:event_jSlider1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.remove(painterTexture.getInternalFrame());
    this.remove(draw);
    draw = new DrawGL();
    draw.DrawGLInit(read.readImage(),"",true,jSlider1.getValue());
    this.add(draw);
    draw.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JMenu Comparar;
    private javax.swing.JMenu Compresor;
    private javax.swing.ButtonGroup FiltorsModelos;
    private javax.swing.JMenu Filtros;
    private javax.swing.ButtonGroup FiltrosBase;
    private javax.swing.JLabel Grados;
    private javax.swing.JMenu ModelosColor;
    private javax.swing.JRadioButtonMenuItem RadioFilterB;
    private javax.swing.JRadioButtonMenuItem RadioFilterCMY;
    private javax.swing.JRadioButtonMenuItem RadioFilterG;
    private javax.swing.JRadioButtonMenuItem RadioFilterGray;
    private javax.swing.JRadioButtonMenuItem RadioFilterHSL;
    private javax.swing.JRadioButtonMenuItem RadioFilterHSV;
    private javax.swing.JRadioButtonMenuItem RadioFilterLMS;
    private javax.swing.JRadioButtonMenuItem RadioFilterNone;
    private javax.swing.JRadioButtonMenuItem RadioFilterR;
    private javax.swing.JRadioButtonMenuItem RadioFilterXYZ;
    private javax.swing.JRadioButtonMenuItem RadioFilterYIQ;
    private javax.swing.JRadioButtonMenuItem RadioFilterYUV;
    private javax.swing.JRadioButtonMenuItem RadioFlterYDbDr;
    private javax.swing.JFrame RotarFrame;
    private javax.swing.JMenu Tranformadas;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

   

}
