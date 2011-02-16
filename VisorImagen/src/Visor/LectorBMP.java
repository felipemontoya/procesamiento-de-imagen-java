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
 * @author Jhon & Felipe
 */
public class LectorBMP {

    private MegaImagen imagenLeida;
    private byte[] contenidoDelFichero;
    private int width;
    private int height;
    private int origen;
    private int offset;
    private int compresionbmp;
    private int espacioColor;
    private int alineacion;
    private int nCanales;
    private int depth;



    public LectorBMP(File fichero){
        System.out.println("Leyendo archivo BMP");

        BufferedInputStream flujoEntrada = null;
        ByteArrayOutputStream flujoSalida = new ByteArrayOutputStream();
        byte[] buffer;
        int leidos = 0;

        buffer = new byte[10 * 1024];
            try{
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

            //System.out.println("Largo del ByteArray: " + contenidoDelFichero.length);

            if (revisarCabecera()){
                this.imagenLeida = new MegaImagen(width, height, origen, nCanales, 1, alineacion, espacioColor);
                llenarMegaImagen();
            }
            else{
                System.out.println("Fallo al cargar la imagen");
            }

    }

    private boolean revisarCabecera(){
        byte[] aux;

        aux = this.recortarBytes(contenidoDelFichero, 18, 22);
        width = this.convertirBytesInt(aux);

        aux = this.recortarBytes(contenidoDelFichero, 22, 26);
        height = this.convertirBytesInt(aux);

        origen = MegaImagen.ARRIBA_IZQ;

        aux = this.recortarBytes(contenidoDelFichero, 10, 14);
        offset = this.convertirBytesInt(aux);

        aux = this.recortarBytes(contenidoDelFichero, 30, 34);
        compresionbmp = this.convertirBytesInt(aux);

        espacioColor = MegaImagen.RGBA;

        alineacion = MegaImagen.ALINEADO_4;

        nCanales = 4;
        /*******************/
        aux = this.recortarBytes(contenidoDelFichero, 28, 30);
        depth = this.convertirBytesInt(aux);
        depth/=8;
        if(depth>=3)
        return true;
        else
        return false;

    }

    public MegaImagen getMegaImagen(){
        return this.imagenLeida;
    }

    public byte[] recortarBytes(byte[] b,int i,int j){
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
    
    private boolean llenarMegaImagen(){
        System.out.println("entro aca");
        int byteleido = offset;
        for(int i = 0;i<imagenLeida.getHeight();i++){
            for(int j = 0;j<imagenLeida.getWidth();j++){
                for(int k = 0 ;k<nCanales;k++){
                   if(k==3 && depth == 3){
                   imagenLeida.datosImagen[i][j][k]=0;
                   }else{
                   imagenLeida.datosImagen[i][j][k]=contenidoDelFichero[byteleido];
                   byteleido++;
                   }
                }
            }
        }
        return true;
    }


     public int convertirBytesInt(byte[] valor){
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
