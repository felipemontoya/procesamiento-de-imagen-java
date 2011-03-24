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
public class RGBtoHSVFilter extends FilterPipeObject{

    private String name;

    public RGBtoHSVFilter(String name) {
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
        byte[] hsv=new byte[3];
       for(int i = 0;i<data.getHeight();i++){
            for(int j = 0;j<data.getWidth();j++){

                if(RGB){
                  hsv= toHSV(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],hsv);

                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]=hsv[0];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]=hsv[1];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]=hsv[2];
                }else{
                  hsv= toHSV(data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1],data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0],hsv);

                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 2]=hsv[0];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 1]=hsv[1];
                  data.bytesImage[(i * data.getWidthStep() + j ) * data.getnCanales() + 0]=hsv[2];
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


    private byte[] toHSV(byte R,byte G, byte B,byte[] hsv){
        int[] rgb = new int[3];
        float aux;
        int max=Integer.MIN_VALUE,min=Integer.MAX_VALUE;

//        fuente : http://en.literateprograms.org/RGB_to_HSV_color_space_conversion_(C)#chunk def:compute value
        rgb[0]=BytesToInt(R);
        rgb[1]=BytesToInt(G);
        rgb[2]=BytesToInt(B);
        for(int i:rgb){
            if(i>max)
                max=i;

            if(i<min)
                min=i;

        }
        hsv[2]=generateByte(max);

        if(max==0){
            hsv[0]=hsv[1]=0x0;
            return hsv;
        }

        hsv[1] =generateByte((int)(255*(max - min)/max));
        if (hsv[1] == 0x0) {
            hsv[0] = 0x0;
            return hsv;
        }

        if (max == rgb[0]) {
            hsv[0] =generateByte( (int)(0 + 43*(rgb[1] - rgb[2])/(max - min)));
        } else if (max == rgb[1]) {
            hsv[0] =generateByte( (int)(85 + 43*(rgb[2] - rgb[0])/(max - min)));
        } else /* rgb_max == rgb.b */ {
            hsv[0] =generateByte( (int)(171 + 43*(rgb[0] - rgb[1])/(max - min)));
        }
            


        return hsv;
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