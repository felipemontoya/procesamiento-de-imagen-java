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
public class MarrHildrethFilter extends FilterPipeObject{

    private String name;
    private boolean isThereMessage = false;
    private int maskSize;
    private double sigma;
    private int threshold;
    public MarrHildrethFilter(String name) {
        super("Marr-Hildreth filter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    @Override
    public boolean ReadMessage(PipeMessage msg)
    {
        if(msg.destination.equals(PipeMessage.Receiver.Marr))
        {
            isThereMessage = true;
            try{
                sigma = msg.dValue1;
            }
            catch(NumberFormatException e) {}

        }
        return true;
    }
    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());


        if (!isThereMessage){
            maskSize = 5;
            sigma = 2;
            threshold = 50;
        }

        int maskOffset = (maskSize - 1) / 2;

        System.out.println("M-H, Width and height from input image: " + data.getWidth() + " - " + data.getHeight());
        //Some definitions
        double LoG[][] = new double[maskSize][maskSize];
        //double vMask[][] = new double[maskSize][maskSize];

        double sig2 = sigma *sigma;

        // Creates a Laplacian of Gaussian mask of the desired size
 //       if (maskSize == 5)
        {
//            LoG[0][0]=0 ;LoG[0][1]=0 ;LoG[0][2]=-1;LoG[0][3]=0 ;LoG[0][4]=0 ;
//            LoG[1][0]=0 ;LoG[1][1]=-1;LoG[1][2]=-2;LoG[1][3]=-1;LoG[1][4]=0 ;
//            LoG[2][0]=-1;LoG[2][1]=-2;LoG[2][2]=16;LoG[2][3]=-2;LoG[2][4]=-1;
//            LoG[3][0]=0 ;LoG[3][1]=-1;LoG[3][2]=-2;LoG[3][3]=-1;LoG[3][4]=0 ;
//            LoG[4][0]=0 ;LoG[4][1]=0 ;LoG[4][2]=-1;LoG[4][3]=0 ;LoG[4][4]=0 ;
            //first the gaussian
            for (int x = -maskOffset; x <= maskOffset; x++)
                    for (int y = -maskOffset; y <= maskOffset; y++)
                        LoG[y+maskOffset][x+maskOffset] = Math.exp(-(x*x + y*y)/(2*sig2));

            // check the sum = 1
            double sum = 0;
            for (int x = 0; x <= maskOffset*2; x++)
                    for (int y = 0; y <= maskOffset*2; y++)
                        sum += LoG[y][x];

            if (sum != 0)
             for (int x = 0; x <= maskOffset*2; x++)
                    for (int y = 0; y <= maskOffset*2; y++)
                        LoG[y][x] = LoG[y][x]/sum;

            //now the laplacian
            for (int x = -maskOffset; x <= maskOffset; x++)
                for (int y = -maskOffset; y <= maskOffset; y++)
                    LoG[y+maskOffset][x+maskOffset] *= (x*x + y*y - 2*sig2)/(sig2*sig2);

            sum =0;
            for (int x = 0; x <= maskOffset*2; x++)
                    for (int y = 0; y <= maskOffset*2; y++)
                        sum += LoG[y][x];

            for (int x = 0; x <= maskOffset*2; x++)
                for (int y = 0; y <= maskOffset*2; y++)
                    LoG[y][x] -= sum/(maskSize*maskSize);

            // print mask on screen
            for (int x = 0; x <= maskOffset*2; x++){
                System.out.println(" ");
                for (int y = 0; y <= maskOffset*2; y++)
                    System.out.print(LoG[y][x] + " ");
            }
        }

        // Creates a high pass filter mask
        int hpfSize = 3;
        int hpfOffset = ( hpfSize -1)/2;
        int hpf[][] = new int[hpfSize][hpfSize];
        if (hpfSize == 3)
        {
            hpf[0][0]=-1;hpf[0][1]=-1;hpf[0][2]=-1;
            hpf[1][0]=-1;hpf[1][1]=8; hpf[1][2]=-1;
            hpf[2][0]=-1;hpf[2][1]=-1;hpf[2][2]=-1;

        }
        else
        {
            System.out.println("Only 3x3 high pass filter supported to this point");
            return false;
        }

        int src[] = new int[(data.getWidth() + maskOffset*2)*(data.getHeight() + maskOffset*2)];
        int dst1[] = new int[(data.getWidth() + hpfOffset*2)*(data.getHeight() + hpfOffset*2)];
        int dst2[] = new int[data.getWidth()*data.getHeight()];
        java.util.Arrays.fill(src,0);


        java.util.Arrays.fill(dst1,128);
        java.util.Arrays.fill(dst2,128);


        // Copy input data into filter src
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                src[(i+maskOffset)+(j+maskOffset)*(data.getWidth()+maskOffset)] = BytesToInt(data.bytesImage[(i + (j*data.getWidth()))*3]);
            }

        int t_LoG;
        int a_i,a_j;
        //Operate
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                //vertical & horizontal operator
                a_i = i+maskOffset;
                a_j = j+maskOffset;
                t_LoG = 0;
                //t_v = 0;
                for (int x = -maskOffset; x <= maskOffset; x++)
                    for (int y = -maskOffset; y <= maskOffset; y++)
                    {
                        t_LoG +=  (LoG[y+maskOffset][x+maskOffset])*src[(a_i+x)+(a_j+y)*(maskOffset+data.getWidth())];

                        //t_h +=  (hMask[y+maskOffset][x+maskOffset])*src[(a_i+x)+(a_j+y)*(maskOffset+data.getWidth())];
                    }
                //Threshold result
//                if (t_LoG > threshold)
//                    t_LoG = 255;
//                else
//                    t_LoG = 0;
                dst1[(i+hpfOffset)+(j+hpfOffset)*(data.getWidth()+2*hpfOffset)] = t_LoG; //src[(a_i)+(a_j)*(maskOffset+data.getWidth())];//
            }

        //High pass filter over the thresholded image
        int t_hpf;
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                //vertical & horizontal operator
                a_i = i+hpfOffset;
                a_j = j+hpfOffset;
                t_hpf = 0;
                //t_v = 0;
                for (int x = -hpfOffset; x <= hpfOffset; x++)
                    for (int y = -hpfOffset; y <= hpfOffset; y++)
                    {
                        t_hpf +=  (hpf[y+hpfOffset][x+hpfOffset])*dst1[(a_i+x)+(a_j+y)*(2*hpfOffset+data.getWidth())];

                        //t_h +=  (hMask[y+maskOffset][x+maskOffset])*src[(a_i+x)+(a_j+y)*(maskOffset+data.getWidth())];
                    }
                //Threshold result
                dst2[i+j*data.getWidth()] = t_hpf; //src[(a_i)+(a_j)*(maskOffset+data.getWidth())];//
            }







        // Copy input data into filter src
        for (int i = 0; i < data.getWidth(); i++)
            for (int j = 0; j < data.getHeight(); j++)
            {
                data.bytesImage[(i + (j*data.getWidth()))*3] = generateByte(dst2[i + j* data.getWidth()]);
                data.bytesImage[(i + (j*data.getWidth()))*3+1] = generateByte(dst2[i + j* data.getWidth()]);
                data.bytesImage[(i + (j*data.getWidth()))*3+2] = generateByte(dst2[i + j* data.getWidth()]);
            }
        this.dataOut.setImageData(data);

//        System.out.println("Internal update Crop : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }
    public double BytesToDouble(byte valor){
             return (double) (valor & 0xFF);

    }
    public byte DoubleToByte(double valor){

            return generateByte((int)valor);
    }
    private byte generateByte(int integer) {
        byte[] byteStr = new byte[4];
        byteStr[0]=(byte)((integer & 0xff000000)>>>24);
        byteStr[1]=(byte)((integer & 0x00ff0000)>>>16);
        byteStr[2]=(byte)((integer & 0x0000ff00)>>>8);
        byteStr[3]=(byte)((integer & 0x000000ff));
        return byteStr[3];
        }
}