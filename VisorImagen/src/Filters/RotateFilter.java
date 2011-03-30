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
public class RotateFilter extends FilterPipeObject{

    private String name;
    private double grados;
    public RotateFilter(String name,double grados) {
        super("RotateFilter " + name);
        this.grados=grados;
        this.name = name;
        this.dataIn = new DataPackage(DataPackage.Type.ImageData);
        this.dataOut = new DataPackage(DataPackage.Type.ImageData);
    }

    //Metodo propio del pipeline, no se debe llamar por fuera de esta!
    @Override
     public boolean InternalUpdate(){
        this.setDataIn(this.getLastElement().getDataOut());
        int ancho =(int) Math.sqrt((dataIn.getImageData().getWidth()*dataIn.getImageData().getWidth()) + (dataIn.getImageData().getHeight()*dataIn.getImageData().getHeight()));
        ImageData data = new ImageData(this.dataIn.getImageData(),ancho,ancho);

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
        


                grados*=Math.PI/180;
                int jj;
                int ii;
                double a;
                double b;


       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                ii=i+(dataIn.getImageData().getWidth()/2);
                  jj=j+(dataIn.getImageData().getHeight()/2);
                  a=((jj*Math.cos(grados))-(ii*Math.sin(grados)));
                  b=((jj*Math.sin(grados))+(ii*Math.cos(grados)));
                  a-=dataIn.getImageData().getWidth()/2;
                  b-=dataIn.getImageData().getHeight()/2;
                if(a<0 || b<0)
                   break; 
                if(RGB){

                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 0]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0];
                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 1]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1];
                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 2]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2];
                }else{
                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 0]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2];
                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 1]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1];
                  data.bytesImage[((int)a * data.getWidthStep() + (int)b ) * data.getnCanales() + 2]=dataIn.getImageData().bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0];
                }



          }
        }



        this.dataOut.setImageData(data);

        System.out.println("Internal update BlankFilter : " + name);
        return  true;
    }

    public int BytesToInt(byte valor){
             return (int)valor & 0xFF;

    }


    private byte spaceY(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((0.257 * RR) + (0.504 * GG) + (0.098 * BB) + 16);
        return generateByte(Math.round(y));
    }

    private byte spaceU(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((-0.148 * RR) - (0.291 * GG) + (0.439 * BB) + 128);
        return generateByte(Math.round(y));
    }

    private byte spaceV(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
        y = (float)((0.439 * RR) - (0.368 * GG) - (0.071 * BB) + 128);
        return generateByte(Math.round(y));
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