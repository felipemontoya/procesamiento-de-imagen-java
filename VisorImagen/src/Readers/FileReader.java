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
import java.io.File;



/**
 *
 * @author Felipe & Jhon
 */
public class FileReader {

    private enum Tipo {BMP, JPEG, error};

    private File currentFile;
    private String extension;
    private Tipo tipo;

    public FileReader(File currentFile){
        if ( currentFile.canWrite() && currentFile.exists() )
            this.currentFile = currentFile;
        else
            System.out.println("El archivo no existe o no se tienen permisos de lectura");

        // Obtiene la extensión
        int i = 0;
        this.extension = "";
        for(char temp : this.currentFile.getName().toCharArray())
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

    public ImageData readImage(){
        if (this.tipo.equals(Tipo.BMP))
            return this.ReadFileBMP();
        else
            if (this.tipo.equals(Tipo.JPEG))
                return this.ReadFileJPEG();
            else
                return new ImageData();  //todo: debería botar una excepcion

    }

    private ImageData ReadFileBMP(){
        ReadBMP lectorBMP = new ReadBMP(this.currentFile);
        return lectorBMP.getImagenData();
    }

     private ImageData ReadFileJPEG(){
        ReadJPEG lectorJPEG = new ReadJPEG(this.currentFile);
        return lectorJPEG.getImagenData();
    }


}