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

    //EStas aún no se han usado
    //ancho de la imagen
    private int width;
    //alto de la imagen
    private int height;
    //orientacion del imagen
    private int origen;
    
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

    // Variables propias del TIFF -> ya se han usado
    private int endian;
    private boolean bigEndian;
    private int isTiff;
    private int offset;

    public ReadTIFF(File fichero){

        System.out.println("Leyendo archivo TIFF");
      
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

            if (HeaderFormatRight()){


            // una imagen generica
            this.readImage = new ImageData(width, height, ImageData.ARRIBA_IZQ, 4, ImageData.PROF_U8, ImageData.ALINEADO_4, ImageData.YCBCR);
        }

    }


    private boolean HeaderFormatRight(){
        byte[] a_dataFile;




        // Revisa si el formato es big o little Endian y de acuerdo a eso funcionará bytesToInt
        a_dataFile = this.CutBytes(bytesFile, 0, 2);
        endian = this.BytesToInt(a_dataFile);
        if (endian == 0x4949)
                bigEndian = false;
        else
                bigEndian = true;

        // Siempre tiene que ser 42
        a_dataFile = this.CutBytes(bytesFile, 2, 4);
        isTiff = this.BytesToInt(a_dataFile);
        if (isTiff != 42){
            System.out.println("No es un archivo TIFF válido");
            return false;
        }

        //El offset del primer IFD
        a_dataFile = this.CutBytes(bytesFile, 4, 8);
        offset = this.BytesToInt(a_dataFile);
        System.out.println("offset " + offset);
        


        System.out.println("todo bien");

        return true;


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
         bigEndian = false;
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


