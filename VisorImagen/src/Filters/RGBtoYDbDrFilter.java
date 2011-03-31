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
public class RGBtoYDbDrFilter extends FilterPipeObject{

    private String name;

    public RGBtoYDbDrFilter(String name) {
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
        boolean RGB=true;
        switch(data.getEspacioColor()){
        case ImageData.RGB:
            RGB=true;
            break;
        case ImageData.BGR:
            RGB=false;
            break;
      }

       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){
                if(RGB){
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]= spaceY(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]= spaceDb(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]= spaceDr(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]);
                }else{
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]= spaceY(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]= spaceDb(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]);
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]= spaceDr(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]);
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
        y = (float)((0.299 * RR) + (0.587 * GG) + (0.114 * BB));
        return generateByte(Math.round(y));
    }

    private byte spaceDb(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
//        y = (float)((((-0.450 * RR) - (0.883 * GG) + (1.333 * BB))+1.333)*95.6273);
        y = (float)((((-0.450 * RR) + (0.883 * GG) + (1.333 * BB))));
        return generateByte(Math.round(y));
    }

    private byte spaceDr(byte R,byte G, byte B){
        float y;
        int RR=BytesToInt(R);
        int BB=BytesToInt(G);
        int GG=BytesToInt(B);
//        y = (float)((((-1.333 * RR) - (1.116 * GG) - (0.217 * BB))+1.333)*95.6273);
        y = (float)((((-1.333 * RR) + (1.116 * GG) + (0.217 * BB))));
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
