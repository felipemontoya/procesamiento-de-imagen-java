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
public class RGBtoLMSFilter extends FilterPipeObject{

    private String name;

    public RGBtoLMSFilter(String name) {
        super("YUVFilter " + name);
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
           a_channel[k]=0;
        }
        //identifica el espacio de canal y asi las operacion R=0.3 G= 0.59 B=0.11
        boolean RGB=true;;
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            RGB=true;
            break;
        case ImageData.BGR:
            RGB=false;
            break;
      }
      byte[] lms;
       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                if(RGB){
                  lms=LMSspace(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]);

                }else{
                    lms=LMSspace(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]);
                  }
                data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]= lms[0];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]= lms[1];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]= lms[2];



          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }


    private byte[] LMSspace(byte R,byte G, byte B){
        float aux;
        int x=spaceX(R,G,B);
        int y=spaceY(R,G,B);
        int z=spaceZ(R,G,B);
        byte [] LMS = new byte[3];
        aux = (float)((0.8562 * x) + ( 0.3372 * y) + (-0.1934 * z));
        LMS[0]=generateByte(Math.round(aux));
        aux = (float)((-0.8360 * y) + ( 1.827 * y) + (0.0033 * z));
        LMS[1]=generateByte(Math.round(aux));
        aux = (float)((0.0357 * z) + ( -0.0469 * y) + (1.0112 * z));
        LMS[2]=generateByte(Math.round(aux));

        return LMS;
    }





    private int spaceX(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((0.6097559 * RR) + ( 0.2052401 * GG) + (0.1492240 * BB));
        return Math.round(y);
    }

    private int spaceY(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((0.3111242 * RR) + (0.6256560 * GG) + ( 0.0632197 * BB));
        return Math.round(y);
    }

    private int spaceZ(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((0.0194811 * RR) + (0.0608902 * GG) + (  0.7448387 * BB));
        return Math.round(y);
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

