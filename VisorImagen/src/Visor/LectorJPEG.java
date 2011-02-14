/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;

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
public class LectorJPEG {

    private MegaImagen imagenLeida;
    private byte[] contenidoDelFichero;

    public LectorJPEG(File fichero){

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
                contenidoDelFichero = flujoSalida.toByteArray();

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

            System.out.println("Length del byte array: " + contenidoDelFichero.length);

            
            revisarInicioFin();
            buscarTags();

            //Por motivos de manejo de printSelf
            this.imagenLeida = new MegaImagen(99, 99, MegaImagen.ARRIBA_IZQ, 4, MegaImagen.PROF_U8, MegaImagen.ALINEADO_4, MegaImagen.YCBCR);


    }

    private boolean buscarTags(){
        Vector<Byte> tags = new Vector<Byte>();
        Vector<Integer> posTags = new Vector<Integer>();
        
        for (int i = 0; i < contenidoDelFichero.length; i++){
            if (contenidoDelFichero[i] == (byte)0xFF){
                tags.add(contenidoDelFichero[i+1]);
                posTags.add(i);
                
            }
//tags.
        }

        System.out.println("Número de tags: " + tags.size() );
        return true;
    }

    private boolean revisarInicioFin(){
           if ( contenidoDelFichero[0] == (byte)0xFF &&   // revisa el primer y último tag
                contenidoDelFichero[1] == (byte)0xD8 &&
                contenidoDelFichero[contenidoDelFichero.length - 2] == (byte)0xFF &&
                contenidoDelFichero[contenidoDelFichero.length - 1] == (byte)0xD9){
               System.out.println("Archivo JPEG válido");
               return true;
            }
             else{
                System.out.println("Archivo JPEG inválido");
                //throw Exception;   //todo: lanzar la excepción realmente
                return false;
             }
    }

    public MegaImagen getMegaImagen(){
        return this.imagenLeida;
    }


}
