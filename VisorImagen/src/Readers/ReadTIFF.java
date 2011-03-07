/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/***************************************************************************************************************/
/*    Especificaciones de la Programacion
 *    Patron
 *    version 1.0
 *
 *    General:
 *          - Toda clase debe tener la informacion de esta especificacion.
 *          - Todo procedimiento/variable programado sera debidamente comentado dependiento de su naturaleza.
 *          - Todas las variables, clases y metodos deberan ser nombradas en ingles y dependiendo de su funcion.
 *    Paquetes:
 *          - Toda clase estara incluida en un paquete.
 *          - El nombre del paquete debera ser acorde a las clases contenidas en el.
 *          - Si son paquetes de prueba deben estar identificados iniciando con "test_nombredelpaquete".
 *          - Si son paquetes que seran utilizados temporalmente para ser mas tarde eliminador iran identificados
 *            iniciando con "temp_nombredelpaquete".
 *
 *    Clases:
 *          - Toda clase iniciara con la primera letra mayuscula, si el nombre esta compuesto por mas palabras,
 *            las siguientes palabras deberan iniciar tambien con la primera letra en mayucula ej: ClaseEjemplo
 *          - El nombre de la clase debe identificar su funcionalidad.
 *          - Antes iniciar la clase debe ser comentado para que sirve dicha clase y como es su constructura.
 *          - Si son clases de prueba iran identificados como "cTest_NombreClase"
 *
 *    Metodos
 *          - Toda metodo iniciara con la primera letra mayuscula, si el nombre esta compuesto por mas palabras,
 *            las siguientes palabras deberan iniciar tambien con la primera letra en mayucula ej: MetodoEjemplo
 *          - El nombre de el metodo debe identificar su funcionalidad. si el metodo es un get o un set ira en minuscula
 *          - Antes iniciar el metodo debe ser comentado para que sirve dicha clase, sus entradas y salidas si tiene
 *
 *    Variables
 *          - Todo nombre de varible inicia en minusculas, si el nombre de la varible esta compuesta por mas palabras
 *            las siguientes palabras deberan iniciar con la primera letra en mayucula ej: variableEjemplo.
 *          - El nombre de la variable debe tener sentido acorde a su valor y su utilidad.
 *          - si son variables auxiliares o temporal deben ir identificados como a_nombreVariable o t_nombreVariable.
 *          - las variables globales ya estan plenamente identificadas con color verde.
 *          - Cuando se declara una variable debe comentarse una breve funcionalidad de dicha variable.
 *
 *   lease y cumplase
 */
/***************************************************************************************************************/


package Readers;

import Data.ImageData;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Jhon
 */
public class ReadTIFF {
    private ImageData readImage;
    //los bytes del archivo leido
    private byte[] bytesFile;
    //ancho de la imagen
    private int width;
    //alto de la imagen
    private int height;
    //orientacion del imagen
    private int origen;
    //byte donde empieza la informacion de la imagen
    private int offset;
    //tipo de compresion BMP
    private int compresionBMP;
    //espacio de color
    private int spaceColor;
    //alineacion
    private int alineacion;
    //canales de la imagen RGB o RGBA
    private int nChanels;
    //bits por pixel
    private int depth;


    public ReadTIFF(File fichero){

        System.out.println("Leyendo archivo TIFF");
        // hacer aquí y en otros métodos de la clase todo el procesamiento que se necesite para convertir un archivo en el formato estandar de MegaImagen

        //FileInputStream flujoEntrada;
        BufferedInputStream flujoEntrada = null;
        ByteArrayOutputStream flujoSalida = new ByteArrayOutputStream();
        byte[] buffer;
        int leidos = 0;

        buffer = new byte[10 * 1024];
            try{
                //flujoEntrada = ;
                flujoEntrada = new BufferedInputStream(new FileInputStream(fichero));
                while ((leidos = flujoEntrada.read(buffer)) >= 0){
                    flujoSalida.write(buffer, 0, leidos);
                }
                bytesFile = flujoSalida.toByteArray();

                flujoSalida.reset();
                flujoSalida.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
                System.out.println("Error en el buffer de lectura del archivo");
            }
            finally{
                if (flujoEntrada != null){
                    try{
                      flujoEntrada.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error en el buffer de lectura del archivo");
                    }
                }
            }

            System.out.println("Length del byte array: " + bytesFile.length);


            //Por motivos de manejo de printSelf
            this.readImage = new ImageData(width, height, ImageData.ARRIBA_IZQ, 4, ImageData.PROF_U8, ImageData.ALINEADO_4, ImageData.YCBCR);


    }


    private boolean HeaderFormatRight(){
        byte[] a_dataFile;
        //para cada atributo de la imagen se obtiene los bytes de la imagen
        //y se transforman en enteros


        a_dataFile = this.CutBytes(bytesFile, 4, 8);
        offset = this.BytesToInt(a_dataFile);

        int i;
        for (i = offset;i<bytesFile.length;i++){
            if (bytesFile[i] == (byte)0x0 && bytesFile[i+1] == (byte)0x1 && bytesFile[i+3] == (byte)0x3){
            i+=7;
            break;
            }
        }

        a_dataFile = this.CutBytes(bytesFile, i, i+2);
        width = this.BytesToInt(a_dataFile);

        for (i = offset;i<bytesFile.length;i++){
            if (bytesFile[i] == (byte)0x1 && bytesFile[i+1] == (byte)0x1 && bytesFile[i+3] == (byte)0x3){
            i+=7;
            break;
            }
        }

        a_dataFile = this.CutBytes(bytesFile, i, i+2);
        height = this.BytesToInt(a_dataFile);

        origen = ImageData.ARRIBA_IZQ;
        spaceColor = ImageData.RGBA;
        alineacion = ImageData.ALINEADO_4;
        

        /*******************/
        //a_dataFile = this.CutBytes(bytesFile, 28, 30);
        nChanels = 3;//this.BytesToInt(a_dataFile)/8;

        depth = nChanels;
        if(depth>=3)
        return true;
        else
        return false;

    }


    public byte[] CutBytes(byte[] b,int i,int j){
        byte[] a = new byte[4];
        for(byte bb : a){
            bb=0;
        }
        int k=0;

        for(int ii = i;ii<j;ii++){
            a[k]=b[ii];
            k++;
        }

        return a;
    }

    public int BytesToInt(byte[] valor){
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

    

    public ImageData getImagenData(){
        return this.readImage;
    }


}


