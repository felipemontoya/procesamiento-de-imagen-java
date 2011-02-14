/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Visor;

import java.io.File;

/**
 *
 * @author Felipe
 */
public class LectorJPEG {

    private MegaImagen imagenLeida;

    public LectorJPEG(File fichero){
        System.out.println("leyendo archivo JPEG");
        // hacer aquí y en otros métodos de la clase todo el procesamiento que se necesite
    }

    public MegaImagen getMegaImagen(){
        return this.imagenLeida;
    }


}
