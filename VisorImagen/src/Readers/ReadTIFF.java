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
    private int compresion;
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
    private int numTags;

    public ReadTIFF(File fichero) throws IOException{

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
            this.readImage = new ImageData(width, height, ImageData.ABAJO_IZQ, 3, 1, ImageData.ALINEADO_4, ImageData.RGB);
            System.out.println(readImage.bytesImage.length);
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
        //avanzamos de tag en tag
        aux_index+=12;
        }


        

        return true;


    }



    private void FillImageData() throws IOException{
    // Para copiar los datos se empiezan a copiar filas completas en orden inverso. Es decir, la pimera fila esta definida
    // entre [offset - width*3,offset), estas se copian en orden descendente en readImage.bytesImage (El indice para esta operacion
    // es j.
        int size;
        int j=0;
        if(compresion==1){
        for(int i = offset - 3 * width  ; i >= 8; i = i - 3 * width){
            System.arraycopy(bytesFile, i, readImage.bytesImage,j, 3 * width);
            j += 3 * width;
        }
        }else if(compresion==5){


         /*
          * YA solucione el problema de que se toteara... ahora el problema es q esta leyendo mal...
          * yo creo q el problema esta en como almacenamos en el diccionario...
          * utilice 3 metodos q estan todos ahi metidos pero en este comento el METODO 1 es el q esta funcionando
          * los otros Metodos estan comentados con eso si los quieren ver funcionando toca q comenten el metodo 1
          * y descomenten todo lo q tenga q ver con el metodo q quieran ver...
          *
          *
          * el metodo 1 es el q esta en Test__LZW
          * el metodo 2 es con listas y es el implementado en adobe
          * el metodo 3 esta en esta pagina... el ultimo http://marknelson.us/1989/10/01/lzw-data-compression/ el de modified
          *
          * no he dado con el tiro... el 1 y 3 botan bien cuando es blanco... pero el 1 empieza a botar
          * cadenas muy largas cuando es negro
          *
          * en definitiva creo q estamos manejando mal la estructura del diccionario como almacenamos...
          *
          * voy a darle a lo de comparacion de imagenes
          *
          *
          */

        ByteArrayInputStream bais = new ByteArrayInputStream(this.CutBytesM(bytesFile, 8, offset));
        BitInputStream inputStream = new BitInputStream(bais);
        // Primero se crea un diccionario y se llenan los primeros 256 valores
        //METODO 1 y 3
        String[] dict = new String[65536];
        int iTable;
        for (iTable = 0; iTable<256;iTable++)
             dict[iTable]=(Character.toString((char)iTable));
        dict[iTable++]=(Character.toString((char)-1));
        dict[iTable++]=(Character.toString((char)-1));


        //METODO 2
        /*List<String> dict = new ArrayList<String>();
        for (iTable = 0; iTable<256;iTable++)
            dict.add(Character.toString((char)iTable));
        dict.add(Character.toString((char)-1));
        dict.add(Character.toString((char)-1));*/


        // Se crea una variable de salida y otras variables temporales del algoritmo
        // Los nombres son consistentes con la explicación de http://www.dspguide.com/ch27/5.htm
        //code es el codigo que lee... el k
        int code;
        //read dice cuando bits va a leer
        int read = 9;
        // aca dice cuando bit a leido... para hacer que el while finalice
        int bitsread=9;
        //oldcode es el codigo viejo... el w




        //METODO 1
        String oldcode;
        //ese outstring lo vi para concatenar...


        code = inputStream.readBits(read);





        //imprimo el codigo
        System.out.println("code: "+Integer.toBinaryString(code)+ " " + code);
        //METODO 2
        //String outString;
        //oldcode=dict.get(code);




        //METODO 1
        oldcode=dict[code];

        //METODO 3
        //int oldcode;
        //oldcode=code;
        //char caracter=dict[code].charAt(0);
        //String string;
        while(bitsread<(offset-9)*8){

            //METODO 3
            /*
            code = inputStream.readBits(read);
            boolean isOnDict = false;
            if(code<=iTable){
              isOnDict=true;
            }


            if(isOnDict){
                string=dict[code];
            }else{
                string=dict[oldcode];
                string+=caracter;
            }

            System.out.println("out: "+string);
            caracter = string.charAt(0);
            dict[iTable++]= String.valueOf(oldcode)+caracter;
            oldcode = code;
             *
             *
             */


             //METODO 1
            //este es el mismo codigo q hay en Test LZW
            code = inputStream.readBits(read);
            System.out.println("leido " + dict[code]); // salida
            oldcode = oldcode.concat(dict[code]);
            dict[iTable++] =oldcode;
            oldcode=dict[code];

            //METODO 2
            /*
            System.out.println("dimencion: "+dict.size());

            code = inputStream.readBits(read);
            System.out.println("code: "+Integer.toBinaryString(code)+ " " + code);

            boolean isOnDict = false;
            for(String d:dict){
                if(d.equals((Character.toString((char)code)))){
                    isOnDict=true;
                    break;
                }
            }
            if(isOnDict){
                System.out.println("leido " + dict.get(code));  // salida
                dict.add(dict.get(Integer.parseInt(oldcode)) + dict.get(code).charAt(0));
                oldcode=String.valueOf(code);
            }else{
                outString = dict.get(StringFromTable(dict,oldcode))+dict.get(StringFromTable(dict,oldcode)).charAt(0);
                System.out.println("leido " + outString);
                dict.add(outString);
                oldcode=String.valueOf(code);
            }*/
            bitsread+=read;
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


