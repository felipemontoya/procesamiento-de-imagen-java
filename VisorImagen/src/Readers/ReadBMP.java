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
 * @author Jhon & Felipe
 */
/* Clase: ReadBMP
 * lee archivos BMP y crea una clase ImageData con los datos de la imagen leida.
 *
 */
public class ReadBMP {
    //datos de la imagen en la clase
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



    public ReadBMP(File currentFile){
        System.out.println("Leyendo archivo BMP");
        //variables de lectura del archivo
        BufferedInputStream flowIn = null;
        ByteArrayOutputStream flowOut = new ByteArrayOutputStream();
        byte[] buffer;
        int readBytes = 0;
        //inicio lectura del archivo para manipularlo en bytes
        buffer = new byte[10 * 1024];
            try{
                flowIn = new BufferedInputStream(new FileInputStream(currentFile));
                while ((readBytes = flowIn.read(buffer)) >= 0){
                    flowOut.write(buffer, 0, readBytes);
                }
                bytesFile = flowOut.toByteArray();

                flowOut.reset();
                flowOut.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
                System.out.println("Error en el buffer de lectura del archivo");
            }
            finally{
                if (flowOut != null){
                    try{
                      flowOut.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error en el buffer de lectura del archivo");
                    }
                }
            }
            //hasta aqui tenemos los bytes del archivo
            if (HeaderFormatRight()){
                this.readImage = new ImageData(width, height, origen, nChanels, 1, alineacion, spaceColor);
                FillImagenData();
            }
            else{
                System.out.println("Fallo al cargar la imagen");
            }

    }
    //headerFormatRight: revisa si tiene el formato de cabecera correcto
    private boolean HeaderFormatRight(){
        byte[] a_dataFile;
        //para cada atributo de la imagen se obtiene los bytes de la imagen
        //y se transforman en enteros
        a_dataFile = this.CutBytes(bytesFile, 18, 22);
        width = this.BytesToInt(a_dataFile);

        a_dataFile = this.CutBytes(bytesFile, 22, 26);
        height = this.BytesToInt(a_dataFile);

        origen = ImageData.ARRIBA_IZQ;

        a_dataFile = this.CutBytes(bytesFile, 10, 14);
        offset = this.BytesToInt(a_dataFile);

        a_dataFile = this.CutBytes(bytesFile, 30, 34);
        compresionBMP = this.BytesToInt(a_dataFile);

        spaceColor = ImageData.BGR;

        alineacion = ImageData.ALINEADO_4;

        /*******************/
        a_dataFile = this.CutBytes(bytesFile, 28, 30);
        nChanels = this.BytesToInt(a_dataFile)/8;

        depth = nChanels;
        if(depth>=3)
        return true;
        else
        return false;

    }
    //retorna la imagen
    public ImageData getImagenData(){
        return this.readImage;
    }

    public ImageData UpdateImage(){
        this.FillImagenData();
        return this.getImagenData();
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
    
    private boolean FillImagenData(){

//TODO: encontrar el mejor formato para visualizar en JOGL, seguramente será RGB o RGBA
// Se encarga de cada tipo de imagen en un bloque diferente  (@Jhon: hay que poder dibujar la imagen, para probar que esto funcione, mientras tanto lo dejo comentado)
//       if (nChanels == 4 /*&& revisar otras cosas como comprseionbmp y alineación */) //En este caso los datos se copian sin hacer modificaciones
//        {
             System.arraycopy(bytesFile, offset, readImage.bytesImage, 0, bytesFile.length-offset);
//        }
//        if (nChanels == 3 /*&& revisar otras cosas como comprseionbmp y alineación */) //En este caso los datos se copian sin hacer modificaciones
//        {
//            java.util.Arrays.fill(readImage.bytesImage,(byte)0);
//            int offsetDestino = 0;
//            for (int i = offset; i < bytesFile.length-offset ; i+=3){
//             System.arraycopy(bytesFile, offset, readImage.bytesImage, offsetDestino, 3);
//             offsetDestino+=4;
//            }
//        }

       
        /* int byteleido = offset;
        for(int i = 0;i<readImage.getHeight();i++){
            for(int j = 0;j<readImage.getWidth();j++){
                for(int k = 0 ;k<nChanels;k++){
                   if(k==3 && depth == 3){
                   readImage.bytesImage[i][j][k]=0;
                   }else{
                   readImage.bytesImage[i][j][k]=bytesFile[byteleido];
                   byteleido++;
                   }
                }
            }
        }*/


        return true;
    }

//convierte de bytes a int
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


}

/*
    private boolean llenarMegaImagen(){
        System.out.println();
        int pixel=0;
        int byteleido = offset;
        for(int i = 0;i<imagenLeida.getHeight();i++){
            for(int j = 0;j<imagenLeida.getWidth();j++){
              if(depth == 24){
              for(int k = 0 ;k<depth;k++){
                   imagenLeida.datosImagen[i][j][k]=contenidoDelFichero[byteleido];
                for   byteleido++;
              }
              }else if(depth == 8){
                   imagenLeida.datosImagen[i][j][0]=contenidoDelFichero[byteleido];
                   byteleido++;
               }else if(depth ==4){
                   if(pixel==0){
                       pixel = 1;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0xF & contenidoDelFichero[byteleido]);
                   }else{
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0xF0 & contenidoDelFichero[byteleido]);
                       pixel = 0;
                       byteleido++;
                   }
               }else{
                   if(pixel==0){
                       pixel = 1;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x1 & contenidoDelFichero[byteleido]);
                   }else if(pixel==1){
                       pixel = 2;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x2 & contenidoDelFichero[byteleido]);
                   }else if(pixel==2){
                       pixel = 3;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x4 & contenidoDelFichero[byteleido]);
                   }else if(pixel==3){
                       pixel = 4;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x8 & contenidoDelFichero[byteleido]);
                   }else if(pixel==4){
                       pixel = 5;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x10 & contenidoDelFichero[byteleido]);
                   }else if(pixel==5){
                       pixel = 6;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x20 & contenidoDelFichero[byteleido]);
                   }else if(pixel==6){
                       pixel = 7;
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x40 & contenidoDelFichero[byteleido]);
                   }else{
                       imagenLeida.datosImagen[i][j][0] =(byte)( 0x80 & contenidoDelFichero[byteleido]);
                       pixel = 0;
                       byteleido++;
                    }
                }
              }

    }
        return true;
    }
*/
