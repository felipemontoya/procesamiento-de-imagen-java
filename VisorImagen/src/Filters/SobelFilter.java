/***************************************************************************************************************/
/*    Especificaciones de la Programación
 *    Patrón
 *    Versión 1.1
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
package Filters;
import Data.ImageData;
import PipeLine.*;


/**
 *
 * @author Jhon
 */
public class SobelFilter extends FilterPipeObject{

    private String name;
    public SobelFilter(String name) {
        super("Sobel filter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }


    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());

        int maskSize = 3;
        int maskOffset = (maskSize - 1) / 2;

        System.out.println("Width and height from input image: " + data.getWidth() + " - " + data.getHeight());
        //Some definitions
        double hMask[][] = new double[maskSize][maskSize];
        double vMask[][] = new double[maskSize][maskSize];

        if (maskSize == 3)
        {
            vMask[0][0]=-1;vMask[0][1]=-2;vMask[0][2]=-1;
            vMask[1][0]=0; vMask[1][1]=0; vMask[1][2]=0;
            vMask[2][0]=+1;vMask[2][1]=+2;vMask[2][2]=+1;

            hMask[0][0]=-1;hMask[0][1]=0;hMask[0][2]=+1;
            hMask[1][0]=-2;hMask[1][1]=0;hMask[1][2]=+2;
            hMask[2][0]=-1;hMask[2][1]=0;hMask[2][2]=+1;     
        }
        else
        {
            System.out.println("Only 3x3 mask supported to this point");
            return false;
        }

        double src[] = new double[(data.getWidth() + maskOffset*2)*(data.getHeight() + maskOffset*2)];
        double dst[] = new double[data.getWidth()*data.getHeight()];
        java.util.Arrays.fill(src,0);
//        java.util.Arrays.fill(src,src[0]);

        java.util.Arrays.fill(dst,128);
//        java.util.Arrays.fill(dst,dst[0]);

        // Copy input data into filter src
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                src[(i+maskOffset)+(j+maskOffset)*(data.getWidth()+maskOffset)] = (double)data.bytesImage[(i + (j*data.getWidth()))*3];
            }

        double t_h,t_v;
        int a_i,a_j;
        //Operate
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                //vertical & horizontal operator
                a_i = i+maskOffset;
                a_j = j+maskOffset;
                t_h = 0;
                t_v = 0;
                for (int x = -maskOffset; x <= maskOffset; x++)
                    for (int y = -maskOffset; y <= maskOffset; y++)
                    {
                        t_v +=  (vMask[y+maskOffset][x+maskOffset])*src[(a_i+x)+(a_j+y)*(maskOffset+data.getWidth())];

                        t_h +=  (hMask[y+maskOffset][x+maskOffset])*src[(a_i+x)+(a_j+y)*(maskOffset+data.getWidth())];
                    }

                dst[i+j*data.getWidth()] = Math.sqrt(t_v*t_v + t_h*t_h);
            }


        // Copy input data into filter src
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                data.bytesImage[(i + (j*data.getWidth()))*3] = (byte)dst[i + j* data.getWidth()];
                data.bytesImage[(i + (j*data.getWidth()))*3+1] = (byte)dst[i + j* data.getWidth()];
                data.bytesImage[(i + (j*data.getWidth()))*3+2] = (byte)dst[i + j* data.getWidth()];
            }
        this.dataOut.setImageData(data);

//        System.out.println("Internal update Crop : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}