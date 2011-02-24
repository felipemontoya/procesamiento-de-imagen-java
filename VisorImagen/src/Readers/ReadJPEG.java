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

    public ReadJPEG(File fichero){

        System.out.println("Leyendo archivo JPEG");
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

            
            searchBegintoEnd();
            searchTags();

            //Por motivos de manejo de printSelf
            this.readImage = new ImageData(99, 99, ImageData.ARRIBA_IZQ, 4, ImageData.PROF_U8, ImageData.ALINEADO_4, ImageData.YCBCR);


    }

    private boolean  searchTags(){
        Vector<Byte> tags = new Vector<Byte>();
        Vector<Integer> posTags = new Vector<Integer>();
        
        for (int i = 0; i < bytesFile.length; i++){
            if (bytesFile[i] == (byte)0xFF){
                tags.add(bytesFile[i+1]);
                posTags.add(i);
                
            }
//tags.
        }

        System.out.println("Número de tags: " + tags.size() );
        return true;
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


}
