/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Filters;
import Data.ImageData;
import PipeLine.*;


/**
 *
 * @author Jhon
 */
public class DCTFilter extends FilterPipeObject{

    private String name;
    private int channel;

    public DCTFilter(String name, int channel) {
        super("Discrete cosine transform " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
        this.channel = channel;
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());


        int nChannels = data.getnCanales();

        int N = data.getWidth();
        int M = data.getHeight();


//        la DCT se calcula para el canal seleccionado de la imagen completa. La ecuación usada es
//        es la de http://www.cs.cf.ac.uk/Dave/Multimedia/node231.html y es consecuente con la de http://en.wikipedia.org/wiki/JPEG
        for (int u = 0; u < data.getWidth(); u++)
        {
            for (int v = 0; v < data.getHeight(); v++)
            {
                double factor = Math.sqrt(2./(double)N) * Math.sqrt(2./(double)M);

                double sum = 0;
                for (int i = 0; i < N; i++)
                    for (int j = 0; j < M; j++)
                        sum += Lambda(i) * Lambda(j)* Math.cos(((Math.PI*u)/(2*N))*(2*i+1))*Math.cos(((Math.PI*v)/(2*M))*(2*j+1))*data.bytesImage[(j * data.getWidthStep() + i ) * nChannels + channel];


                for(int k = 0 ;k < nChannels; k++){
                    data.bytesImage[(v * data.getWidthStep() + u ) * nChannels + k] = (byte)(factor*sum);
                }


            }
        }


        this.dataOut.setImageData(data);

        System.out.println("Internal update DCT : " + name);
        return  true;
    }

    public double Alpha(int i,int N){
        double temp = Math.sqrt(N);
        if (i==0)
            return 1./temp;
        else
            return 1.4142135623730950488016887242097/temp;
    }

    public double Lambda(int i){
        if (i==0)
            return 1./1.4142135623730950488016887242097;
        else
            return 1.;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

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
