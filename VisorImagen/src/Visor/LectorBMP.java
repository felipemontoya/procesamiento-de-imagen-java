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

            if (revisarCabecera())
                this.imagenLeida = new MegaImagen(width, height, origen, nCanales, depth, alineacion, espacioColor);
            llenarMegaImagen();

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
        aux = this.recortarBytes(contenidoDelFichero, 26, 28);
        depth = this.convertirBytesInt(aux);

        return true;
    }

    private boolean llenarMegaImagen(){

        //Aquí se llena la matriz de bytes en el espacio de color especificado, para este caso RGBA (4 canales)

        return true;
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
