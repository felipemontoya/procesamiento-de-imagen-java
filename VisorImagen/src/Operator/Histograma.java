/***************************************************************************************************************/
/*    Especificaciones de la Programación
 *    Patrón
 *    Versión 1.0
 *
 *    General:
 *          - Toda clase debe tener la información de esta especificación.
 *          - Todo procedimiento/variable programado será debidamente comentado dependiendo de su naturaleza.
 *          - Todas las variables, clases y métodos deberán ser nombradas en ingles y dependiendo de su función.
 *    Paquetes:
 *          - Toda clase estará incluida en un paquete.
 *          - El nombre del paquete deberá ser acorde a las clases contenidas en el.
 *          - Si son paquetes de prueba deben estar identificados iniciando con "test_nombredelpaquete".
 *          - Si son paquetes que serán utilizados temporalmente para ser mas tarde eliminador irán identificados
 *            iniciando con "temp_nombredelpaquete".
 *
 *    Clases:
 *          - Toda clase iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: ClaseEjemplo
 *          - El nombre de la clase debe identificar su funcionalidad.
 *          - Antes iniciar la clase debe ser comentado para que sirve dicha clase y como es su constructor.
 *          - Si son clases de prueba irán identificados como "cTest_NombreClase"
 *
 *    Métodos
 *          - Toda método iniciara con la primera letra mayúscula, si el nombre está compuesto por mas palabras,
 *            Las siguientes palabras deberán iniciar también con la primera letra en mayúscula ej.: MetodoEjemplo
 *          - El nombre de el método debe identificar su funcionalidad. Si el método es un get o un set ira en minúscula
 *          - Antes iniciar el método debe ser comentado para que sirve dicha clase, sus entradas y salidas si tiene
 *
 *    Variables
 *          - Todo nombre de variable inicia en minúsculas, si el nombre de la variable está compuesta por mas palabras
 *            Las siguientes palabras deberán iniciar con la primera letra en mayúscula ej.: variableEjemplo.
 *          - El nombre de la variable debe tener sentido acorde a su valor y su utilidad.
 *          - si son variables auxiliares o temporal deben ir identificados como a_nombreVariable o t_nombreVariable.
 *          - las variables globales ya están plenamente identificadas con color verde.
 *          - Cuando se declara una variable debe comentarse una breve funcionalidad de dicha variable.
 *
 *   léase y cúmplase
 */
/***************************************************************************************************************/


package Operator;

import Data.ImageData;

/**
 *
 * @author Jhon
 */
public class Histograma {
    private int color;
    private int[] hist;

    public Histograma(ImageData data,int color){
        hist =new int[256];
        int k;
        switch(data.getEspacioColor()){
        case ImageData.BGR:
            if(color==0)
                color=2;
            else if(color==2)
                color=0;
            break;
      }

        for(int i=0;i<256;i++){
            hist[i]=0;
        }

        for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                k = BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + color]);
                hist[k]++;
            }
        }
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }

    public int mayor(){
        int mayor = Integer.MIN_VALUE;
        for(int i=0;i<hist.length;i++){
            if(mayor<hist[i])
                mayor=hist[i];
        }
        return mayor;
    }

    public int menor(){
        int menor = Integer.MAX_VALUE;
        for(int i=0;i<hist.length;i++){
            if(menor>hist[i])
                menor=hist[i];
        }
        return menor;
    }

    /**
     * @return the hist
     */
    public int[] getHist() {
        return hist;
    }

    /**
     * @param hist the hist to set
     */
    public void setHist(int[] hist) {
        this.hist = hist;
    }


}
