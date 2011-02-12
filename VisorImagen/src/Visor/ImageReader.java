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

    public MegaImagen getImageData(File f){
        byte[] contenidoDelFichero = null;
        FileInputStream theFIS = null;
        BufferedInputStream theBIS = null;
        byte[] buffer;
        byte[] aux;
        int leido = 0;
        ByteArrayOutputStream theBOS = new ByteArrayOutputStream();
        String extencion="";
        MegaImagen image = null;
        int i=0;
        for(char a : f.getPath().toCharArray()){
            if(a=='.' || i > 0){
                i++;
                if(i>1){
                    extencion+=String.valueOf(a);
                }

            }
        }
        //ACA EMPIEZA EL BMP
        if(extencion.equals("BMP") || extencion.equals("bmp")){
            buffer = new byte[54 * 1024];
            try{
                theFIS = new FileInputStream(f);
                theBIS = new BufferedInputStream(theFIS);
                while ((leido = theBIS.read(buffer)) >= 0){
                theBOS.write(buffer, 0, leido);
                }
                contenidoDelFichero = theBOS.toByteArray();
                int extencionx=0;
                aux = this.recortarBytes(contenidoDelFichero, 17, 21);
                int width = this.convertirBytesInt(aux);
                aux = this.recortarBytes(contenidoDelFichero, 21, 25);
                int height = this.convertirBytesInt(aux);
                aux = this.recortarBytes(contenidoDelFichero, 9, 13);
                int origen = this.convertirBytesInt(aux);
                boolean formatoendian=false;
                aux = this.recortarBytes(contenidoDelFichero, 29, 33);
                int compresionbmp = this.convertirBytesInt(aux);
                aux = this.recortarBytes(contenidoDelFichero, 27, 29);
                int bpp= this.convertirBytesInt(aux);
                byte[][] datosImagen = new byte[54 * 1024][54 * 1024];
                image = new MegaImagen(extencionx,width,height,origen,formatoendian,compresionbmp,bpp,datosImagen);




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
        //ACA EMPIEZA EL JPG
        }else if(extencion.equals("jpg") || extencion.equals("JPG") || extencion.equals("jpeg") || extencion.equals("JPEG")){
            buffer = new byte[10 * 1024];
            try{
                theFIS = new FileInputStream(f);
                theBIS = new BufferedInputStream(theFIS);
                while ((leido = theBIS.read(buffer)) >= 0){
                theBOS.write(buffer, 0, leido);
                }
                contenidoDelFichero = theBOS.toByteArray();
                




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

    public byte[] recortarBytes(byte[] b,int i,int j){
        System.out.println(b.length+" "+i+" "+j);
        byte[] a = new byte[(j-i) * 1024];
        int k=0;
        for(int ii = i;ii<j;ii++){
            a[k]=b[ii];
            k++;
        }
        return a;
    }

     public int convertirBytesInt(byte[] b){
        String val = new String(b);
        int num = -1;
        try {
        num = Integer.decode(val);
        }   catch(NumberFormatException e) {
        num = -1;}
        return num;
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