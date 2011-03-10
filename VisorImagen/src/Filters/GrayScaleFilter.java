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
public class GrayScaleFilter extends FilterPipeObject{

    private String name;

    public GrayScaleFilter(String name) {
        super("GrayScaleFilter " + name);
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        ImageData data = new ImageData(this.dataIn.getImageData());

        float[] a_channel = new float[data.getnCanales()];
        for(int k = 0 ;k<data.getnCanales();k++){
           a_channel[k]=1;
        }
        //identifica el espacio de canal y asi las operacion R=0.3 G= 0.59 B=0.11
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            a_channel[0]=0.2989f;
            a_channel[1]=0.5870f;
            a_channel[2]=0.1140f;
            break;
        case ImageData.BGR:
            a_channel[0]=0.1140f;
            a_channel[1]=0.5870f;
            a_channel[2]=0.2989f;
            break;
      }
        a_channel[0]=1;
        float colorg;
        byte a_byte;
       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                colorg= (BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()]) * a_channel[0] + BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()+1])*a_channel[1] +BytesToInt(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()+2])*a_channel[2]);
                a_byte=generateByte(Math.round(colorg));
                data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()] =a_byte ;
                data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()+1] = a_byte;
                data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales()+2] = a_byte;
                
          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public float BytesToInt(byte valor){
             float d =  valor & 0xFF;
	     return d;
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
