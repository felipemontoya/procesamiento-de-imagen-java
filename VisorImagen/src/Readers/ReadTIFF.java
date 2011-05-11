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
import Utilities.BitInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import test_Draw.*;

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
    
    //tipo de compresion BMP
    private int compresion;

    private int predictor;

    private int interleaved;
    private int samplesPerPixel;
    private int tileWidth;
    private int tileHeight;


    // Variables propias del TIFF -> ya se han usado

    private int endian;
    private boolean bigEndian;
    private int isTiff;
    private int offset;
    private int numTags;

    public ReadTIFF(File fichero) {

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
            



            this.readImage = new ImageData(width, height, ImageData.ARRIBA_IZQ, 3, 1, ImageData.ALINEADO_4, ImageData.RGB);
//            System.out.println(readImage.bytesImage.length);
            FillImageData();

            }

    }


    private boolean HeaderFormatRight(){
        byte[] a_dataFile;
        int aux_index;
        int aux_tag;


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
        System.out.println("offset :" + offset);

        //contamos el numero de tag del tiff con el offset
        a_dataFile = this.CutBytes(bytesFile, offset, offset+2);
        numTags = this.BytesToInt(a_dataFile);
        System.out.println("number of Tags: " + numTags);

        //inicio del primer tag!!
        aux_index=offset+2;

        //empezamos a buscar los 2 primeros bytes de cada tag (cada tag es de 12 bytes)
        for(int i=0;i<numTags;i++){

            a_dataFile = this.CutBytes(bytesFile, aux_index, aux_index+2);
            aux_tag = this.BytesToInt(a_dataFile);
            //numero del tag de la altura 257
            if(aux_tag==257){
            //copiamos la altura
            a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
            height = this.BytesToInt(a_dataFile);
            System.out.println("Alto: "+height);
            //tag del ancho 256
            }else if(aux_tag==256){
                //copiamos la altura
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                width = this.BytesToInt(a_dataFile);
                System.out.println("Ancho: "+width);
            } else if(aux_tag==259){
                 a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                compresion = this.BytesToInt(a_dataFile);
                if(compresion==1){
                    System.out.println("TIFF sin compresion.");
                }else if(compresion==5){
                    System.out.println("TIFF con compresion LZW.");
                }else{
                    System.out.println("TIFF con compresion no soportada.");
                    return false;
                }
             }
            else if (aux_tag==317)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                predictor = this.BytesToInt(a_dataFile);
                System.out.println("Predictor information: " + predictor);
            }
            else if (aux_tag==284)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                interleaved = this.BytesToInt(a_dataFile);
                System.out.println("Planar configuration: " + interleaved);
            }
            else if (aux_tag==277)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                samplesPerPixel = this.BytesToInt(a_dataFile);
                System.out.println("Samples per pixel: " + samplesPerPixel);
            }
            else if (aux_tag==322)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                tileWidth = this.BytesToInt(a_dataFile);
                System.out.println("tileWidth: " + tileWidth);
            }
            else if (aux_tag==323)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                tileHeight = this.BytesToInt(a_dataFile);
                System.out.println("tileWidth: " + tileHeight);
            }
            else if (aux_tag==324)
            {
                a_dataFile = this.CutBytes(bytesFile, aux_index+8, aux_index+12);
                int a_tiled = this.BytesToInt(a_dataFile);
                System.out.println("Tiled: " + a_tiled);
            }
            //avanzamos de tag en tag
            aux_index+=12;
        }


        

        return true;


    }



    private void FillImageData() {
    // Para copiar los datos se empiezan a copiar filas completas en orden inverso. Es decir, la pimera fila esta definida
    // entre [offset - width*3,offset), estas se copian en orden descendente en readImage.bytesImage (El indice para esta operacion
    // es j.
        int size;
        int j = (width-1)*height*3;
        if(compresion==1){

        for(int i = offset - 3 * width  ; i >= 8; i = i - 3 * width){
            System.arraycopy(bytesFile, i, readImage.bytesImage,j, 3 * width);
            j -= 3 * width;
        }

        }else if(compresion==5){
            
            
            
            
            ByteArrayInputStream bais = new ByteArrayInputStream(this.CutBytesM(bytesFile, 8, offset));
            BitInputStream inputStream = new BitInputStream(bais);

            cTest_LZW decoder = new cTest_LZW();

            try
            {
                char[] decoded = decoder.decodeTiff(inputStream,predictor,interleaved,samplesPerPixel,tileWidth,tileHeight, width, height);

                if (samplesPerPixel ==3)
                {
                    for (int i = 0; i < decoded.length; i++)
                    {
                        readImage.bytesImage[i] = (byte)decoded[i];
                    }
                }
                else if(samplesPerPixel == 4)
                {
                    int a_i = 0;
                    for (int i = 0; i < decoded.length; i+=4)
                    {
                        readImage.bytesImage[a_i++] = (byte)decoded[i];
                        readImage.bytesImage[a_i++] = (byte)decoded[i+1];
                        readImage.bytesImage[a_i++] = (byte)decoded[i+2];
                    }
                }

                //flip vertically

                for (int i = 0; i < readImage.getHeight()/2; i++)
                {
                    byte[] t_copy = new byte[readImage.getnCanales()*readImage.getWidth()];

                    System.arraycopy(readImage.bytesImage, i * readImage.getWidth() * readImage.getnCanales(), t_copy,0, t_copy.length);

                    System.arraycopy(readImage.bytesImage, (readImage.getHeight()- 1 - i) * readImage.getWidth() * readImage.getnCanales(), readImage.bytesImage,i * readImage.getWidth() * readImage.getnCanales(), t_copy.length);

                    System.arraycopy(t_copy, 0, readImage.bytesImage,(readImage.getHeight()-1 - i) * readImage.getWidth() * readImage.getnCanales(), t_copy.length);

                }
            }
            catch (Exception e)
            {
                System.out.println("Algo salio mal descomprimiendo LZW " + e.getMessage());
            }
        }
     }
    
    public int StringFromTable(List<String> dict,String search){
        int i = 0;
        for(i=0;i<dict.size();i++){
            if(search.equals(dict.get(i))){
                break;
            }
        }
        
        return i;
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
    
    public byte[] CutBytesM(byte[] b,int i,int j){
        byte[] a = new byte[j-i];
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


