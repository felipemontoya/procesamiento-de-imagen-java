/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;



import java.io.File;



/**
 *
 * @author Felipe & Jhon
 */
public class LectorArchivo {

    private enum Tipo {BMP, JPEG, error};

    private File fichero;
    private String extension;
    private Tipo tipo;

    public LectorArchivo(File fichero){
        if ( fichero.canWrite() && fichero.exists() )
            this.fichero = fichero;
        else
            System.out.println("El archivo no existe o no se tienen permisos de lectura");

        // Obtiene la extensión
        int i = 0;
        this.extension = "";
        for(char temp : this.fichero.getName().toCharArray())
            if( temp == '.' || i > 0){
                i++;
                if(i>1)
                    this.extension += String.valueOf(temp);
            }

        if (this.extension.isEmpty()){
            System.out.println("El archivo esta corrupto");
            this.tipo = Tipo.error;
        }
        else
        if (this.extension.equalsIgnoreCase("bmp")){
            System.out.println("Archivo de bitmap de windows");
            this.tipo = Tipo.BMP;
        }
        else
        if (this.extension.equalsIgnoreCase("jpeg") || this.extension.equalsIgnoreCase("jpg")){
            System.out.println("Archivo JPEG");
            this.tipo = Tipo.JPEG;
        }
        else{
            System.out.println("Archivo no reconocido");
        }
    }

    public MegaImagen leerImagen(){
        if (this.tipo.equals(Tipo.BMP))
            return this.leerBMP();
        else
            if (this.tipo.equals(Tipo.JPEG))
                return this.leerJPEG();
            else
                return new MegaImagen();  //todo: debería botar una excepcion

    }

    private MegaImagen leerBMP(){
        MegaImagen retorno = new MegaImagen(); //aqui es donde se llama a la clase como en leerJPEG
        return retorno;
    }

     private MegaImagen leerJPEG(){
        LectorJPEG lectorJPEG = new LectorJPEG(this.fichero);
        return lectorJPEG.getMegaImagen();
    }


}
