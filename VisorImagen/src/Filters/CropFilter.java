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
public class CropFilter extends FilterPipeObject{

    private String name;
    public CropFilter(String name) {
        super("CropFilter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    private boolean isThereMessage = false;
    private boolean markOnly = true;
    private int x0,y0,width,height;

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Crop))
        {
            isThereMessage = true;
            try{
                if(msg.bValue2)
                {
                    x0 = msg.iValue1;
                    if (x0 < 0)
                        x0 = 0;
                    y0 = msg.iValue2;
                    if (y0 < 0)
                        y0 = 0;
                    width = msg.iValue3;
                    if (width > (dataIn.getImageData().getWidth()-x0))
                        width = dataIn.getImageData().getWidth()-x0;
                    height = msg.iValue4;
                    if (height > (dataIn.getImageData().getHeight()-y0))
                        height = dataIn.getImageData().getHeight()-y0;
                }
                if(msg.bValue1)
                    markOnly = true;
                else
                    markOnly = markOnly^true;
            }
            catch(NumberFormatException e) {}
            
        }
        return true;
    }
    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data;
        if(!isThereMessage || markOnly)
        {
            data = new ImageData(this.dataIn.getImageData());
            System.arraycopy(dataIn.getImageData().bytesImage, 0, data.bytesImage, 0, dataIn.getImageData().bytesImage.length);
        }
        else
            data = new ImageData(this.dataIn.getImageData(),width,height);

//        System.out.println("Filtro de crop por dentro " + x0 + " "+ y0);

        int inWidth = dataIn.getImageData().getWidth();
        int inHeight = dataIn.getImageData().getHeight();
        //Sigo trabajando 01/04/2011 . Felipe
        int nChannels = dataIn.getImageData().getnCanales();

        

        if(isThereMessage)
        if (markOnly)
        {
            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++)
                    data.bytesImage[ ((i + (inHeight - y0 - height))*data.getWidthStep() + j + x0)*nChannels ] = (byte)255;
            }
        }
        else
        {
            for (int i = 0; i < height; i++)
            {
                System.arraycopy(   dataIn.getImageData().bytesImage,
                                    ((inHeight -y0 - i)*dataIn.getImageData().getWidthStep() + x0)*nChannels,
                                    data.bytesImage,
                                    (height - i -1) * data.getWidthStep() * nChannels,
                                    width*nChannels) ;
//                data.bytesImage[ ((i + (inHeight - y0 - height))*data.getWidthStep() + j + x0)*nChannels ] = (byte)255;
            }
        }
//            System.arraycopy(dataIn.getImageData().bytesImage,
//                                (( i +  y0) * dataIn.getImageData().getWidthStep() + x0) * nChannels,
//                                data.bytesImage,
//                                (i) * data.getWidthStep() * nChannels,
//                                width*nChannels);

        this.dataOut.setImageData(data);

//        System.out.println("Internal update Crop : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }



}