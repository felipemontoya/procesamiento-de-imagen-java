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

/**
 *
 * @author Jhon
 */
public class ImageReader{

    public ImageReader(){
    }

    public ImageData getImageData(File f){
        byte[] contenidoDelFichero = null;
        FileInputStream theFIS = null;
        BufferedInputStream theBIS = null;
        byte[] buffer;
        int leido = 0;
        ByteArrayOutputStream theBOS = new ByteArrayOutputStream();
        String extencion="";
        int i=0;
        ImageData image = new ImageData();
        for(char a : f.getPath().toCharArray()){
            if(a=='.' || i > 0){
                i++;
                if(i>1){
                    extencion+=String.valueOf(a);
                }

            }
        }
        image.setFormat(extencion);
        if(extencion.equals("BMP") || extencion.equals("bmp")){
            image.setFormato(false);
            buffer = new byte[54 * 1024];
            try{
                theFIS = new FileInputStream(f);
                theBIS = new BufferedInputStream(theFIS);
                while ((leido = theBIS.read(buffer)) >= 0){
                theBOS.write(buffer, 0, leido);
                }
                contenidoDelFichero = theBOS.toByteArray();
                image.setCabecera(contenidoDelFichero);





                theBOS.reset();
                theBOS.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
            }
            finally{
                if (theBIS != null){
                    try{
                      theBIS.close();
                    }
                    catch (IOException e){
                    e.printStackTrace();
                    }
              }
            }
            
        }else if(extencion.equals("jpg") || extencion.equals("JPG") || extencion.equals("jpeg") || extencion.equals("JPEG")){
            image.setFormato(false);
            buffer = new byte[10 * 1024];
            try{
                theFIS = new FileInputStream(f);
                theBIS = new BufferedInputStream(theFIS);
                while ((leido = theBIS.read(buffer)) >= 0){
                theBOS.write(buffer, 0, leido);
                }
                contenidoDelFichero = theBOS.toByteArray();
                image.setCabecera(contenidoDelFichero);





                theBOS.reset();
                theBOS.close();
            }
            catch (IOException e1){
                e1.printStackTrace();
            }
            finally{
                if (theBIS != null){
                    try{
                      theBIS.close();
                    }
                    catch (IOException e){
                    e.printStackTrace();
                    }
                }
            }
        }else{
            
        }


       
        return image;
    }
}



 /*

    try
    {
      theFIS = new FileInputStream(f);
      theBIS = new BufferedInputStream(theFIS);
      theBIS.read();
      while ((leido = theBIS.read(buffer)) >= 0)
      {
        theBOS.write(buffer, 0, leido);
      }


      // Fichero leido del todo, pasamos el contenido
      // del BOS al byte[]
      contenidoDelFichero = theBOS.toByteArray();
      // Liberamos y cerramos para ser eficientes
      theBOS.reset();
      // Este close no va dentro de un try/catch por que
      // BOS es un Stream especial y close no hace nada
      theBOS.close();
    }
    catch (IOException e1)
    {
      // Error leyendo el fichero así que no tenemos
      // en memoria el fichero.
      e1.printStackTrace();
    }
    finally
    {
      if (theBIS != null)
      {
        try
        {
          theBIS.close();
        }
        catch (IOException e)
        {
          // Error cerrando stream del fichero
          e.printStackTrace();
        }
      }
    }
        ImageData i = new ImageData(8 * 1024,true);
        i.setBits(contenidoDelFichero);
        return i;*/