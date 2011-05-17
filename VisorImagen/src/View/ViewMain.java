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
                                                             //equalizacion->listo .Jhon
//interpolación(rotate y sola(zoom))
                                                             //crop - > listo. Felipe
//rotate como filtro
//Video. Epsilon
                                                             //guardar una lista de los filtros usados -> listo .Felipe
//corregir algo raro que tiene el HSL




package View;

import Filters.RGBtoXYZFilter;
import Filters.RGBtoHSLFilter;
import Filters.RGBtoYUVFilter;
//import Filters.RGBtoHSVFilter;
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
import PipeLine.PipeMessage;
import PipeLine.PipeObject;

/**
 *
 * @author Jhon & Felipe
 */import Readers.Jpeg;

import Test_Compressor.LZW;
import java.util.ArrayList;


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

//     ArrayList<PipeObject> currentPipe = new ArrayList<PipeObject>();
     //pintor
     Painter draw;
     LienzoBW bw;
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
//       jMenu1.setDefaultLightWeightPopupEnabled(false);
       
    }

    /** Creates new form VisorMain */
    public ViewMain() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}
        System.setProperty("sun.awt.noerasebackground", "true");
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
        HistogramaColor = new javax.swing.JFrame();
        Color = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        Color5 = new javax.swing.JPanel();
        Color6 = new javax.swing.JPanel();
        HistogramaColor1 = new javax.swing.JFrame();
        Color1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Color3 = new javax.swing.JPanel();
        Color4 = new javax.swing.JPanel();
        Interpolar = new javax.swing.JFrame();
        EjeX = new javax.swing.JTextField();
        EjeY = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        Binary = new javax.swing.JFrame();
        WhiteBlack = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Level = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        Filtros = new javax.swing.JMenu();
        RadioFilterNone = new javax.swing.JRadioButtonMenuItem();
        RadioFilterG = new javax.swing.JRadioButtonMenuItem();
        RadioFilterR = new javax.swing.JRadioButtonMenuItem();
        RadioFilterB = new javax.swing.JRadioButtonMenuItem();
        RadioFilterGray = new javax.swing.JRadioButtonMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
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
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        Compresor = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        Tranformadas = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
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

        HistogramaColor.setTitle("Histograma Colores");
        HistogramaColor.setMinimumSize(new java.awt.Dimension(900, 340));
        HistogramaColor.setResizable(false);

        Color.setBackground(new java.awt.Color(255, 255, 255));
        Color.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout ColorLayout = new javax.swing.GroupLayout(Color);
        Color.setLayout(ColorLayout);
        ColorLayout.setHorizontalGroup(
            ColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        ColorLayout.setVerticalGroup(
            ColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jLabel2.setText("Histograma Colores");

        Color5.setBackground(new java.awt.Color(255, 255, 255));
        Color5.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout Color5Layout = new javax.swing.GroupLayout(Color5);
        Color5.setLayout(Color5Layout);
        Color5Layout.setHorizontalGroup(
            Color5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        Color5Layout.setVerticalGroup(
            Color5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        Color6.setBackground(new java.awt.Color(255, 255, 255));
        Color6.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout Color6Layout = new javax.swing.GroupLayout(Color6);
        Color6.setLayout(Color6Layout);
        Color6Layout.setHorizontalGroup(
            Color6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        Color6Layout.setVerticalGroup(
            Color6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout HistogramaColorLayout = new javax.swing.GroupLayout(HistogramaColor.getContentPane());
        HistogramaColor.getContentPane().setLayout(HistogramaColorLayout);
        HistogramaColorLayout.setHorizontalGroup(
            HistogramaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaColorLayout.createSequentialGroup()
                .addGroup(HistogramaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistogramaColorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Color5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Color6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistogramaColorLayout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HistogramaColorLayout.setVerticalGroup(
            HistogramaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaColorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HistogramaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Color6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Color5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HistogramaColor1.setTitle("Histograma Colores");
        HistogramaColor1.setMinimumSize(new java.awt.Dimension(911, 340));
        HistogramaColor1.setResizable(false);

        Color1.setBackground(new java.awt.Color(255, 255, 255));
        Color1.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout Color1Layout = new javax.swing.GroupLayout(Color1);
        Color1.setLayout(Color1Layout);
        Color1Layout.setHorizontalGroup(
            Color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        Color1Layout.setVerticalGroup(
            Color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jLabel3.setText("Histograma Colores Ecualizado");

        Color3.setBackground(new java.awt.Color(255, 255, 255));
        Color3.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout Color3Layout = new javax.swing.GroupLayout(Color3);
        Color3.setLayout(Color3Layout);
        Color3Layout.setHorizontalGroup(
            Color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        Color3Layout.setVerticalGroup(
            Color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        Color4.setBackground(new java.awt.Color(255, 255, 255));
        Color4.setPreferredSize(new java.awt.Dimension(257, 257));

        javax.swing.GroupLayout Color4Layout = new javax.swing.GroupLayout(Color4);
        Color4.setLayout(Color4Layout);
        Color4Layout.setHorizontalGroup(
            Color4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );
        Color4Layout.setVerticalGroup(
            Color4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout HistogramaColor1Layout = new javax.swing.GroupLayout(HistogramaColor1.getContentPane());
        HistogramaColor1.getContentPane().setLayout(HistogramaColor1Layout);
        HistogramaColor1Layout.setHorizontalGroup(
            HistogramaColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaColor1Layout.createSequentialGroup()
                .addGroup(HistogramaColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HistogramaColor1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Color4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HistogramaColor1Layout.createSequentialGroup()
                        .addGap(329, 329, 329)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HistogramaColor1Layout.setVerticalGroup(
            HistogramaColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistogramaColor1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HistogramaColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Color4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Interpolar.setTitle("Interpolar");
        Interpolar.setMinimumSize(new java.awt.Dimension(205, 120));
        Interpolar.setResizable(false);

        EjeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EjeYActionPerformed(evt);
            }
        });

        jLabel4.setText("Eje X:");

        jLabel5.setText("Eje Y:");

        jButton3.setText("Interpolar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout InterpolarLayout = new javax.swing.GroupLayout(Interpolar.getContentPane());
        Interpolar.getContentPane().setLayout(InterpolarLayout);
        InterpolarLayout.setHorizontalGroup(
            InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterpolarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(20, 20, 20)
                .addGroup(InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(EjeY, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(EjeX, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                .addContainerGap())
        );
        InterpolarLayout.setVerticalGroup(
            InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InterpolarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EjeX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InterpolarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EjeY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menu1.setLabel("File");
        menu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu1ActionPerformed(evt);
            }
        });
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        Binary.setMinimumSize(new java.awt.Dimension(320, 380));

        WhiteBlack.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout WhiteBlackLayout = new javax.swing.GroupLayout(WhiteBlack);
        WhiteBlack.setLayout(WhiteBlackLayout);
        WhiteBlackLayout.setHorizontalGroup(
            WhiteBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );
        WhiteBlackLayout.setVerticalGroup(
            WhiteBlackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );

        jLabel6.setText("Nivel:");

        Level.setText("128");

        jSlider2.setMaximum(255);
        jSlider2.setValue(128);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        javax.swing.GroupLayout BinaryLayout = new javax.swing.GroupLayout(Binary.getContentPane());
        Binary.getContentPane().setLayout(BinaryLayout);
        BinaryLayout.setHorizontalGroup(
            BinaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BinaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BinaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BinaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(WhiteBlack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BinaryLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(90, 90, 90)))
                    .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        BinaryLayout.setVerticalGroup(
            BinaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BinaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WhiteBlack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BinaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setPreferredSize(new java.awt.Dimension(60, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Crop", "Rotate", "Gamma", "Channel" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton2.setText("Enviar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField2.setPreferredSize(new java.awt.Dimension(60, 20));

        jTextField3.setPreferredSize(new java.awt.Dimension(60, 20));

        jTextField4.setPreferredSize(new java.awt.Dimension(60, 20));
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem11.setText("Guardar");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Editar");

        jMenuItem6.setText("Remover último filtro");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

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

        jMenuItem12.setText("Sobel");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        Filtros.add(jMenuItem12);

        jMenuItem13.setText("Binary Threshold");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        Filtros.add(jMenuItem13);

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

        jMenu4.setText("YUV to");

        jMenuItem10.setText("RGB");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        ModelosColor.add(jMenu4);

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

        jMenuItem9.setText("IDCT");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        Tranformadas.add(jMenuItem9);

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

        jMenuItem8.setText("Interpolar");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        Tranformadas.add(jMenuItem8);

        jMenuItem7.setText("Gamma");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        Tranformadas.add(jMenuItem7);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //variable que indica que desea abrir un archivo
        boolean a_isSafe = false;
//        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
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
//                    currentPipe.add(read);
//                    currentPipe.add(painterTexture);
                    this.jDesktopPane1.add(painterTexture.getInternalFrame(),-1);
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
        filter2.setLastElement(painterTexture.getLastElement());
//        currentPipe.get(currentPipe.size()-2);
        painterTexture.setLastElement(filter2);
        painterTexture.Update();

    }//GEN-LAST:event_RadioFilterGActionPerformed

    private void RadioFilterNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterNoneActionPerformed
        painterTexture.setLastElement(read);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterNoneActionPerformed

    private void RadioFilterRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterRActionPerformed
        ChannelFilter filter2 = new ChannelFilter("filtro canal",0);
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterRActionPerformed

    private void RadioFilterBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterBActionPerformed
        ChannelFilter filter2 = new ChannelFilter("filtro canal",2);
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterBActionPerformed

    private void RadioFilterGrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterGrayActionPerformed
        GrayScaleFilter filter2 = new GrayScaleFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterGrayActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
//        LZW l = new LZW(read.readImage());
//        l.CompressionLZW();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void RadioFilterYUVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYUVActionPerformed
        RGBtoYUVFilter filter2 = new RGBtoYUVFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYUVActionPerformed

    private void ModelosColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModelosColorActionPerformed
      
    }//GEN-LAST:event_ModelosColorActionPerformed

    private void RadioFilterHSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSVActionPerformed
//        RGBtoHSVFilter filter2 = new RGBtoHSVFilter("filtro canal");
//        filter2.setLastElement(painterTexture.getLastElement());
//        painterTexture.setLastElement(filter2);
//        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSVActionPerformed

    private void TranformadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TranformadasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TranformadasActionPerformed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        RGBtoYUVFilter yuv = new RGBtoYUVFilter("filtro canal");
        yuv.setLastElement(painterTexture.getLastElement());
        DCTFilter8x8 dct = new DCTFilter8x8("filtro canal",1);
        dct.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(dct);
        painterTexture.Update();
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void RadioFilterCMYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterCMYActionPerformed
        RGBtoCMYFilter filter2 = new RGBtoCMYFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterCMYActionPerformed

    private void RadioFilterHSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterHSLActionPerformed
        RGBtoHSLFilter filter2 = new RGBtoHSLFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterHSLActionPerformed

    private void RadioFlterYDbDrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFlterYDbDrActionPerformed
        RGBtoYDbDrFilter filter2 = new RGBtoYDbDrFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFlterYDbDrActionPerformed

    private void RadioFilterXYZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterXYZActionPerformed
        RGBtoXYZFilter filter2 = new RGBtoXYZFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterXYZActionPerformed

    private void RadioFilterYIQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterYIQActionPerformed
        RGBtoYIQFilter filter2 = new RGBtoYIQFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(filter2);
        painterTexture.Update();
    }//GEN-LAST:event_RadioFilterYIQActionPerformed

    private void RadioFilterLMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioFilterLMSActionPerformed
        RGBtoLMSFilter filter2 = new RGBtoLMSFilter("filtro canal");
        filter2.setLastElement(painterTexture.getLastElement());
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
                    this.jDesktopPane1.add(p.getInternalFrame());
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
//        painterTexture.getInternalFrame().setVisible(false);
//        if(draw!=null)
//            this.remove(draw.pointPainter);
//        draw = new Painter(currentFile.getName(), Painter.Type.PointToPoint);
//        draw.pointPainter.DrawGLInit(painterTexture.getLastElement().getDataOut().getImageData(),"",true,0);
//        this.add(draw.pointPainter);
//        draw.pointPainter.setVisible(true);
//        System.out.println("Rotando");
        RotateFilter rotate = new RotateFilter("Rotation with texture");
        rotate.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(rotate);
        painterTexture.Update();
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
    Color.removeAll();
    Color1.removeAll();
    Color3.removeAll();
    Color4.removeAll();
    Color5.removeAll();
    Color6.removeAll();
    int[] vacio = new int[256];
        for(int i=0;i<256;i++)
            vacio[i]=0;

    Lienzo rojo = new Lienzo();
    Lienzo verde = new Lienzo();
    Lienzo azul = new Lienzo();
    Lienzo acumulado;
    Lienzo equalR;
    Lienzo equalG;
    Lienzo equalB;

    Histograma h = new Histograma(painterTexture.getLastElement().getDataOut().getImageData(),0);
    Histograma h1 = new Histograma(painterTexture.getLastElement().getDataOut().getImageData(),1);
    Histograma h2 = new Histograma(painterTexture.getLastElement().getDataOut().getImageData(),2);

    rojo.color(0);
    rojo.setMayor(25);
    rojo.getDatos(h.getHist());
    Color.add(rojo);
    rojo.repaint();

    verde.color(1);
    verde.setMayor(25);
    verde.getDatos(h1.getHist());
    Color5.add(verde);
    verde.repaint();

    azul.color(2);
    azul.setMayor(25);
    azul.getDatos(h2.getHist());
    Color6.add(azul);
    azul.repaint();

    HistogramaColor.setVisible(true);
    EqualizationFilter filter2 = new  EqualizationFilter("filtro canal",h,h1,h2);
    Painter painterTexture1 = new Painter(currentFile.getName(), Painter.Type.Texture);
    filter2.setLastElement(painterTexture.getLastElement());
    this.jDesktopPane1.add(painterTexture1.getInternalFrame());
    painterTexture1.setLastElement(filter2);
    painterTexture1.Update();

    
    equalR=filter2.getEqualR();
    Color1.add(equalR);
    equalR.repaint();

    equalG=filter2.getEqualG();
    Color3.add(equalG);
    equalG.repaint();

    equalB=filter2.getEqualB();
    Color4.add(equalB);
    equalB.repaint();

    HistogramaColor1.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        System.out.println("Para seleccionar una región utilize la tecla Ctrl y arraste el puntero. Cuando termine oprima Ctrl + el boton derecho");
        CropFilter crop = new CropFilter("Filtro seleccionar región");
        crop.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(crop);
        painterTexture.Update();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // Remover último filtro del pipeline:
        if (painterTexture.getLastElement().getType()!=PipeObject.Type.source)
        {
            PipeObject t_obj = painterTexture.getLastElement().getLastElement();
            painterTexture.setLastElement(t_obj);
        }
        painterTexture.Update();
        painterTexture.getInternalFrame().setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        GammaFilter gamma = new GammaFilter("Filtro Gamma");
        gamma.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(gamma);
        painterTexture.Update();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selected = jComboBox1.getSelectedIndex();
        double value = Double.parseDouble(jTextField1.getText());

        switch(selected)
        {
        
            case 2:
                PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Gamma,"Written message");
                msg.dValue1 = value;
                painterTexture.PassMessage(msg);
                painterTexture.Update();
                break;
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void EjeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EjeYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EjeYActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    Interpolar.setVisible(true);
    EjeX.setText(String.valueOf(painterTexture.getLastElement().getDataOut().getImageData().getWidth()));
    EjeY.setText(String.valueOf(painterTexture.getLastElement().getDataOut().getImageData().getHeight()));
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int x = Integer.parseInt(EjeX.getText().trim());
        int y = Integer.parseInt(EjeY.getText().trim());
        InterpolateFilter gamma = new InterpolateFilter("Filtro Interpolar",x,y);
        gamma.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(gamma);
        painterTexture.Update();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
      IDCTFilter8x8 idct = new IDCTFilter8x8("Filtro IDCT");
        idct.setLastElement(painterTexture.getLastElement());
        YUVtoRGBFilter y = new YUVtoRGBFilter("Filtro YIQ");
        y.setLastElement(idct);
        painterTexture.setLastElement(idct);
        painterTexture.Update();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
         YUVtoRGBFilter y = new YUVtoRGBFilter("Filtro YIQ");
        y.setLastElement(painterTexture.getLastElement());
        painterTexture.setLastElement(y);
        painterTexture.Update();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
    JFileChooser chooser = new JFileChooser();
    chooser.showSaveDialog(null);
    File f = chooser.getSelectedFile();
    String p = f.getAbsolutePath()+".jpg";
    System.out.println("Guardando archivo jpg en: "+p);
    Jpeg j = new Jpeg();
    j.createJPG(painterTexture.getImageData(), p);
    
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        GrayScaleFilter gray = new GrayScaleFilter("Gray antes de sobel");
        SobelFilter sobel = new SobelFilter("Filtro sobel");
        gray.setLastElement(painterTexture.getLastElement());
        sobel.setLastElement(gray);
        painterTexture.setLastElement(sobel);
        painterTexture.Update();      

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menu1ActionPerformed

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
       Level.setText(String.valueOf(jSlider2.getValue()));
        PipeMessage msg = new PipeMessage(PipeMessage.Receiver.Binary,"Gamma");
            msg.iValue1 = jSlider2.getValue();
            bw.setpos(jSlider2.getValue());
            bw.repaint();
            painterTexture.PassMessage(msg);
            painterTexture.Update();
    }//GEN-LAST:event_jSlider2StateChanged

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        WhiteBlack.removeAll();
        GrayScaleFilter gray = new GrayScaleFilter("Gray antes de sobel");
        BinaryThresholdFilter binary = new BinaryThresholdFilter("Binary Filter", 128);
        gray.setLastElement(painterTexture.getLastElement());
        binary.setLastElement(gray);
        painterTexture.setLastElement(binary);
        painterTexture.Update();
        bw = new LienzoBW(128);
         Histograma h = new Histograma(painterTexture.getLastElement().getLastElement().getDataOut().getImageData(),0);
        bw.getDatos(h.getHist());
        WhiteBlack.add(bw);
        
        Binary.setVisible(true);
        bw.repaint();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JPopupMenu.setDefaultLightWeightPopupEnabled(false);
                new ViewMain().setVisible(true);

             }
        });
    }

    





    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame Binary;
    private javax.swing.JPanel Color;
    private javax.swing.JPanel Color1;
    private javax.swing.JPanel Color3;
    private javax.swing.JPanel Color4;
    private javax.swing.JPanel Color5;
    private javax.swing.JPanel Color6;
    private javax.swing.JMenu Comparar;
    private javax.swing.JMenu Compresor;
    private javax.swing.JTextField EjeX;
    private javax.swing.JTextField EjeY;
    private javax.swing.ButtonGroup FiltorsModelos;
    private javax.swing.JMenu Filtros;
    private javax.swing.ButtonGroup FiltrosBase;
    private javax.swing.JLabel Grados;
    private javax.swing.JFrame HistogramaColor;
    private javax.swing.JFrame HistogramaColor1;
    private javax.swing.JFrame Interpolar;
    private javax.swing.JLabel Level;
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
    private javax.swing.JPanel WhiteBlack;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables

   

}
