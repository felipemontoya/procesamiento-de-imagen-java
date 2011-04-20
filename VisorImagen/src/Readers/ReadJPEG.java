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
import java.util.Stack;
import java.util.Vector;


/**
 *
 * @author Felipe
 */
public class ReadJPEG {

    //datos de la imagen en la clase
    private ImageData readImage;
    //los bytes del archivo leido
    private byte[] bytesFile;

    int width, height;
    int numberOfComponents;
    int rawDataIndex;

    public ReadJPEG(File fichero){

        System.out.println("Leyendo archivo JPEG");
        
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

            
            searchBegintoEnd();
            searchTags();

            //Por motivos de manejo de printSelf
            this.readImage = new ImageData(width, height, ImageData.ARRIBA_IZQ, 4, ImageData.PROF_U8, ImageData.ALINEADO_4, ImageData.RGB);


    }

    private boolean  searchTags(){
        Vector<Byte> tags = new Vector<Byte>();
        Vector<Integer> posTags = new Vector<Integer>();
        
        for (int i = 0; i < bytesFile.length; i++){
            if (bytesFile[i] == (byte)0xFF){
                tags.add(bytesFile[i+1]);
                posTags.add(i);
            }
        }
//      Ahora se deben revisar los tags que estén en la lista
        System.out.println("Cantidad de tags(Antes de corrección): " + tags.size());

        for(int i = 0; i < tags.size(); i++){
            System.out.println("Tag: " + tags.elementAt(i));

            switch(tags.elementAt(i))
            {
                case (byte)0xD8:
                    System.out.println("Start of Image");
                    break;
                case (byte)0xD9:
                    System.out.println("End of Image");
                    break;
                case (byte)0xDB:
                    System.out.println("Quantization Table");
                    break;
                case (byte)0xC4:
                    System.out.println("Huffman Table");
                    break;
                case (byte)0xDA:
                    System.out.println("Start of Scan");
                    break;
                case (byte)0xE0:
                    System.out.println("JFIF application segment");
                    readAPP0(posTags.elementAt(i));
                    break;
                case (byte)0xE1:
                    System.out.println("APP1");
                    break;
                case (byte)0xC0:
                    System.out.println("SOF0");
                    readSOF0(posTags.elementAt(i));
                    break;



            }

        }

        return true;
    }
    


    private void readSOF0(int baseIndex)
    {
        byte[] a_dataFile;
        int aux_index;
        int aux_tag;

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 2, 2);
        aux_tag = this.BytesToInt(a_dataFile,false);
        System.out.println("SOF0 lenght :" + aux_tag);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 4, 1);
        aux_tag = this.BytesToInt(a_dataFile,false);
        System.out.println("Data precision :" + aux_tag);
        if (aux_tag != 8)
            System.out.println("Error: la precisión es diferente de 8");


        a_dataFile = this.CutBytes(bytesFile, baseIndex + 5, 2);
        aux_tag = this.BytesToInt(a_dataFile,true);
        System.out.println("Image Height :" + aux_tag);
        height = aux_tag;

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 7, 2);
        aux_tag = this.BytesToInt(a_dataFile,true);
        System.out.println("Image Width :" + aux_tag);
        width = aux_tag;

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 9, 1);
        aux_tag = this.BytesToInt(a_dataFile,true);
        System.out.println("Number of components :" + aux_tag);
        numberOfComponents = aux_tag;

        rawDataIndex = baseIndex + 10;


    }

    private void readAPP0(int baseIndex)
    {
        byte[] a_dataFile;
        int aux_index;
        int aux_tag;

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 2, 2);
        aux_tag = this.BytesToInt(a_dataFile,false);
        System.out.println("APP0 lenght :" + aux_tag);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 4, 5);
        System.out.println("Identifier :" + (char)a_dataFile[0] + (char)a_dataFile[1] +(char)a_dataFile[2] +(char)a_dataFile[3]);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 9, 2);
//        aux_tag = this.BytesToInt(a_dataFile,false);
        System.out.println("Versioning :" + (int)a_dataFile[0] + ":" + (int)a_dataFile[1]);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 11, 1);
//        aux_tag = this.BytesToInt(a_dataFile,false);
        System.out.println("Units :" + (int)a_dataFile[0]);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 12, 2);
        aux_tag = this.BytesToInt(a_dataFile,true);
        System.out.println("Xdensity :" + aux_tag);

        a_dataFile = this.CutBytes(bytesFile, baseIndex + 14, 2);
        aux_tag = this.BytesToInt(a_dataFile,true);
        System.out.println("Ydensity :" + aux_tag);

//        a_dataFile = this.CutBytes(bytesFile, baseIndex + 16, 1);
//        aux_tag = this.BytesToInt(a_dataFile,false);
//        System.out.println("thumbnail x :" + aux_tag);
    }

    private boolean searchBegintoEnd(){
           if ( bytesFile[0] == (byte)0xFF &&   // revisa el primer y último tag
                bytesFile[1] == (byte)0xD8 &&
                bytesFile[bytesFile.length - 2] == (byte)0xFF &&
                bytesFile[bytesFile.length - 1] == (byte)0xD9){
               System.out.println("Archivo JPEG válido");
               return true;
            }
             else{
                System.out.println("Archivo JPEG inválido");
                //throw Exception;   //todo: lanzar la excepción realmente
                return false;
             }
    }

    public ImageData getImagenData(){
        return this.readImage;
    }

    public int BytesToInt(byte[] valor, boolean endian){


	     int a, b, c, d;
             if (valor.length == 1)
                 return (int)valor[0];
             if (valor.length == 2)
             {
	     if(endian){
	          c = (valor[0] & 0xFF) << 8;
	          d =  valor[1] & 0xFF;
	     } else{
	          c = (valor[1] & 0xFF) << 8;
	          d =  valor[0] & 0xFF;
	     }
	     return  c | d;
             }
             else
             {
                 if(valor.length < 4){
                      throw new ArrayIndexOutOfBoundsException(valor. length);
                 }
                 if(endian){
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
            }

	     return  a | b | c | d;
	}

    public byte[] CutBytes(byte[] b,int i,int j){
         byte[] a;
//        if (j > 4)
            a = new byte[j];
//        else
//            a = new byte[4];

        for(byte bb : a){
            bb = 0;
        }
        int k=0;

        for(int ii = i;ii< i + j;ii++){
            a[k]=b[ii];
            k++;
        }

        return a;
    }

}
