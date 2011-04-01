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



// Mensajes; TODO; Lo que hablamos en clase
        //equalizacion Jhon
//interpolación(rotate y sola(zoom))
        //crop - > listo el backbone: luego me ocupo de copiar los datos. Felipe
//rotate como filtro
        //Video. Epsilon
//guardar una lista de los filtros usados




package View;

import Filters.RGBtoXYZFilter;
import Filters.RGBtoHSLFilter;
import Filters.RGBtoYUVFilter;
import Filters.RGBtoHSVFilter;
import Filters.RGBtoYDbDrFilter;
import Filters.RGBtoCMYFilter;
import Filters.GrayScaleFilter;
//import Filters.SpaceColor.*;
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
import Operator.Histograma;
import Operator.PSNR;

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
     Painter draw;
//     TextureGL texture;
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
//        draw = new DrawGL();
//        this.add(draw);
//        texture = new TextureGL();
        Filtros.setEnabled(false);
        Compresor.setEnabled(false);
        Comparar.setEnabled(false);
        ModelosColor.setEnabled(false);
        Tranformadas.setEnabled(false);
//        this.add(draw);
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
        HistogramaRojo = new javax.swing.JFrame();
        Rojo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        HistogramaVerde = new javax.swing.JFrame();
        Verde = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        HistogramaAzul = new javax.swing.JFrame();
        Azul = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
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
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
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

        HistogramaRojo.setTitle("Histograma Rojo");
        HistogramaRojo.setMinimumSize(new java.awt.Dimension(276, 340));
        HistogramaRojo.setResizable(false);

        Rojo.setBackground(new java.awt.Color(255, 255, 255));
        Rojo.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout RojoLayout = new javax.swing.GroupLayout(Rojo);
        Rojo.setLayout(RojoLayout);
        RojoLayout.setHorizontalGroup(
            RojoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        RojoLayout.setVerticalGroup(
            RojoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jLabel2.setText("Histograma Rojo");

        javax.swing.GroupLayout HistogramaRojoLayout = new javax.swing.GroupLayout(HistogramaRojo.getContentPane());
        HistogramaRojo.getContentPane().setLayout(HistogramaRojoLayout);
        HistogramaRojoLayout.setHorizontalGroup(
            HistogramaRojoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaRojoLayout.createSequentialGroup()
                .addGroup(HistogramaRojoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistogramaRojoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Rojo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistogramaRojoLayout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HistogramaRojoLayout.setVerticalGroup(
            HistogramaRojoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaRojoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Rojo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HistogramaVerde.setTitle("Histograma Verde");
        HistogramaVerde.setMinimumSize(new java.awt.Dimension(276, 340));
        HistogramaVerde.setResizable(false);

        Verde.setBackground(new java.awt.Color(255, 255, 255));
        Verde.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout VerdeLayout = new javax.swing.GroupLayout(Verde);
        Verde.setLayout(VerdeLayout);
        VerdeLayout.setHorizontalGroup(
            VerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        VerdeLayout.setVerticalGroup(
            VerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jLabel3.setText("Histograma Verde");

        javax.swing.GroupLayout HistogramaVerdeLayout = new javax.swing.GroupLayout(HistogramaVerde.getContentPane());
        HistogramaVerde.getContentPane().setLayout(HistogramaVerdeLayout);
        HistogramaVerdeLayout.setHorizontalGroup(
            HistogramaVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaVerdeLayout.createSequentialGroup()
                .addGroup(HistogramaVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistogramaVerdeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Verde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistogramaVerdeLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HistogramaVerdeLayout.setVerticalGroup(
            HistogramaVerdeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaVerdeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Verde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HistogramaAzul.setTitle("Histograma Azul");
        HistogramaAzul.setMinimumSize(new java.awt.Dimension(276, 340));
        HistogramaAzul.setResizable(false);

        Azul.setBackground(new java.awt.Color(255, 255, 255));
        Azul.setPreferredSize(new java.awt.Dimension(256, 256));

        javax.swing.GroupLayout AzulLayout = new javax.swing.GroupLayout(Azul);
        Azul.setLayout(AzulLayout);
        AzulLayout.setHorizontalGroup(
            AzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        AzulLayout.setVerticalGroup(
            AzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );

        jLabel4.setText("Histograma Azul");

        javax.swing.GroupLayout HistogramaAzulLayout = new javax.swing.GroupLayout(HistogramaAzul.getContentPane());
        HistogramaAzul.getContentPane().setLayout(HistogramaAzulLayout);
        HistogramaAzulLayout.setHorizontalGroup(
            HistogramaAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaAzulLayout.createSequentialGroup()
                .addGroup(HistogramaAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistogramaAzulLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Azul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistogramaAzulLayout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HistogramaAzulLayout.setVerticalGroup(
            HistogramaAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaAzulLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Azul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jMenuItem4.setText("Histogramas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        Tranformadas.add(jMenuItem4);

        jMenuItem5.setText("Crop");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        Tranformadas.add(jMenuItem5);

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
                    read = new FileReader(currentFile);
                    painterTexture = new Painter(currentFile.getName(), Painter.Type.Texture);
                    painterTexture.setLastElement(read);
                    // Anade la ventana al frame principal
                    this.add(painterTexture.getInternalFrame());
                    painterTexture.Update();
                 }
                 else{
                    System.out.println("Error abriendo el archivo");
                 }

            }
        }


    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void RadioFilterGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterGActionPerformed
        ChannelFilter filter2 = new ChannelFilter("filtro canal",1);
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();

    }//GEN-LAST:event_RadioFilterGActionPerformed

    private void RadioFilterNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterNoneActionPerformed
        painterTexture.setLastElement(read);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterNoneActionPerformed

    private void RadioFilterRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterRActionPerformed
        ChannelFilter filter2 = new ChannelFilter("filtro canal",0);
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterRActionPerformed

    private void RadioFilterBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterBActionPerformed
        ChannelFilter filter2 = new ChannelFilter("filtro canal",2);
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterBActionPerformed

    private void RadioFilterGrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterGrayActionPerformed
        GrayScaleFilter filter2 = new GrayScaleFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterGrayActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
//        LZW l = new LZW(read.readImage());
//        l.CompressionLZW();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void RadioFilterYUVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYUVActionPerformed
        RGBtoYUVFilter filter2 = new RGBtoYUVFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYUVActionPerformed

    private void ModelosColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModelosColorActionPerformed
      
    }//GEN-LAST:event_ModelosColorActionPerformed

    private void RadioFilterHSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSVActionPerformed
        RGBtoHSVFilter filter2 = new RGBtoHSVFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSVActionPerformed

    private void TranformadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TranformadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TranformadasActionPerformed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        RGBtoYUVFilter yuv = new RGBtoYUVFilter("filtro canal");
        yuv.setLastElement(read);
        DCTFilter8x8 dct = new DCTFilter8x8("filtro canal",1);
        dct.setLastElement(yuv);
        painterTexture.setLastElement(dct);
        painterTexture.Update();
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void RadioFilterCMYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterCMYActionPerformed
        RGBtoCMYFilter filter2 = new RGBtoCMYFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterCMYActionPerformed

    private void RadioFilterHSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSLActionPerformed
        RGBtoHSLFilter filter2 = new RGBtoHSLFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSLActionPerformed

    private void RadioFlterYDbDrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFlterYDbDrActionPerformed
        RGBtoYDbDrFilter filter2 = new RGBtoYDbDrFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFlterYDbDrActionPerformed

    private void RadioFilterXYZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterXYZActionPerformed
        RGBtoXYZFilter filter2 = new RGBtoXYZFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterXYZActionPerformed

    private void RadioFilterYIQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYIQActionPerformed
        RGBtoYIQFilter filter2 = new RGBtoYIQFilter("filtro canal");
        filter2.setLastElement(read);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYIQActionPerformed

    private void RadioFilterLMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterLMSActionPerformed
        RGBtoLMSFilter filter2 = new RGBtoLMSFilter("filtro canal");
        filter2.setLastElement(read);
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
// rotation
        this.remove(painterTexture.getInternalFrame());
        if(draw!=null)
            this.remove(draw.pointPainter);
        draw = new Painter(currentFile.getName(), Painter.Type.PointToPoint);
        draw.pointPainter.DrawGLInit(read.readImage(),"",true,0);
        this.add(draw.pointPainter);
        draw.pointPainter.setVisible(true);
//        System.out.println("Rotando");
//        RotateFilter rotate = new RotateFilter("Rotation with texture",90);
//        rotate.setLastElement(read);
////        RGBtoLMSFilter filter2 = new RGBtoLMSFilter("filtro canal");
////        filter2.setLastElement(read);
//        painterTexture.setLastElement(rotate);
//        System.out.println("antesd del update");
//        painterTexture.Update();
    }//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
//      Grados.setText(String.valueOf(jSlider1.getValue()));
    }//GEN-LAST:event_jSlider1StateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//    this.remove(painterTexture.getInternalFrame());
//    this.remove(draw);
//    draw = new DrawGL();
//    draw.DrawGLInit(read.readImage(),"",true,jSlider1.getValue());
//    this.add(draw);
//    draw.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
    Rojo.removeAll();
    Verde.removeAll();
    Azul.removeAll();
    Lienzo rojo = new Lienzo();
    Lienzo verde = new Lienzo();
    Lienzo azul = new Lienzo();
    String mensaje="";
    Histograma h = new Histograma(read.readImage(),0);
    rojo.color(0);
    mensaje+=("1 "+h.mayor());
    rojo.setMayor(h.mayor());
    rojo.getDatos(h.getHist());
    Rojo.add(rojo);
    rojo.repaint();
    HistogramaRojo.setVisible(true);
    
    Histograma h1 = new Histograma(read.readImage(),1);
    verde.color(1);
    verde.setMayor(h1.mayor());
    mensaje+=("  2 "+h1.mayor());
    verde.getDatos(h1.getHist());
    
    Verde.add(verde);
    verde.repaint();
    HistogramaVerde.setVisible(true);
    System.out.println();
    Histograma h2 = new Histograma(read.readImage(),2);
    azul.color(2);
    azul.setMayor(h2.mayor());
    mensaje+=("   3 "+h2.mayor());
    azul.getDatos(h2.getHist());
    
    Azul.add(azul);

    azul.repaint();
    HistogramaAzul.setVisible(true);
    System.out.println(mensaje);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        System.out.println("Para seleccionar una región utilize la tecla Ctrl y arraste el puntero");
        CropFilter crop = new CropFilter("Filtro seleccionar región");
        crop.setLastElement(read);
        painterTexture.setLastElement(crop);
        painterTexture.Update();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
    private javax.swing.JPanel Azul;
    private javax.swing.JMenu Comparar;
    private javax.swing.JMenu Compresor;
    private javax.swing.ButtonGroup FiltorsModelos;
    private javax.swing.JMenu Filtros;
    private javax.swing.ButtonGroup FiltrosBase;
    private javax.swing.JLabel Grados;
    private javax.swing.JFrame HistogramaAzul;
    private javax.swing.JFrame HistogramaRojo;
    private javax.swing.JFrame HistogramaVerde;
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
    private javax.swing.JPanel Rojo;
    private javax.swing.JFrame RotarFrame;
    private javax.swing.JMenu Tranformadas;
    private javax.swing.JPanel Verde;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

   

}
